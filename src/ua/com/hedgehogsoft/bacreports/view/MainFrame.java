package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.commons.DirectoryCopywriter;
import ua.com.hedgehogsoft.bacreports.commons.Sources;
import ua.com.hedgehogsoft.bacreports.commons.Units;
import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.listener.IncomingActionListener;
import ua.com.hedgehogsoft.bacreports.listener.OutcomingActionListener;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.view.table.ProductStoreTableModel;

public class MainFrame
{
   private JButton incomingButton = null;
   private JButton outcomingButton = null;
   private JButton reportsButton = null;
   private JButton backupButton = null;
   private JButton exitButton = null;
   private JTable table = null;
   private Sources sources = null;
   private Units units = null;
   private MainFrame own = null;
   private static final Logger logger = Logger.getLogger(MainFrame.class);

   public MainFrame()
   {
      own = this;

      sources = new Sources(new Connection().getSources());

      units = new Units(new Connection().getUnits());

      JFrame mainFrame = new JFrame("������ - �����");

      mainFrame.setLayout(new BorderLayout());

      mainFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            close();
         }
      });

      incomingButton = new JButton("�����������");

      incomingButton.addActionListener(new IncomingActionListener(this));

      outcomingButton = new JButton("������������");

      outcomingButton.addActionListener(new OutcomingActionListener(this));

      reportsButton = new JButton("����");

      reportsButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new ReportsFrame(own);
         }
      });

      exitButton = new JButton("�����");

      exitButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent arg0)
         {
            close();
         }
      });

      backupButton = new JButton("����������");

      backupButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent arg0)
         {
            new DirectoryCopywriter().copy();
         }
      });

      JPanel buttonsPanel = new JPanel(new BorderLayout());

      JPanel comingButtonPanel = new JPanel();

      comingButtonPanel.add(incomingButton, BorderLayout.WEST);

      comingButtonPanel.add(outcomingButton, BorderLayout.EAST);

      JPanel functionalButtonPanel = new JPanel();

      functionalButtonPanel.add(reportsButton, BorderLayout.WEST);

      functionalButtonPanel.add(exitButton, BorderLayout.EAST);

      buttonsPanel.add(comingButtonPanel, BorderLayout.NORTH);

      buttonsPanel.add(functionalButtonPanel, BorderLayout.SOUTH);

      JScrollPane scrollPane = new JScrollPane(table = getFilledTable());

      mainFrame.add(scrollPane, BorderLayout.CENTER);

      mainFrame.add(buttonsPanel, BorderLayout.SOUTH);

      mainFrame.add(backupButton, BorderLayout.NORTH);

      mainFrame.setSize(1000, 700);

      mainFrame.setResizable(true);

      mainFrame.setLocationRelativeTo(null);

      mainFrame.setVisible(true);

      logger.info("BacReports was started.");
   }

   private JTable getFilledTable()
   {
      String[] columnNames = {"� �/�",
                              "������������ ��������� ������ �� �������� ������",
                              "������� �����",
                              "ֳ��, ���./��.",
                              "ʳ������, ��.",
                              "����, ���.",
                              "�����"};

      List<Product> products = new Connection().getStore();

      ProductStoreTableModel model = new ProductStoreTableModel(products, columnNames);

      if (!products.isEmpty())
      {
         for (int i = 0; i < products.size(); i++)
         {
            Product product = products.get(i);

            model.addRow(new Object[] {product.getId(),
                                       product.getName(),
                                       units.valueOf(product.getUnit()).getName(),
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
            case 3:
            case 4:
            case 5:

               column = table.getColumnModel().getColumn(i);

               column.setMinWidth(100);

               column.setMaxWidth(100);

               column.setPreferredWidth(100);

               break;

            case 6:

               column = table.getColumnModel().getColumn(i);

               column.setMinWidth(100);

               column.setMaxWidth(200);

               column.setPreferredWidth(100);

               break;
         }
      }
   }

   public JTable getTable()
   {
      return table;
   }

   public Sources getSources()
   {
      return sources;
   }

   private void close()
   {
      Connection.shutdownDB();

      logger.info("BacReports was finished.");

      System.exit(0);
   }
}
