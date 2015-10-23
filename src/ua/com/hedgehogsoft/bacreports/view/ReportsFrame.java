package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class ReportsFrame
{
   private JButton remainsReportButton = null;
   private JButton actReportButton = null;
   private JButton finalReportButton = null;
   private JButton incomingsReportButton = null;
   private JButton outcomingsReportButton = null;
   private JButton closeButton = null;
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
            new RemainsReportDateRangeFrame();
         }
      });

      actReportButton = new JButton("��� ��������");

      actReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new ActReportDateRangeFrame();
         }
      });

      finalReportButton = new JButton("���");

      finalReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new FinalReportDateRangeFrame();
         }
      });

      incomingsReportButton = new JButton("�����������");

      incomingsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new IncomingsReportDateRangeFrame();
         }
      });

      outcomingsReportButton = new JButton("������������");

      outcomingsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new OutcomingsReportDateRangeFrame();
         }
      });

      JPanel buttonsPanel = new JPanel(new GridLayout(2, 3));

      buttonsPanel.add(remainsReportButton);

      buttonsPanel.add(actReportButton);

      buttonsPanel.add(finalReportButton);

      buttonsPanel.add(incomingsReportButton);

      buttonsPanel.add(outcomingsReportButton);

      buttonsPanel.add(closeButton);

      reportsFrame.add(buttonsPanel, BorderLayout.CENTER);

      reportsFrame.pack();

      reportsFrame.setResizable(false);

      reportsFrame.setLocationRelativeTo(null);

      reportsFrame.setVisible(true);

      logger.info("ReportsFrame was started.");
   }
}
