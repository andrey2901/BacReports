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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.model.Incoming;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.print.Printer;

public class IncomingsReport
{
   private JButton printButton = null;
   private JButton closeButton = null;
   private JTable table = null;
   private static final Logger logger = Logger.getLogger(IncomingsReport.class);

   public IncomingsReport(String from, String to)
   {
      JFrame reportsFrame = new JFrame("БакОтчеты - Поступления");

      reportsFrame.pack();

      reportsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("IncomingsReport was closed.");

            reportsFrame.dispose();
         }
      });

      closeButton = new JButton("Закрыть");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            reportsFrame.dispose();

            logger.info("IncomingsReport was closed.");
         }
      });

      printButton = new JButton("Печать");

      printButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new Printer().print(table);

            reportsFrame.dispose();

            logger.info("IncomingsReport was closed.");
         }
      });

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

      datePanel.add(new JLabel("Начало периода:"));

      datePanel.add(new JLabel(from));

      datePanel.add(new JLabel("Конец периода:"));

      datePanel.add(new JLabel(to));

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(printButton);

      buttonsPanel.add(closeButton);

      JScrollPane scrollPane = new JScrollPane(table = getFilledTable(from, to));

      reportsFrame.add(scrollPane, BorderLayout.CENTER);

      reportsFrame.add(datePanel, BorderLayout.NORTH);

      reportsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      reportsFrame.pack();

      reportsFrame.setSize(1000, 700);

      reportsFrame.setResizable(true);

      reportsFrame.setLocationRelativeTo(null);

      reportsFrame.setVisible(true);

      logger.info("ReportsFrame was started.");
   }

   private JTable getFilledTable(String from, String to)
   {
      String[] columnNames = {"№, п/п",
                              "Наименование товара",
                              "Цена, грн./ед.",
                              "Количество, ед.",
                              "Сумма, грн.",
                              "Дата поступления"};

      List<Incoming> incomings = new Connection().getIncomings(from, to);

      DefaultTableModel model = new DefaultTableModel();

      model.setColumnIdentifiers(columnNames);

      if (!incomings.isEmpty())
      {
         for (int i = 0; i < incomings.size(); i++)
         {
            Product product = incomings.get(i).getProduct();

            model.addRow(new Object[] {i + 1,
                                       product.getName(),
                                       product.getPrice(),
                                       product.getAmount(),
                                       product.getTotalPrice(),
                                       incomings.get(i).getDate()});
         }
      }

      JTable table = new JTable(model);

      table.setPreferredScrollableViewportSize(new Dimension(500, 70));

      table.setFillsViewportHeight(true);

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
}
