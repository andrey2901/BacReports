package ua.com.hedgehogsoft.bacreports;

import ua.com.hedgehogsoft.bacreports.db.Connection;

public class BacReports
{
   public static void main(String[] args)
   {
      System.out.println("BacReports is starting ...");

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

      System.out.println("BacReports was finished.");
   }
}
