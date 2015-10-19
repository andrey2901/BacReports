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
import java.util.Enumeration;
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
import ua.com.hedgehogsoft.bacreports.print.Printer;
import ua.com.hedgehogsoft.bacreports.view.table.MultiLineHeaderRenderer;
import ua.com.hedgehogsoft.bacreports.view.table.ProductStoreTableModel;

public class FinalReportFrame
{
   private JButton printButton = null;
   private JButton closeButton = null;
   private JTable table = null;
   private static final Logger logger = Logger.getLogger(FinalReportFrame.class);

   public FinalReportFrame(String dateFrom, String dateTo)
   {
      JFrame finalReportFrame = new JFrame("БакЗвіт - загальний звіт");

      finalReportFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("FinalReportFrame was closed.");

            finalReportFrame.dispose();
         }
      });

      closeButton = new JButton("Закрити");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            finalReportFrame.dispose();

            logger.info("FinalReportFrame was closed.");
         }
      });

      printButton = new JButton("Друкувати");

      printButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new Printer().print(table);

            finalReportFrame.dispose();

            logger.info("FinalReportFrame was closed.");
         }
      });

      JPanel titlePanel = new JPanel(new GridLayout(4, 1));

      titlePanel.add(new JLabel("Звіт", SwingConstants.CENTER));

      titlePanel.add(new JLabel("про надходження і відпуск (використання)", SwingConstants.CENTER));

      titlePanel.add(new JLabel("лікарських засобів та медичних виробів", SwingConstants.CENTER));

      titlePanel.add(new JLabel("з " + dateFrom + " до " + dateTo, SwingConstants.CENTER));

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(printButton);

      buttonsPanel.add(closeButton);

      JScrollPane scrollPane = new JScrollPane(table = getFilledTable(dateFrom, dateTo));

      finalReportFrame.add(scrollPane, BorderLayout.CENTER);

      finalReportFrame.add(titlePanel, BorderLayout.NORTH);

      finalReportFrame.add(buttonsPanel, BorderLayout.SOUTH);

      finalReportFrame.pack();

      finalReportFrame.setSize(1000, 700);

      finalReportFrame.setResizable(true);

      finalReportFrame.setLocationRelativeTo(null);

      finalReportFrame.setVisible(true);

      logger.info("FinalReportFrame was started.");
   }

   private JTable getFilledTable(String dateFrom, String dateTo)
   {
      String[] columnNames = {"№ з/п",
                              "Найменування лікарських засобів та медичних виробів",
                              "Одиниця виміру",
                              "Залишок на\nпочаток періоду",
                              "Надходження",
                              "Використання",
                              "Залишок на\nкінець періоду",
                              "Група"};

      List<Integer> ids = new Connection().getIds();

      List<Product> products = new ArrayList<Product>(ids.size());

      ProductStoreTableModel model = new ProductStoreTableModel(products, columnNames);

      int i = 0;

      for (int id : ids)
      {
         double incomingSumBetweenDates = new Connection().getIncomingSumsBetweenDates(id, dateFrom, dateTo);

         double outcomingSumBetweenDates = new Connection().getOutcomingSumsBetweenDates(id, dateFrom, dateTo);

         double incomingSumFromDates = new Connection().getIncomingSumsFromDate(id, dateFrom);

         double outcomingSumFromDates = new Connection().getOutcomingSumsFromDate(id, dateFrom);

         double incomingSumToDates = new Connection().getIncomingSumsFromDate(id, dateTo);

         double outcomingSumToDates = new Connection().getOutcomingSumsFromDate(id, dateTo);

         Product product = new Connection().getProductById(id);

         model.addRow(new Object[] {i++,
                                    product.getName(),
                                    new Units(new Connection().getUnits()).valueOf(product.getUnit()).getName(),
                                    product.getAmount() + outcomingSumFromDates - incomingSumFromDates,
                                    incomingSumBetweenDates,
                                    outcomingSumBetweenDates,
                                    product.getAmount() + outcomingSumToDates - incomingSumToDates,
                                    new Sources(new Connection().getSources()).valueOf(product.getSource()).getName()});
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

      MultiLineHeaderRenderer rend = new MultiLineHeaderRenderer();

      Enumeration<TableColumn> e = table.getColumnModel().getColumns();

      while (e.hasMoreElements())
      {
         ((TableColumn) e.nextElement()).setHeaderRenderer(rend);
      }

      TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

      for (int i = 0; i < 8; i++)
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
            case 3:
            case 4:
            case 5:
            case 6:

               column = table.getColumnModel().getColumn(i);

               column.setMinWidth(100);

               column.setMaxWidth(100);

               column.setPreferredWidth(100);

               break;

            case 7:

               column = table.getColumnModel().getColumn(i);

               column.setMinWidth(100);

               column.setMaxWidth(200);

               column.setPreferredWidth(100);

               break;
         }
      }
   }
}
