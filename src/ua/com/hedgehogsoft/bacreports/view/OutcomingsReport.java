package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.commons.DateLabelFormatter;
import ua.com.hedgehogsoft.bacreports.commons.Sources;
import ua.com.hedgehogsoft.bacreports.commons.Units;
import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.model.Outcoming;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.print.OutcomingsPrinter;
import ua.com.hedgehogsoft.bacreports.view.table.ProductStoreTableModel;

public class OutcomingsReport
{
   private JButton printButton = null;
   private JButton deleteButton = null;
   private JButton closeButton = null;
   private JTable table = null;
   private static final Logger logger = Logger.getLogger(OutcomingsReport.class);

   public OutcomingsReport(MainFrame mainFrame, String from, String to)
   {
      JFrame reportsFrame = new JFrame("БакЗвіт - списання");

      reportsFrame.pack();

      reportsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            close(reportsFrame);
         }
      });

      closeButton = new JButton("Закрити");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            close(reportsFrame);
         }
      });

      printButton = new JButton("Друкувати");

      printButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new OutcomingsPrinter().print(table, from, to);

            close(reportsFrame);
         }
      });

      deleteButton = new JButton("Видалити");

      deleteButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            Outcoming outcoming = new Connection().getOutcomingById((int) table.getValueAt(table.getSelectedRow(), 0));

            Product existedProduct = new Connection().getProductById(outcoming.getProduct().getId());

            existedProduct.setAmount(existedProduct.getAmount() + outcoming.getProduct().getAmount());

            if (new Connection().updateProduct(existedProduct))
            {
               if (new Connection().deleteOutcomingById(outcoming.getId()))
               {
                  DefaultTableModel model = (DefaultTableModel) table.getModel();

                  model.removeRow(table.getSelectedRow());

                  for (int i = 0; i < mainFrame.getTable().getColumnCount(); i++)
                  {
                     if (mainFrame.getTable().getColumnName(i).equals("№ з/п"))
                     {
                        for (int k = 0; k < mainFrame.getTable().getRowCount(); k++)
                        {
                           if (((int) mainFrame.getTable().getValueAt(k, i)) == existedProduct.getId())
                           {
                              for (int z = 0; z < mainFrame.getTable().getColumnCount(); z++)
                              {
                                 if (mainFrame.getTable().getColumnName(z).equals("Кількість, од."))
                                 {
                                    ((ProductStoreTableModel) mainFrame.getTable().getModel())
                                          .updateAmount(existedProduct);

                                    break;
                                 }
                              }
                              break;
                           }
                        }
                        break;
                     }
                  }

                  Sources sources = new Sources(new Connection().getSources());

                  Units units = new Units(new Connection().getUnits());

                  JPanel panel = new JPanel(new GridLayout(7, 2));
                  panel.add(new JLabel("Дата: "));
                  panel.add(new JLabel(new DateLabelFormatter().dateToString(outcoming.getDate())));
                  panel.add(new JLabel("Найменування: "));
                  panel.add(new JLabel(outcoming.getProduct().getName()));
                  panel.add(new JLabel("Кількість, од.: "));
                  panel.add(new JLabel(Double.toString(outcoming.getProduct().getAmount())));
                  panel.add(new JLabel("Одиниця виміру: "));
                  panel.add(new JLabel(units.valueOf(outcoming.getProduct().getUnit()).getName()));
                  panel.add(new JLabel("Ціна, грн./од.: "));
                  panel.add(new JLabel(Double.toString(outcoming.getProduct().getPrice())));
                  panel.add(new JLabel("Група: "));
                  panel.add(new JLabel(sources.valueOf(outcoming.getProduct().getSource()).getName()));
                  panel.add(new JLabel("Сума, грн.: "));
                  panel.add(new JLabel(Double.toString(outcoming.getProduct().getTotalPrice())));

                  JOptionPane.showMessageDialog(null, panel, "Видалено", JOptionPane.INFORMATION_MESSAGE);

                  close(reportsFrame);
               }
            }
         }
      });

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

      datePanel.add(new JLabel("Початок періоду:"));

      datePanel.add(new JLabel(from));

      datePanel.add(new JLabel("Кінець періоду:"));

      datePanel.add(new JLabel(to));

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(printButton);

      buttonsPanel.add(deleteButton);

      buttonsPanel.add(closeButton);

      JScrollPane scrollPane = new JScrollPane(table = getFilledTable(from, to));

      reportsFrame.add(scrollPane, BorderLayout.CENTER);

      reportsFrame.add(datePanel, BorderLayout.NORTH);

      reportsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      reportsFrame.setSize(1000, 700);

      reportsFrame.setResizable(true);

      reportsFrame.setLocationRelativeTo(null);

      reportsFrame.setVisible(true);

      logger.info("OutcomingsReportFrame was started.");
   }

   private JTable getFilledTable(String from, String to)
   {
      String[] columnNames = {"№ з/п",
                              "Найменування предметів закупівель",
                              "Одиниця виміру",
                              "Дата списання",
                              "Ціна, грн./од.",
                              "Кількість, од.",
                              "Сума, грн.",
                              "Група"};

      List<Outcoming> outcomings = new Connection().getOutcomings(from, to);

      Sources sources = new Sources(new Connection().getSources());

      Units units = new Units(new Connection().getUnits());

      DateLabelFormatter formatter = new DateLabelFormatter();

      DefaultTableModel model = new DefaultTableModel()
      {
         private static final long serialVersionUID = 1L;

         @Override
         public boolean isCellEditable(int row, int column)
         {
            return false;
         }
      };

      model.setColumnIdentifiers(columnNames);

      if (!outcomings.isEmpty())
      {
         for (int i = 0; i < outcomings.size(); i++)
         {
            Product product = outcomings.get(i).getProduct();

            model.addRow(new Object[] {outcomings.get(i).getId(),
                                       product.getName(),
                                       units.valueOf(product.getUnit()).getName(),
                                       formatter.dateToString(outcomings.get(i).getDate()),
                                       product.getPrice(),
                                       product.getAmount(),
                                       product.getTotalPrice(),
                                       sources.valueOf(product.getSource()).getName()});
         }
      }

      JTable table = new JTable(model);

      table.setPreferredScrollableViewportSize(new Dimension(500, 70));

      table.setFillsViewportHeight(true);

      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);

      table.setRowSorter(sorter);

      initColumnSizes(table);

      return table;
   }

   private void initColumnSizes(JTable table)
   {
      DefaultTableModel model = (DefaultTableModel) table.getModel();

      TableColumn column = null;

      Component comp = null;

      int headerWidth = 0;

      int cellWidth = 0;

      TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

      for (int i = 0; i < 5; i++)
      {
         column = table.getColumnModel().getColumn(i);

         comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);

         headerWidth = comp.getPreferredSize().width;

         comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table,
               model.getColumnName(i), false, false, 0, i);

         cellWidth = comp.getPreferredSize().width;

         column.setPreferredWidth(Math.max(headerWidth, cellWidth));
      }
   }

   public JTable getTable()
   {
      return table;
   }

   private void close(JFrame frame)
   {
      frame.dispose();

      logger.info("OutcomingsReport was closed.");
   }
}
