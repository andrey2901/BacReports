package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ua.com.hedgehogsoft.bacreports.db.Connection;

public class IncomingsFrame
{
   private JButton closeButton = null;
   private JButton incomingButton = null;
   private JDatePickerImpl datePicker = null;
   private JComboBox<String> incomingNameComboBox = null;
   private JComboBox<String> incomingCostComboBox = null;
   private static final Logger logger = Logger.getLogger(IncomingsFrame.class);

   public IncomingsFrame()
   {
      final JFrame mainFrame = new JFrame("БакОтчеты - приход");

      mainFrame.pack();

      mainFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("IncomingsFrame was closed.");

            mainFrame.dispose();
         }
      });

      closeButton = new JButton("Закрыть");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            mainFrame.dispose();

            logger.info("IncomingsFrame was closed.");
         }
      });

      incomingButton = new JButton("Оприходовать");

      incomingButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            logger.info(datePicker.getJFormattedTextField().getText());

            logger.info("Incomings were performed.");
         }
      });

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(incomingButton);

      buttonsPanel.add(closeButton);

      /*--------------------------------------------------------------*/
      UtilDateModel model = new UtilDateModel();

      Properties props = new Properties();
      props.put("text.today", "Сегодня");
      props.put("text.month", "Месяц");
      props.put("text.year", "Год");

      JDatePanelImpl datePanel = new JDatePanelImpl(model, props);

      datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
      /*--------------------------------------------------------------*/
      JPanel incomingPanel = new JPanel(new GridLayout(4, 2));

      incomingPanel.add(new JLabel("Наименование товара:"));

      incomingNameComboBox = new JComboBox<String>();

      incomingNameComboBox.setEditable(true);

      List<String> names = new Connection().getUniqueProductNames();

      if (!names.isEmpty())
      {
         for (String name : names)
         {
            incomingNameComboBox.addItem(name);
         }
      }

      incomingNameComboBox.setSelectedItem("");

      incomingPanel.add(incomingNameComboBox);

      incomingPanel.add(new JLabel("Цена, грн./ед.:"));

      incomingCostComboBox = new JComboBox<String>();

      incomingCostComboBox.setEditable(true);

      incomingPanel.add(incomingCostComboBox);

      incomingNameComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            incomingCostComboBox.removeAllItems();

            List<Double> prices = new Connection().getPricesByProduct((String) incomingNameComboBox.getSelectedItem());

            if (!prices.isEmpty())
            {
               for (Double price : prices)
               {
                  incomingCostComboBox.addItem(Double.toString(price));
               }
            }
         }
      });

      incomingPanel.add(new JLabel("Количество, ед.:"));

      JPanel incomingAmountPanel = new JPanel();

      incomingAmountPanel.add(new JTextField(5));

      incomingAmountPanel.add(new JLabel(","));

      incomingAmountPanel.add(new JTextField(3));

      incomingAmountPanel.add(new JLabel("ед.     "));

      incomingPanel.add(incomingAmountPanel);

      incomingPanel.add(new JLabel("Дата:"));

      incomingPanel.add(datePicker);

      mainFrame.add(incomingPanel, BorderLayout.CENTER);

      mainFrame.add(buttonsPanel, BorderLayout.SOUTH);

      mainFrame.pack();

      mainFrame.setResizable(false);

      mainFrame.setLocationRelativeTo(null);

      mainFrame.setVisible(true);

      logger.info("IncomingsFrame was started.");
   }
}
