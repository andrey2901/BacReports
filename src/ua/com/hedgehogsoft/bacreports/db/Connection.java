package ua.com.hedgehogsoft.bacreports.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.model.Product;

public class Connection
{
   private String protocol = "jdbc:derby:";
   private String dbName = "derbyDB";
   private Properties props = null;
   private static final Logger logger = Logger.getLogger(Connection.class);

   public Connection()
   {
      props = new Properties();

      props.put("user", "user1");

      props.put("password", "user1");
   }

   public void createDB()
   {
      java.sql.Connection conn = null;

      Statement storeStatement = null;

      Statement incomingStatement = null;

      Statement outcomingStatement = null;

      try
      {
         conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);

         logger.info("Created database " + dbName);

         conn.setAutoCommit(false);

         storeStatement = conn.createStatement();

         incomingStatement = conn.createStatement();

         outcomingStatement = conn.createStatement();

         // We create a table...
         storeStatement.execute(
               "CREATE TABLE store(name varchar(40) not null, price double not null, amount double not null, CONSTRAINT productID PRIMARY KEY (name, price))");

         incomingStatement.execute(
               "CREATE TABLE incomings(name varchar(40) not null, price double not null, amount double not null, incoming_date timestamp not null, CONSTRAINT incomingID PRIMARY KEY (name, price, incoming_date))");

         outcomingStatement.execute(
               "CREATE TABLE outcomings(name varchar(40) not null, price double not null, amount double not null, outcoming_date timestamp not null, CONSTRAINT outcomingID PRIMARY KEY (name, price, outcoming_date))");

         conn.commit();

         logger.info("Created table store");

         logger.info("Created table incomings");

         logger.info("Created table outcomings");
      }
      catch (SQLException e)
      {
         printSQLException(e);
      }
      finally
      {
         try
         {
            closeStatements(storeStatement, incomingStatement, outcomingStatement);

            closeConnection(conn);
         }
         catch (SQLException e)
         {
            printSQLException(e);
         }

         shutdownDB();
      }
   }

   public void dropTables()
   {
      java.sql.Connection conn = null;

      Statement storeStatement = null;

      Statement incomingStatement = null;

      Statement outcomingStatement = null;

      Statement dropStoreStatement = null;

      Statement dropIncomingStatement = null;

      Statement dropOutcomingStatement = null;

      try
      {
         conn = DriverManager.getConnection(protocol + dbName, props);

         logger.info("Connected to database " + dbName);

         conn.setAutoCommit(false);

         storeStatement = conn.createStatement();

         incomingStatement = conn.createStatement();

         outcomingStatement = conn.createStatement();

         dropStoreStatement = conn.createStatement();

         dropIncomingStatement = conn.createStatement();

         dropOutcomingStatement = conn.createStatement();

         // delete the table
         storeStatement.execute("ALTER TABLE store DROP CONSTRAINT productID");

         dropStoreStatement.execute("DROP TABLE store");

         incomingStatement.execute("ALTER TABLE incomings DROP CONSTRAINT incomingID");

         dropIncomingStatement.execute("DROP TABLE incomings");

         outcomingStatement.execute("ALTER TABLE outcomings DROP CONSTRAINT outcomingID");

         dropOutcomingStatement.execute("DROP TABLE outcomings");

         conn.commit();

         logger.info("Dropped table store");

         logger.info("Dropped table incomings");

         logger.info("Dropped table outcomings");
      }
      catch (SQLException e)
      {
         printSQLException(e);
      }
      finally
      {
         try
         {
            closeStatements(storeStatement, incomingStatement, outcomingStatement, dropStoreStatement,
                  dropIncomingStatement, dropOutcomingStatement);

            closeConnection(conn);
         }
         catch (SQLException e)
         {
            printSQLException(e);
         }

         shutdownDB();
      }
   }

   public static void shutdownDB()
   {
      try
      {
         DriverManager.getConnection("jdbc:derby:;shutdown=true");
         // DriverManager.getConnection("jdbc:derby:" + dbName +
         // ";shutdown=true");
      }
      catch (SQLException se)
      {
         if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState()))))
         {
            // we got the expected exception
            logger.info("Derby shut down normally");
            // Note that for single database shutdown, the expected
            // SQL state is "08006", and the error code is 45000.
         }
         else
         {
            // if the error code or SQLState is different, we have
            // an unexpected exception (shutdown failed)
            logger.error("Derby did not shut down normally");
            printSQLException(se);
         }
      }
   }

   public List<Product> getStore()
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Product> result = new ArrayList<Product>();

      try
      {
         conn = DriverManager.getConnection(protocol + dbName, props);

         logger.info("Connected to database " + dbName);

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT * FROM store");

         while (rs.next())
         {
            Product product = new Product();

            product.setName(rs.getString("name"));

            product.setPrice(rs.getDouble("price"));

            product.setAmount(rs.getDouble("amount"));

            result.add(product);
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         printSQLException(e);
      }
      finally
      {
         try
         {
            closeStatements(s);

            closeConnection(conn);
         }
         catch (SQLException e)
         {
            printSQLException(e);
         }
      }
      return result;
   }

   public List<String> getUniqueProductNames()
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<String> result = new ArrayList<String>();

      try
      {
         conn = DriverManager.getConnection(protocol + dbName, props);

         logger.info("Connected to database " + dbName);

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT DISTINCT name FROM store");

         while (rs.next())
         {
            result.add(rs.getString("name"));
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         printSQLException(e);
      }
      finally
      {
         try
         {
            closeStatements(s);

            closeConnection(conn);
         }
         catch (SQLException e)
         {
            printSQLException(e);
         }
      }
      return result;
   }

   public void connect()
   {
      logger.info("Connection is starting...");
      java.sql.Connection conn = null;
      ArrayList<Statement> statements = new ArrayList<Statement>();
      PreparedStatement psInsert;
      PreparedStatement psUpdate;
      Statement s;
      ResultSet rs = null;
      try
      {
         /*
          * This connection specifies create=true in the connection URL to cause
          * the database to be created when connecting for the first time. To
          * remove the database, remove the directory derbyDB (the same as the
          * database name) and its contents.
          *
          * The directory derbyDB will be created under the directory that the
          * system property derby.system.home points to, or the current
          * directory (user.dir) if derby.system.home is not set.
          */
         conn = DriverManager.getConnection(protocol + dbName, props);

         logger.info("Connected to database " + dbName);

         // We want to control transactions manually. Autocommit is on by
         // default in JDBC.
         conn.setAutoCommit(false);

         s = conn.createStatement();

         statements.add(s);

         /*
          * It is recommended to use PreparedStatements when you are repeating
          * execution of an SQL statement. PreparedStatements also allows you to
          * parameterize variables. By using PreparedStatements you may increase
          * performance (because the Derby engine does not have to recompile the
          * SQL statement each time it is executed) and improve security
          * (because of Java type checking).
          */
         // parameter 1 is num (int), parameter 2 is addr (varchar)
         psInsert = conn.prepareStatement("insert into location values (?, ?)");
         statements.add(psInsert);

         psInsert.setInt(1, 1956);
         psInsert.setString(2, "Webster St.");
         psInsert.executeUpdate();
         logger.info("Inserted 1956 Webster");

         psInsert.setInt(1, 1910);
         psInsert.setString(2, "Union St.");
         psInsert.executeUpdate();
         logger.info("Inserted 1910 Union");

         // Let's update some rows as well...

         // parameter 1 and 3 are num (int), parameter 2 is addr (varchar)
         psUpdate = conn.prepareStatement("update location set num=?, addr=? where num=?");
         statements.add(psUpdate);

         psUpdate.setInt(1, 180);
         psUpdate.setString(2, "Grand Ave.");
         psUpdate.setInt(3, 1956);
         psUpdate.executeUpdate();
         logger.info("Updated 1956 Webster to 180 Grand");

         psUpdate.setInt(1, 300);
         psUpdate.setString(2, "Lakeshore Ave.");
         psUpdate.setInt(3, 180);
         psUpdate.executeUpdate();
         logger.info("Updated 180 Grand to 300 Lakeshore");

         /*
          * We select the rows and verify the results.
          */
         rs = s.executeQuery("SELECT num, addr FROM location ORDER BY num");

         /*
          * we expect the first returned column to be an integer (num), and
          * second to be a String (addr). Rows are sorted by street number
          * (num).
          *
          * Normally, it is best to use a pattern of while(rs.next()) { // do
          * something with the result set } to process all returned rows, but we
          * are only expecting two rows this time, and want the verification
          * code to be easy to comprehend, so we use a different pattern.
          */

         int number; // street number retrieved from the database
         boolean failure = false;
         if (!rs.next())
         {
            failure = true;
            reportFailure("No rows in ResultSet");
         }

         if ((number = rs.getInt(1)) != 300)
         {
            failure = true;
            reportFailure("Wrong row returned, expected num=300, got " + number);
         }

         if (!rs.next())
         {
            failure = true;
            reportFailure("Too few rows");
         }

         if ((number = rs.getInt(1)) != 1910)
         {
            failure = true;
            reportFailure("Wrong row returned, expected num=1910, got " + number);
         }

         if (rs.next())
         {
            failure = true;
            reportFailure("Too many rows");
         }

         if (!failure)
         {
            logger.info("Verified the rows");
         }

         /*
          * We commit the transaction. Any changes will be persisted to the
          * database now.
          */
         conn.commit();
         logger.info("Committed the transaction");

         /*
          * In embedded mode, an application should shut down the database. If
          * the application fails to shut down the database, Derby will not
          * perform a checkpoint when the JVM shuts down. This means that it
          * will take longer to boot (connect to) the database the next time,
          * because Derby needs to perform a recovery operation.
          *
          * It is also possible to shut down the Derby system/engine, which
          * automatically shuts down all booted databases.
          *
          * Explicitly shutting down the database or the Derby engine with the
          * connection URL is preferred. This style of shutdown will always
          * throw an SQLException.
          *
          * Not shutting down when in a client environment, see method Javadoc.
          */

         shutdownDB();
      }
      catch (SQLException sqle)
      {
         printSQLException(sqle);
      }
      finally
      {
         // release all open resources to avoid unnecessary memory usage

         // ResultSet
         try
         {
            if (rs != null)
            {
               rs.close();
               rs = null;
            }
         }
         catch (SQLException sqle)
         {
            printSQLException(sqle);
         }

         // Statements and PreparedStatements
         int i = 0;
         while (!statements.isEmpty())
         {
            // PreparedStatement extend Statement
            Statement st = (Statement) statements.remove(i);
            try
            {
               if (st != null)
               {
                  st.close();
                  st = null;
               }
            }
            catch (SQLException sqle)
            {
               printSQLException(sqle);
            }
         }

         // Connection
         try
         {
            if (conn != null)
            {
               conn.close();
               conn = null;
            }
         }
         catch (SQLException sqle)
         {
            printSQLException(sqle);
         }
      }
   }

   /**
    * Reports a data verification failure to System.err with the given message.
    *
    * @param message
    *           A message describing what failed.
    */
   private void reportFailure(String message)
   {
      logger.error("\nData verification failed:");
      logger.error('\t' + message);
   }

   /**
    * Prints details of an SQLException chain to <code>System.err</code>.
    * Details included are SQL State, Error code, Exception message.
    *
    * @param e
    *           the SQLException from which to print details.
    */
   private static void printSQLException(SQLException e)
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

   private void closeStatements(Statement... storeStatement) throws SQLException
   {
      if (storeStatement != null)
         for (int i = 0; i < storeStatement.length; i++)
         {
            if (storeStatement[i] != null)
            {
               storeStatement[i].close();

               storeStatement[i] = null;
            }
         }
   }

   private void closeConnection(java.sql.Connection conn) throws SQLException
   {
      if (conn != null)
      {
         conn.close();

         conn = null;

         logger.info("Connection was closed successfully.");
      }
   }
}
