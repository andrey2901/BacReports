package ua.com.hedgehogsoft.bacreports.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DbConnection
{
   private static final Logger logger = Logger.getLogger(DbConnection.class);
   private static String protocol = "jdbc:derby:";
   private static String dbName = "derbyDB";
   private static Properties props = null;

   static
   {
      props = new Properties();

      props.put("user", "user1");

      props.put("password", "user1");
   }

   public static Connection getConnection()
   {
      Connection conn = null;
      try
      {
         conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);

         logger.trace("Connected database " + dbName);
      }
      catch (SQLException e)
      {
         printSQLException(e);
      }
      return conn;
   }

   /**
    * Prints details of an SQLException chain to <code>System.err</code>.
    * Details included are SQL State, Error code, Exception message.
    *
    * @param e
    *           the SQLException from which to print details.
    */
   public static void printSQLException(SQLException e)
   {
      // Unwraps the entire exception chain to unveil the real cause of the
      // Exception.
      while (e != null)
      {
         logger.error("\n----- SQLException -----");
         logger.error("  SQL State:  " + e.getSQLState());
         logger.error("  Error Code: " + e.getErrorCode());
         logger.error("  Message:    " + e.getMessage());
         // for stack traces, refer to derby.log or uncomment this:
         // e.printStackTrace(System.err);
         e = e.getNextException();
      }
   }

   public static void closeConnection(Connection conn) throws SQLException
   {
      if (conn != null)
      {
         conn.close();

         conn = null;

         logger.trace("Connection was closed successfully.");
      }
   }

   public static void closeStatements(Statement... storeStatement) throws SQLException
   {
      if (storeStatement != null)
      {
         for (int i = 0; i < storeStatement.length; i++)
         {
            if (storeStatement[i] != null)
            {
               storeStatement[i].close();

               storeStatement[i] = null;
            }
         }
         logger.trace("All  statements were closed successfully.");
      }
   }
}
