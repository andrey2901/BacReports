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

public class FinalReportDateRangeFrame
{
   private JButton okButton = null;
   private JButton closeButton = null;
   private JComboBox<String> monthComboBox = null;
   private JComboBox<Integer> yearComboBox = null;
   private static final Logger logger = Logger.getLogger(FinalReportDateRangeFrame.class);

   public FinalReportDateRangeFrame()
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

            new FinalReportFrame(formatter.dateToString(ranger.from()), formatter.dateToString(ranger.to()));

            frame.dispose();
         }
      });

      JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

      buttonsPanel.add(okButton);

      buttonsPanel.add(closeButton);

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

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

      frame.add(datePanel, BorderLayout.CENTER);

      frame.add(buttonsPanel, BorderLayout.SOUTH);

      frame.pack();

      frame.setResizable(false);

      frame.setLocationRelativeTo(null);

      frame.setVisible(true);

      logger.info("FinalReportDateRangeFrame was started.");
   }

   private void close(JFrame frame)
   {
      logger.info("FinalReportDateRangeFrame was closed.");

      frame.dispose();
   }
}
