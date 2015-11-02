package ua.com.hedgehogsoft.bacreports.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import org.apache.log4j.Logger;

public class DirectoryCopywriter
{
   private static final Logger logger = Logger.getLogger(DirectoryCopywriter.class);

   public void copy()
   {
      File srcFolder = new File(System.getProperty("derby.system.home") + "/derbyDB");
      File destFolder = new File(System.getProperty("derby.system.home") + "/backup/derbyDB"
            + new DateFormatter().valueToString(Calendar.getInstance()));

      if (!srcFolder.exists())
      {

         logger.error("Directory does not exist.");

         return;
      }
      else
      {

         try
         {
            copyFolder(srcFolder, destFolder);
         }
         catch (IOException e)
         {
            logger.error(e);

            return;
         }
      }

      logger.info("Done");
   }

   public static void copyFolder(File src, File dest) throws IOException
   {

      if (src.isDirectory())
      {
         if (!dest.exists())
         {
            dest.mkdir();
            logger.info("Directory copied from " + src + "  to " + dest);
         }

         String files[] = src.list();

         for (String file : files)
         {
            File srcFile = new File(src, file);
            File destFile = new File(dest, file);
            copyFolder(srcFile, destFile);
         }

      }
      else
      {
         InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(dest);

         byte[] buffer = new byte[1024];

         int length;

         while ((length = in.read(buffer)) > 0)
         {
            out.write(buffer, 0, length);
         }

         in.close();
         out.close();
         logger.info("File copied from " + src + " to " + dest);
      }
   }
}
