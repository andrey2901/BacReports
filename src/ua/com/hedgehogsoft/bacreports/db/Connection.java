package ua.com.hedgehogsoft.bacreports.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import ua.com.hedgehogsoft.bacreports.model.Incoming;
import ua.com.hedgehogsoft.bacreports.model.Outcoming;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.model.Source;
import ua.com.hedgehogsoft.bacreports.model.Unit;

public class Connection
{
   private static final Logger logger = Logger.getLogger(Connection.class);

   public void createDB()
   {
      java.sql.Connection conn = null;

      Statement storeStatement = null;

      Statement incomingStatement = null;

      Statement outcomingStatement = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         storeStatement = conn.createStatement();

         incomingStatement = conn.createStatement();

         outcomingStatement = conn.createStatement();

         // We create a table...
         storeStatement.execute(
               "CREATE TABLE store(name varchar(40) not null, price double not null, amount double not null, CONSTRAINT productID PRIMARY KEY (name, price))");

         incomingStatement.execute(
               "CREATE TABLE incomings(name varchar(40) not null, price double not null, amount double not null, incoming_date date not null)");

         outcomingStatement.execute(
               "CREATE TABLE outcomings(name varchar(40) not null, price double not null, amount double not null, outcoming_date date not null)");

         conn.commit();

         logger.info("Created table store");

         logger.info("Created table incomings");

         logger.info("Created table outcomings");
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(storeStatement, incomingStatement, outcomingStatement);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }

