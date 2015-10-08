package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.model.Outcoming;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.print.Printer;

public class OutcomingsReport
{
   private JButton printButton = null;
   private JButton closeButton = null;
   private JTable table = null;
   private static final Logger logger = Logger.getLogger(OutcomingsReport.class);

   public OutcomingsReport(String from, String to)
   {
      JFrame reportsFrame = new JFrame("БакОтчеты - Списания");

      reportsFrame.pack();

      reportsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("OutcomingsReport was closed.");

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

            logger.info("OutcomingsReport was closed.");
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

            logger.info("OutcomingsReport was closed.");
         }
      });

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

      datePanel.add(new Label("Начало периода:"));

      datePanel.add(new Label(from));

      datePanel.add(new Label("Конец периода:"));

      datePanel.add(new Label(to));

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(printButton);

      buttonsPanel.add(closeButton);

      JScrollPane scrollPane = new JScrollPane(table = getFilledTable(from, to));

      reportsFrame.add(scrollPane, BorderLayout.CENTER);

      reportsFrame.add(datePanel, BorderLayout.NORTH);

      reportsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      reportsFrame.pack();

      reportsFrame.setResizable(true);

      reportsFrame.setLocationRelativeTo(null);

      reportsFrame.setVisible(true);

      logger.info("OutcomingsReportFrame was started.");
   }

   private JTable getFilledTable(String from, String to)
   {
      String[] columnNames = {"№, п/п",
                              "Наименование товара",
                              "Цена, грн./ед.",
                              "Количество, ед.",
                              "Сумма, грн.",
                              "Дата списания"};

      List<Outcoming> outcomings = new Connection().getOutcomings(from, to);

      DefaultTableModel model = new DefaultTableModel();

      model.setColumnIdentifiers(columnNames);

      if (!outcomings.isEmpty())
      {
         for (int i = 0; i < outcomings.size(); i++)
         {
            Product product = outcomings.get(i).getProduct();

            model.addRow(new Object[] {i + 1,
                                       product.getName(),
                                       product.getPrice(),
                                       product.getAmount(),
                                       product.getTotalPrice(),
                                       outcomings.get(i).getDate()});
         }
      }

      JTable table = new JTable(model);

      table.setPreferredScrollableViewportSize(new Dimension(500, 70));

      table.setFillsViewportHeight(true);

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
