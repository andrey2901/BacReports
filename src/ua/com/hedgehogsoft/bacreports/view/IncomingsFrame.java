package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class IncomingsFrame
{
   private JButton closeButton = null;

   private JButton incomingButton = null;

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
            logger.info("Incomings were performed.");
         }
      });

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(incomingButton);

      buttonsPanel.add(closeButton);

      mainFrame.add(buttonsPanel, BorderLayout.SOUTH);

      mainFrame.setSize(900, 661);

      mainFrame.setResizable(false);

      mainFrame.setLocationRelativeTo(null);

      mainFrame.setVisible(true);

      logger.info("IncomingsFrame was started.");
   }
}
