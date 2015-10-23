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
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import ua.com.hedgehogsoft.bacreports.commons.DateLabelFormatter;
import ua.com.hedgehogsoft.bacreports.commons.DateRange;

public class ActReportDateRangeFrame
{
   private JButton okButton = null;
   private JButton closeButton = null;
   private JComboBox<String> monthComboBox = null;
   private JComboBox<Integer> yearComboBox = null;
   private JComboBox<String> sourceComboBox = null;
   private static final Logger logger = Logger.getLogger(ActReportDateRangeFrame.class);

   public ActReportDateRangeFrame()
   {
      JFrame frame = new JFrame("БакЗвіт - оберить період");

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
            DateRange ranger = new DateRange(monthComboBox.getSelectedIndex(), (int) yearComboBox.getSelectedItem());

            DateLabelFormatter formatter = new DateLabelFormatter();

            if (sourceComboBox.getSelectedIndex() == 1)
            {
               new ActReportPatronFrame(formatter.dateToString(ranger.from()), formatter.dateToString(ranger.to()));
            }
            else
            {
               new ActReportFrame(formatter.dateToString(ranger.from()), formatter.dateToString(ranger.to()));
            }

            frame.dispose();
         }
      });

      JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

      buttonsPanel.add(okButton);

      buttonsPanel.add(closeButton);

      JPanel datePanel = new JPanel(new GridLayout(3, 2));

      datePanel.add(new JLabel("Місяць:"));

      monthComboBox = new JComboBox<String>(
            new String[] {"Січень",
                          "Лютий",
                          "Березень",
                          "Квітень",
                          "Травень",
                          "Червень",
                          "Липень",
                          "Серпень",
                          "Вересень",
                          "Жовтень",
                          "Листопад",
                          "Грудень"});

      datePanel.add(monthComboBox);

      datePanel.add(new JLabel("Рік:"));

      yearComboBox = new JComboBox<Integer>();

      for (int i = 2015; i < 2026; i++)
      {
         yearComboBox.addItem(i);
      }

      datePanel.add(yearComboBox);

      datePanel.add(new JLabel("Група:"));

      sourceComboBox = new JComboBox<String>(new String[] {"Бюджет", "Меценат"});

      datePanel.add(sourceComboBox);

      frame.add(datePanel, BorderLayout.CENTER);

      frame.add(buttonsPanel, BorderLayout.SOUTH);

      frame.pack();

      frame.setResizable(false);

      frame.setLocationRelativeTo(null);

      frame.setVisible(true);

      logger.info("ActReportDateRangeFrame was started.");
   }

   private void close(JFrame frame)
   {
      logger.info("ActReportDateRangeFrame was closed.");

      frame.dispose();
   }
}
