package ua.com.hedgehogsoft.bacreports;

import java.awt.EventQueue;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.view.MainFrame;

public class BacReports
{
   private static final Logger logger = Logger.getLogger(BacReports.class);

   public static void main(String[] args)
   {
      logger.info("BacReports is starting ...");

      if (args.length > 0)
      {
         if (args[0].equalsIgnoreCase("--install") || args[0].equalsIgnoreCase("-i"))
         {
            new Connection().createDB();
         }
         if (args[0].equalsIgnoreCase("--drop") || args[0].equalsIgnoreCase("-d"))
         {
            new Connection().dropTables();
         }
      }
      else
      {
         // new Connection().connect();
         new BacReports().start();
      }
   }

   private void start()
   {
      EventQueue.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            new MainFrame();
         }
      });
   }
}
