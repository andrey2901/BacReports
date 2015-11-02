package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.commons.Sources;
import ua.com.hedgehogsoft.bacreports.commons.Units;
import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.print.ActPatronReportPrinter;
import ua.com.hedgehogsoft.bacreports.view.table.ProductStoreTableModel;

public class ActReportPatronFrame
{
   private JButton printButton = null;
   private JButton closeButton = null;
   private JTable table = null;
   private static final Logger logger = Logger.getLogger(ActReportFrame.class);

   public ActReportPatronFrame(String dateFrom, String dateTo)
   {
      JFrame actsFrame = new JFrame("БакЗвіт - акт списання");

      actsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("ActReportFrame was closed.");

            actsFrame.dispose();
         }
      });

      closeButton = new JButton("Закрити");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            actsFrame.dispose();

            logger.info("ActReportFrame was closed.");
         }
      });

      printButton = new JButton("Друкувати");

      printButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new ActPatronReportPrinter().print(table, dateFrom, dateTo);

            actsFrame.dispose();

            logger.info("ActReportFrame was closed.");
         }
      });

      JPanel titlePanel = new JPanel(new GridLayout(5, 1));

      titlePanel.add(new JLabel("Акт", SwingConstants.CENTER));

      titlePanel
            .add(new JLabel("списання поживних середовищ і хімреактивів, лабораторного скла,", SwingConstants.CENTER));

      titlePanel.add(new JLabel(
            "використаних Централізованою баклабораторією Лівобережжя КЗ \"Дніпропетровьска міська клінічна лікарня №9\" ДОР\"",
            SwingConstants.CENTER));

      titlePanel.add(new JLabel("з " + dateFrom + " до " + dateTo, SwingConstants.CENTER));

      titlePanel.add(new JLabel("\"Меценат\"", SwingConstants.CENTER));

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(printButton);

      buttonsPanel.add(closeButton);

      JScrollPane scrollPane = new JScrollPane(table = getFilledTable(dateFrom, dateTo));

      actsFrame.add(scrollPane, BorderLayout.CENTER);

      actsFrame.add(titlePanel, BorderLayout.NORTH);

      actsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      actsFrame.pack();

      actsFrame.setSize(1000, 700);

      actsFrame.setResizable(true);

      actsFrame.setLocationRelativeTo(null);

      actsFrame.setVisible(true);

      logger.info("ActReportFrame was started.");
   }

   private JTable getFilledTable(String dateFrom, String dateTo)
   {
      String[] columnNames = {"№ з/п",
                              "Найменування предметів закупівель",
                              "Одиниця виміру",
                              "Група",
                              "Ціна, грн./од.",
                              "Кількість, од.",
                              "Сума, грн."};

      List<Integer> ids = new Connection().getUniqueIdsForOutcomingsForPatron(dateFrom, dateTo);

      List<Product> products = new ArrayList<Product>();

      for (int id : ids)
      {
         products.add(new Connection().getOutcomingProduct(id, dateFrom, dateTo));
      }

      ProductStoreTableModel model = new ProductStoreTableModel(products, columnNames);

      if (!products.isEmpty())
      {
         for (int i = 0; i < products.size(); i++)
         {
            Product product = products.get(i);

            model.addRow(new Object[] {i + 1,
                                       product.getName(),
                                       new Units(new Connection().getUnits()).valueOf(product.getUnit()).getName(),
                                       new Sources(new Connection().getSources()).valueOf(product.getSource())
                                             .getName(),
                                       product.getPrice(),
                                       product.getAmount(),
                                       product.getTotalPrice(),});
         }
      }

      table = new JTable(model);

      table.setPreferredScrollableViewportSize(new Dimension(500, 70));

      table.setFillsViewportHeight(true);

      table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

      RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);

      table.setRowSorter(sorter);

      initColumnSizes(table);

      return table;
   }

   private void initColumnSizes(JTable table)
   {
      AbstractTableModel model = (AbstractTableModel) table.getModel();

      TableColumn column = null;

      Component comp = null;

      int headerWidth = 0;

      int cellWidth = 0;

      TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

      for (int i = 0; i < 7; i++)
      {
         switch (i)
         {
            case 0:

               column = table.getColumnModel().getColumn(i);

               column.setMaxWidth(50);

               column.setMinWidth(50);

               column.setPreferredWidth(50);

               DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

               renderer.setHorizontalAlignment(SwingConstants.CENTER);

               column.setCellRenderer(renderer);

               break;

            case 1:

               column = table.getColumnModel().getColumn(i);

               comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);

               headerWidth = comp.getPreferredSize().width;

               comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table,
                     model.getColumnName(i), false, false, 0, i);

               cellWidth = comp.getPreferredSize().width;

               column.setPreferredWidth(Math.max(headerWidth, cellWidth));

               break;

            case 2:
            case 4:
            case 5:
            case 6:

               column = table.getColumnModel().getColumn(i);

               column.setMinWidth(100);

               column.setMaxWidth(100);

               column.setPreferredWidth(100);

               break;

            case 3:

               column = table.getColumnModel().getColumn(i);

               column.setMinWidth(100);

               column.setMaxWidth(200);

               column.setPreferredWidth(100);

               break;
         }
      }
   }
}