         shutdownDB();
      }
   }

   public void dropTables()
   {
      java.sql.Connection conn = null;

      Statement storeStatement = null;

      Statement dropStoreStatement = null;

      Statement dropIncomingStatement = null;

      Statement dropOutcomingStatement = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         storeStatement = conn.createStatement();

         dropStoreStatement = conn.createStatement();

         dropIncomingStatement = conn.createStatement();

         dropOutcomingStatement = conn.createStatement();

         // delete the table
         storeStatement.execute("ALTER TABLE store DROP CONSTRAINT productID");

         dropStoreStatement.execute("DROP TABLE store");

         dropIncomingStatement.execute("DROP TABLE incomings");

         dropOutcomingStatement.execute("DROP TABLE outcomings");

         conn.commit();

         logger.info("Dropped table store");

         logger.info("Dropped table incomings");

         logger.info("Dropped table outcomings");
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(storeStatement, dropStoreStatement, dropIncomingStatement,
                  dropOutcomingStatement);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
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
            DbConnection.printSQLException(se);
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
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT name, price, amount, source_id, unit_id FROM store");

         while (rs.next())
         {
            Product product = new Product();

            product.setName(rs.getString("name"));

            product.setPrice(rs.getDouble("price"));

            product.setAmount(rs.getDouble("amount"));

            product.setSource(rs.getInt("source_id"));

            product.setUnit(rs.getInt("unit_id"));

            result.add(product);
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public List<Source> getSources()
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Source> result = new ArrayList<Source>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT  * FROM source_group");

         while (rs.next())
         {
            Source source = new Source();

            source.setId(rs.getInt("id"));

            source.setName(rs.getString("source"));

            result.add(source);
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public List<Unit> getUnits()
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Unit> result = new ArrayList<Unit>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT  * FROM units");

         while (rs.next())
         {
            Unit unit = new Unit();

            unit.setId(rs.getInt("id"));

            unit.setName(rs.getString("unit"));

            result.add(unit);
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   /*public List<Product> getStoreBySource(Source source)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Product> result = new ArrayList<Product>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT name, price, amount, source_id FROM store WHERE source_id = " + source.getId());

         while (rs.next())
         {
            Product product = new Product();

            product.setName(rs.getString("name"));

            product.setPrice(rs.getDouble("price"));

            product.setAmount(rs.getDouble("amount"));

            product.setSource(rs.getInt("source_id"));

            result.add(product);
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }*/

   public List<String> getUniqueProductNames()
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<String> result = new ArrayList<String>();

      try
      {
         conn = DbConnection.getConnection();

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
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public List<String> getUniqueProductNamesBySource(int sourceID)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<String> result = new ArrayList<String>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT DISTINCT name FROM store WHERE source_id=" + sourceID);

         while (rs.next())
         {
            result.add(rs.getString("name"));
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public List<Incoming> getIncomings(String dateFrom, String dateTo)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Incoming> result = new ArrayList<Incoming>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT * FROM incomings  WHERE incoming_date >= '" + dateFrom + "' AND incoming_date <= '"
               + dateTo + "'");

         while (rs.next())
         {
            Incoming incoming = new Incoming();

            Product product = new Product();

            product.setName(rs.getString("name"));

            product.setPrice(rs.getDouble("price"));

            product.setAmount(rs.getDouble("amount"));

            incoming.setProduct(product);

            incoming.setDate(rs.getDate("incoming_date"));

            result.add(incoming);
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public List<Outcoming> getOutcomings(String dateFrom, String dateTo)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Outcoming> result = new ArrayList<Outcoming>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT * FROM outcomings  WHERE outcoming_date >= '" + dateFrom
               + "' AND outcoming_date <= '" + dateTo + "'");

         while (rs.next())
         {
            Outcoming outcoming = new Outcoming();

            Product product = new Product();

            product.setName(rs.getString("name"));

            product.setPrice(rs.getDouble("price"));

            product.setAmount(rs.getDouble("amount"));

            outcoming.setProduct(product);

            outcoming.setDate(rs.getDate("outcoming_date"));

            result.add(outcoming);
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public List<Double> getPricesByProduct(String productName)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Double> result = new ArrayList<Double>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT DISTINCT price FROM store WHERE name='" + productName + "'");

         while (rs.next())
         {
            result.add(rs.getDouble("price"));
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public List<Double> getPricesByProductAndSource(String productName, int sourceID)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      List<Double> result = new ArrayList<Double>();

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery(
               "SELECT DISTINCT price FROM store WHERE name='" + productName + "' AND source_id=" + sourceID);

         while (rs.next())
         {
            result.add(rs.getDouble("price"));
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public Product getProductByNameAndPriceAndSource(String productName, double productPrice, int sourceID)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      Product result = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT * FROM store WHERE name='" + productName + "' AND price=" + productPrice
               + " AND source_id = " + sourceID);

         while (rs.next())
         {
            result = new Product();

            result.setName(rs.getString("name"));

            result.setPrice(rs.getDouble("price"));

            result.setAmount(rs.getDouble("amount"));

            result.setSource(rs.getInt("source_id"));

            break;
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public void updateProduct(Product product)
   {
      java.sql.Connection conn = null;

      PreparedStatement ps = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         ps = conn.prepareStatement("UPDATE store SET amount=? WHERE name=? AND price=? AND source_id=?");

         ps.setDouble(1, product.getAmount());
         ps.setString(2, product.getName());
         ps.setDouble(3, product.getPrice());
         ps.setInt(4, product.getSource());
         ps.executeUpdate();

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(ps);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
   }

   public void addProductToStore(Product product)
   {
      java.sql.Connection conn = null;

      PreparedStatement ps = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         ps = conn.prepareStatement("INSERT INTO store(name, price, amount, source_id) VALUES (?, ?, ?, ?)");

         ps.setString(1, product.getName());
         ps.setDouble(2, product.getPrice());
         ps.setDouble(3, product.getAmount());
         ps.setInt(4, product.getSource());
         ps.executeUpdate();

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(ps);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
   }

   public void addIncoming(Product product, String date)
   {
      java.sql.Connection conn = null;

      PreparedStatement ps = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         ps = conn.prepareStatement("INSERT INTO incomings(amount, incoming_date, product_id)"
               + " VALUES (?, ?, (SELECT id FROM store WHERE name = ? AND price = ? AND source_id = ?))");

         ps.setDouble(1, product.getAmount());
         ps.setString(2, date);
         ps.setString(3, product.getName());
         ps.setDouble(4, product.getPrice());
         ps.setInt(5, product.getSource());

         ps.executeUpdate();

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(ps);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
   }

   public void addOutcoming(Product product, String date)
   {
      java.sql.Connection conn = null;

      PreparedStatement ps = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         ps = conn.prepareStatement("INSERT INTO outcomings(amount, outcoming_date, product_id)"
               + " VALUES (?, ?, (SELECT id FROM store WHERE name = ? AND price = ? AND source_id = ?))");

         ps.setDouble(1, product.getAmount());
         ps.setString(2, date);
         ps.setString(3, product.getName());
         ps.setDouble(4, product.getPrice());
         ps.setInt(5, product.getSource());
         ps.executeUpdate();

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(ps);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
   }

   public Double getAmountByProductNameAndPriceAndSource(String productName, double productPrice, int sourceID)
   {
      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      double result = 0;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT amount FROM store WHERE name='" + productName + "' AND price=" + productPrice
               + "AND source_id=" + sourceID);

         while (rs.next())
         {
            result = rs.getDouble("amount");

            break;
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }

   public boolean productExist(String productName, double productPrice, int sourceID)
   {
      boolean result = false;

      java.sql.Connection conn = null;

      Statement s = null;

      ResultSet rs = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         s = conn.createStatement();

         rs = s.executeQuery("SELECT COUNT(*) FROM store WHERE name = '" + productName + "' AND price = " + productPrice
               + " AND source_id = " + sourceID);

         while (rs.next())
         {
            if (rs.getInt("1") != 0)
            {
               result = true;
            }
         }

         conn.commit();
      }
      catch (SQLException e)
      {
         DbConnection.printSQLException(e);
      }
      finally
      {
         try
         {
            DbConnection.closeStatements(s);

            DbConnection.closeConnection(conn);
         }
         catch (SQLException e)
         {
            DbConnection.printSQLException(e);
         }
      }
      return result;
   }
}
