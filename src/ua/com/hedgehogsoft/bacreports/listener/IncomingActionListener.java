package ua.com.hedgehogsoft.bacreports.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ua.com.hedgehogsoft.bacreports.view.IncomingsFrame;
import ua.com.hedgehogsoft.bacreports.view.MainFrame;

public class IncomingActionListener implements ActionListener
{
   public MainFrame mainFrame = null;

   public IncomingActionListener(MainFrame mainFrame)
   {
      this.mainFrame = mainFrame;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      new IncomingsFrame(mainFrame);
   }
}
