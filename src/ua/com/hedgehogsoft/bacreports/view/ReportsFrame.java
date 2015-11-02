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

   public ReportsFrame(MainFrame mainFrame)
   {
      JFrame reportsFrame = new JFrame("БакЗвіт - звіти");

      reportsFrame.pack();

      reportsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("ReportsFrame was closed.");

            reportsFrame.dispose();
         }
      });

      closeButton = new JButton("Закрити");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            reportsFrame.dispose();

            logger.info("ReportsFrame was closed.");
         }
      });

      remainsReportButton = new JButton("Залишки");

      remainsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new RemainsReportDateRangeFrame();
         }
      });

      actReportButton = new JButton("Акт списання");

      actReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new ActReportDateRangeFrame();
         }
      });

      finalReportButton = new JButton("Звіт");

      finalReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new FinalReportDateRangeFrame();
         }
      });

      incomingsReportButton = new JButton("Надходження");

      incomingsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new IncomingsReportDateRangeFrame(mainFrame);
         }
      });

      outcomingsReportButton = new JButton("Використання");

      outcomingsReportButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            new OutcomingsReportDateRangeFrame(mainFrame);
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
