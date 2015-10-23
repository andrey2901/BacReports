package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePickerImpl;

import ua.com.hedgehogsoft.bacreports.commons.DateLabelFormatter;
import ua.com.hedgehogsoft.bacreports.view.date.DatePicker;

public class IncomingsReportDateRangeFrame
{
   private JButton okButton = null;
   private JButton closeButton = null;
   private JDatePickerImpl datePickerFrom = null;
   private JDatePickerImpl datePickerTo = null;
   private static final Logger logger = Logger.getLogger(IncomingsReportDateRangeFrame.class);

   public IncomingsReportDateRangeFrame()
   {
      JFrame frame = new JFrame("������ - ������� �����");

      frame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            close(frame);
         }
      });

      closeButton = new JButton("�������");

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
            // TODO Auto-generated method stub
            if (checkInputData())
            {
               new IncomingsReport(datePickerFrom.getJFormattedTextField().getText(),
                     datePickerTo.getJFormattedTextField().getText());

               close(frame);
            }
         }
      });

      JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

      buttonsPanel.add(okButton);

      buttonsPanel.add(closeButton);

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

      datePanel.add(new JLabel("������� ������:"));

      datePanel.add(new JLabel("ʳ���� ������:"));

      datePanel.add(datePickerFrom = DatePicker.getDatePicker());

      datePanel.add(datePickerTo = DatePicker.getDatePicker());

      frame.add(datePanel, BorderLayout.CENTER);

      frame.add(buttonsPanel, BorderLayout.SOUTH);

      frame.pack();

      frame.setResizable(false);

      frame.setLocationRelativeTo(null);

      frame.setVisible(true);

      logger.info("IncomingsReportDateRangeFrame was started.");
   }

   private boolean checkInputData()
   {
      boolean result = true;

      Date dateFrom = null;
      Date dateTo = null;

      if (datePickerFrom.getJFormattedTextField().getText() == null
            || datePickerFrom.getJFormattedTextField().getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "�������� ���� ������� ������", "�������", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      else
      {
         dateFrom = (Date) new DateLabelFormatter().stringToValue(datePickerFrom.getJFormattedTextField().getText());
      }

      if (datePickerTo.getJFormattedTextField().getText() == null
            || datePickerTo.getJFormattedTextField().getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "�������� ���� ���� ������", "�������", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      else
      {
         dateTo = (Date) new DateLabelFormatter().stringToValue(datePickerTo.getJFormattedTextField().getText());
      }

      if (dateFrom != null && dateTo != null)
      {
         if (dateTo.before(dateFrom))
         {
            JOptionPane.showMessageDialog(null,
                  "ʳ���� ������ �� ���� ���� ����� �� ���� �������.\n��������, ���� �����, ���� ������.",
                  "�������", JOptionPane.ERROR_MESSAGE);
         }
      }

      return result;
   }

   private void close(JFrame frame)
   {
      logger.info("IncomingsReportDateRangeFrame was closed.");

      frame.dispose();
   }
}
