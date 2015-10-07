package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class ReportsFrame
{
   private JButton storeButton = null;
   private JButton incomingsButton = null;
   private JButton outcomingsButton = null;
   private JButton closeButton = null;
   private JDatePickerImpl datePickerFrom = null;
   private JDatePickerImpl datePickerTo = null;
   private static final Logger logger = Logger.getLogger(ReportsFrame.class);

   public ReportsFrame()
   {
      JFrame reportsFrame = new JFrame("��������� - ������");

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

      storeButton = new JButton("�������� �� ������");

      storeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            // TODO Auto-generated method stub

         }
      });

      incomingsButton = new JButton("�����������");

      incomingsButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new IncomingsReport(datePickerFrom.getJFormattedTextField().getText(),
                  datePickerTo.getJFormattedTextField().getText());
         }
      });

      outcomingsButton = new JButton("��������");

      outcomingsButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new OutcomingsReport(datePickerFrom.getJFormattedTextField().getText(),
                  datePickerTo.getJFormattedTextField().getText());
         }
      });

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(storeButton);

      buttonsPanel.add(incomingsButton);

      buttonsPanel.add(outcomingsButton);

      buttonsPanel.add(closeButton);

      JPanel datePanel = new JPanel(new GridLayout(2, 2));

      datePanel.add(new Label("������ �������:"));

      datePanel.add(new Label("����� �������:"));

      datePanel.add(datePickerFrom = createDatePicker());

      datePanel.add(datePickerTo = createDatePicker());

      reportsFrame.add(datePanel, BorderLayout.CENTER);

      reportsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      reportsFrame.pack();

      reportsFrame.setResizable(false);

      reportsFrame.setLocationRelativeTo(null);

      reportsFrame.setVisible(true);

      logger.info("ReportsFrame was started.");
   }

   private JDatePickerImpl createDatePicker()
   {
      UtilDateModel model = new UtilDateModel();

      Properties props = new Properties();

      props.put("text.today", "�������");

      props.put("text.month", "�����");

      props.put("text.year", "���");

      JDatePanelImpl datePanel = new JDatePanelImpl(model, props);

      return new JDatePickerImpl(datePanel, new DateLabelFormatter());
   }
}
