package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePickerImpl;

import ua.com.hedgehogsoft.bacreports.view.date.DatePicker;

public class RemainsReportDateRangeFrame
{
   private JButton okButton = null;
   private JButton closeButton = null;
   private JDatePickerImpl datePicker = null;
   private JComboBox<String> sourceComboBox = null;
   private static final Logger logger = Logger.getLogger(RemainsReportDateRangeFrame.class);

   public RemainsReportDateRangeFrame()
   {
      JFrame frame = new JFrame("БакЗвіт - оберить дату");

      frame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            close(frame);
         }
      });

      closeButton = new JButton("Закрити");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            close(frame);
         }
      });

      okButton = new JButton("Ok");

      okButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (checkInputData())
            {
               if (sourceComboBox.getSelectedIndex() == 1)
               {
                  new RemainsReportPatronFrame(datePicker.getJFormattedTextField().getText());
               }
               else
               {
                  new RemainsReportFrame(datePicker.getJFormattedTextField().getText());
               }

               close(frame);
            }
         }
      });

      JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

      buttonsPanel.add(okButton);

      buttonsPanel.add(closeButton);

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

      datePanel.add(new JLabel("Група:"));

      sourceComboBox = new JComboBox<String>(new String[] {"Бюджет", "Меценат"});

      datePanel.add(sourceComboBox);

      datePanel.add(new JLabel("Дата:"));

      datePanel.add(datePicker = DatePicker.getDatePicker());

      frame.add(datePanel, BorderLayout.CENTER);

      frame.add(buttonsPanel, BorderLayout.SOUTH);

      frame.pack();

      frame.setResizable(false);

      frame.setLocationRelativeTo(null);

      frame.setVisible(true);

      logger.info("RemainsReportDateRangeFrame was started.");
   }

   private boolean checkInputData()
   {
      boolean result = true;

      if (datePicker.getJFormattedTextField().getText() == null
            || datePicker.getJFormattedTextField().getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле дати", "Помилка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }

      return result;
   }

   private void close(JFrame frame)
   {
      logger.info("RemainsReportDateRangeFrame was closed.");

      frame.dispose();
   }
}
