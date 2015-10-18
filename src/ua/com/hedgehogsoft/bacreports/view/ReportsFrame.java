package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePickerImpl;

import ua.com.hedgehogsoft.bacreports.view.date.DateLabelFormatter;
import ua.com.hedgehogsoft.bacreports.view.date.DatePicker;

public class ReportsFrame
{
   private JButton remainsReportButton = null;
   private JButton actReportButton = null;
   private JButton finalReportButton = null;
   private JButton incomingsReportButton = null;
   private JButton outcomingsReportButton = null;
   private JButton closeButton = null;
   private JDatePickerImpl datePickerFrom = null;
   private JDatePickerImpl datePickerTo = null;
   private static final Logger logger = Logger.getLogger(ReportsFrame.class);

   public ReportsFrame()
   {
      JFrame reportsFrame = new JFrame("������ - ����");

      reportsFrame.pack();

      reportsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("ReportsFrame was closed.");

            reportsFrame.dispose();
         }
      });

      closeButton = new JButton("�������");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            reportsFrame.dispose();

            logger.info("ReportsFrame was closed.");
         }
      });

      remainsReportButton = new JButton("�������");

      remainsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            // TODO Auto-generated method stub
            if (checkInputData())
            {

            }
         }
      });

      actReportButton = new JButton("���");

      actReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            // TODO Auto-generated method stub
            if (checkInputData())
            {

            }
         }
      });

      finalReportButton = new JButton("���");

      finalReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            // TODO Auto-generated method stub
            if (checkInputData())
            {

            }
         }
      });

      incomingsReportButton = new JButton("�����������");

      incomingsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            // TODO Auto-generated method stub
            if (checkInputData())
            {
               // new
               // IncomingsReport(datePickerFrom.getJFormattedTextField().getText(),
               // datePickerTo.getJFormattedTextField().getText());
            }

         }
      });

      outcomingsReportButton = new JButton("������������");

      outcomingsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            // TODO Auto-generated method stub
            if (checkInputData())
            {
               // new
               // OutcomingsReport(datePickerFrom.getJFormattedTextField().getText(),
               // datePickerTo.getJFormattedTextField().getText());
            }
         }
      });

      JPanel buttonsPanel = new JPanel(new GridLayout(2, 3));

      buttonsPanel.add(remainsReportButton);

      buttonsPanel.add(actReportButton);

      buttonsPanel.add(finalReportButton);

      buttonsPanel.add(incomingsReportButton);

      buttonsPanel.add(outcomingsReportButton);

      buttonsPanel.add(closeButton);

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

      datePanel.add(new Label("������� ������:"));

      datePanel.add(new Label("ʳ���� ������:"));

      datePanel.add(datePickerFrom = DatePicker.getDatePicker());

      datePanel.add(datePickerTo = DatePicker.getDatePicker());

      reportsFrame.add(datePanel, BorderLayout.CENTER);

      reportsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      reportsFrame.pack();

      reportsFrame.setResizable(false);

      reportsFrame.setLocationRelativeTo(null);

      reportsFrame.setVisible(true);

      logger.info("ReportsFrame was started.");
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
}
