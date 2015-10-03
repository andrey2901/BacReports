package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

public class MainFrame
{
   private JButton incomingButton = null;
   private JButton outcomingButton = null;
   private JButton reportsButton = null;
   private JButton exitButton = null;
   private static final Logger logger = Logger.getLogger(MainFrame.class);

   public MainFrame()
   {
      final JFrame mainFrame = new JFrame("БакОтчеты");

      mainFrame.setLayout(new BorderLayout());

      mainFrame.pack();

      mainFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("BacReports was finished.");

            System.exit(0);
         }
      });

      incomingButton = new JButton("Приход");

      outcomingButton = new JButton("Списание");

      reportsButton = new JButton("Отчеты");

      exitButton = new JButton("Выход");

      exitButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent arg0)
         {
            logger.info("BacReports was finished.");

            System.exit(0);
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
      
      String[] columnNames = {"№, п/п", "Наименование товара", "Цена, грн./ед.", "Количество, ед.", "Сумма, грн."};

      Object[][] data = {{"Kathy", "Smith", "Snowboarding", new Integer(5), new Boolean(false)},
                         {"John", "Doe", "Rowing", new Integer(3), new Boolean(true)},
                         {"Sue", "Black", "Knitting", new Integer(2), new Boolean(false)},
                         {"Jane", "White", "Speed reading", new Integer(20), new Boolean(true)},
                         {"Joe", "Brown", "Pool", new Integer(10), new Boolean(false)}};

      final JTable table = new JTable(data, columnNames);
      
      table.setPreferredScrollableViewportSize(new Dimension(500, 70));
      
      table.setFillsViewportHeight(true);
      
      JScrollPane scrollPane = new JScrollPane(table);
      
      mainFrame.add(scrollPane, BorderLayout.CENTER);

      mainFrame.add(buttonsPanel, BorderLayout.SOUTH);

      mainFrame.setSize(900, 661);

      mainFrame.setResizable(false);

      mainFrame.setLocationRelativeTo(null);

      mainFrame.setVisible(true);

      logger.info("BacReports was started.");
   }
}
