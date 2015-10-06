package ua.com.hedgehogsoft.bacreports.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ua.com.hedgehogsoft.bacreports.view.MainFrame;
import ua.com.hedgehogsoft.bacreports.view.OutcomingsFrame;

public class OutcomingActionListener implements ActionListener
{
   public MainFrame mainFrame = null;

   public OutcomingActionListener(MainFrame mainFrame)
   {
      this.mainFrame = mainFrame;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      new OutcomingsFrame(mainFrame);
   }
}
