package ua.com.hedgehogsoft.bacreports;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.db.Connection;

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
         new Connection().connect();
      }

      logger.info("BacReports was finished.");
   }
}
