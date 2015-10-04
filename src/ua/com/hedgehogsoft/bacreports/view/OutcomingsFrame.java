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
import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ua.com.hedgehogsoft.bacreports.db.Connection;

public class OutcomingsFrame
{
   private JButton closeButton = null;
   private JButton outcomingButton = null;
   private JDatePickerImpl datePicker = null;
   private static final Logger logger = Logger.getLogger(OutcomingsFrame.class);

   public OutcomingsFrame()
   {
      final JFrame mainFrame = new JFrame("БакОтчеты - списание");

      mainFrame.pack();

      mainFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("OutcomingsFrame was closed.");

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

            logger.info("OutcomingsFrame was closed.");
         }
      });

      outcomingButton = new JButton("Списать");

      outcomingButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            logger.info(datePicker.getJFormattedTextField().getText());

            logger.info("Outcomings were performed.");
         }
      });

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(outcomingButton);

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
      JPanel outcomingPanel = new JPanel(new GridLayout(4, 2));

      outcomingPanel.add(new JLabel("Наименование товара:"));

      JComboBox<String> outcomingNameComboBox = new JComboBox<String>();

      List<String> names = new Connection().getUniqueProductNames();

      if (!names.isEmpty())
      {
         for (String name : names)
         {
            outcomingNameComboBox.addItem(name);
         }
      }

      JPanel outcomingNamePanel = new JPanel();

      outcomingNamePanel.add(outcomingNameComboBox);

      outcomingPanel.add(outcomingNamePanel);

      outcomingPanel.add(new JLabel("Цена, грн./ед.:"));

      JPanel outcomingCostPanel = new JPanel();

      JComboBox<String> outcomingCostComboBox = new JComboBox<String>();

      outcomingCostPanel.add(outcomingCostComboBox);

      outcomingPanel.add(outcomingCostPanel);

      outcomingPanel.add(new JLabel("Количество, ед.:"));

      JPanel outcomingAmountPanel = new JPanel();

      JComboBox<String> outcomingAmountComboBox = new JComboBox<String>();

      outcomingAmountPanel.add(outcomingAmountComboBox);

      outcomingPanel.add(outcomingAmountPanel);

      outcomingPanel.add(new JLabel("Дата:"));

      JPanel outcomingDatePanel = new JPanel();

      outcomingDatePanel.add(datePicker);

      outcomingPanel.add(outcomingDatePanel);

      mainFrame.add(outcomingPanel, BorderLayout.CENTER);

      mainFrame.add(buttonsPanel, BorderLayout.SOUTH);

      mainFrame.pack();

      mainFrame.setResizable(false);

      mainFrame.setLocationRelativeTo(null);

      mainFrame.setVisible(true);

      logger.info("OutcomingsFrame was started.");
   }
}
