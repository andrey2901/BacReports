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

      Statement unitsTableStatement = null;

      Statement insertUnitsTableStatement = null;

      Statement sourceTableStatement = null;

      Statement insertSourceTableStatement = null;

      Statement storeTableStatement = null;

      Statement incomingTableStatement = null;

      Statement outcomingTableStatement = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         unitsTableStatement = conn.createStatement();

         insertUnitsTableStatement = conn.createStatement();

         sourceTableStatement = conn.createStatement();

         insertSourceTableStatement = conn.createStatement();

         storeTableStatement = conn.createStatement();

         incomingTableStatement = conn.createStatement();

         outcomingTableStatement = conn.createStatement();

         unitsTableStatement.execute(
               "CREATE TABLE units(id int not null generated always as identity (start with 1, increment by 1), unit varchar(100) not null, PRIMARY KEY (id), UNIQUE (unit))");

         logger.info("Created table units");

         insertUnitsTableStatement.execute(
               "INSERT INTO units(unit) VALUES ('амп'), ('флак'), ('шт'), ('гр'), ('кг'), ('набір'), ('уп'), ('кор'), ('пар')");

         logger.info("Filled table units by default values");

         sourceTableStatement.execute(
               "CREATE TABLE source_group(id int not null generated always as identity (start with 1, increment by 1), source varchar(100) not null, PRIMARY KEY (id), UNIQUE (source))");

         logger.info("Created table sources");

         insertSourceTableStatement.execute(
               "INSERT INTO source_group(source) VALUES ('Реактиви, поживні середовища'), ('Меценат'), ('Від провізора'), ('Від дезінфектора')");

         logger.info("Filled table sources by default values");

         storeTableStatement.execute(
               "CREATE TABLE store(id int not null generated always as identity (start with 1, increment by 1), name varchar(255) not null, price double not null, amount double not null, source_id int not null, unit_id int not null, PRIMARY KEY (id), UNIQUE (name, price, source_id, unit_id), FOREIGN KEY (source_id) REFERENCES source_group(id), FOREIGN KEY (unit_id) REFERENCES units(id))");

         logger.info("Created table store");

         incomingTableStatement.execute(
               "CREATE TABLE incomings(id int not null generated always as identity (start with 1, increment by 1), amount double not null, incoming_date date not null, product_id int not null, PRIMARY KEY (id), FOREIGN KEY (product_id) REFERENCES store(id))");

         logger.info("Created table incomings");

         outcomingTableStatement.execute(
               "CREATE TABLE outcomings(id int not null generated always as identity (start with 1, increment by 1), amount double not null, outcoming_date date not null, product_id int not null, PRIMARY KEY (id), FOREIGN KEY (product_id) REFERENCES store(id))");

         logger.info("Created table outcomings");

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
            DbConnection.closeStatements(storeTableStatement, incomingTableStatement, outcomingTableStatement);

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

      Statement dropIncomingTableStatement = null;

      Statement dropOutcomingTableStatement = null;

      Statement dropStoreTableStatement = null;

      Statement dropSourceTableStatement = null;

      Statement dropUnitsTableStatement = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         dropIncomingTableStatement = conn.createStatement();

         dropOutcomingTableStatement = conn.createStatement();

         dropStoreTableStatement = conn.createStatement();

         dropSourceTableStatement = conn.createStatement();

         dropUnitsTableStatement = conn.createStatement();

         dropIncomingTableStatement.execute("DROP TABLE incomings");

         logger.info("Dropped table incomings");

         dropOutcomingTableStatement.execute("DROP TABLE outcomings");

         logger.info("Dropped table outcomings");

         dropStoreTableStatement.execute("DROP TABLE store");

         logger.info("Dropped table store");

         dropSourceTableStatement.execute("DROP TABLE source_group");

         logger.info("Dropped table sources");

         dropUnitsTableStatement.execute("DROP TABLE units");

         logger.info("Dropped table units");

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
            DbConnection.closeStatements(dropIncomingTableStatement, dropOutcomingTableStatement,
                  dropStoreTableStatement, dropSourceTableStatement, dropUnitsTableStatement);

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

   /*
    * public List<Product> getStoreBySource(Source source) { java.sql.Connection
    * conn = null;
    * 
    * Statement s = null;
    * 
    * ResultSet rs = null;
    * 
    * List<Product> result = new ArrayList<Product>();
    * 
    * try { conn = DbConnection.getConnection();
    * 
    * conn.setAutoCommit(false);
    * 
    * s = conn.createStatement();
    * 
    * rs = s.executeQuery(
    * "SELECT name, price, amount, source_id FROM store WHERE source_id = " +
    * source.getId());
    * 
    * while (rs.next()) { Product product = new Product();
    * 
    * product.setName(rs.getString("name"));
    * 
    * product.setPrice(rs.getDouble("price"));
    * 
    * product.setAmount(rs.getDouble("amount"));
    * 
    * product.setSource(rs.getInt("source_id"));
    * 
    * result.add(product); }
    * 
    * conn.commit(); } catch (SQLException e) {
    * DbConnection.printSQLException(e); } finally { try {
    * DbConnection.closeStatements(s);
    * 
    * DbConnection.closeConnection(conn); } catch (SQLException e) {
    * DbConnection.printSQLException(e); } } return result; }
    */

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

   public List<String> getUniqueUnitNames()
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

         rs = s.executeQuery("SELECT DISTINCT unit FROM units");

         while (rs.next())
         {
            result.add(rs.getString("unit"));
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

   public List<String> getUniqueUnitNamesByProductName(String productName)
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

         rs = s.executeQuery(
               "SELECT DISTINCT unit FROM units LEFT JOIN store ON units.id = store.unit_id WHERE store.name='"
                     + productName + "'");

         while (rs.next())
         {
            result.add(rs.getString("unit"));
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

   public List<String> getUniqueUnitNamesByProductNameAndSourceName(String productName, int sourceID)
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

         rs = s.executeQuery(
               "SELECT DISTINCT unit FROM units LEFT JOIN store ON units.id = store.unit_id WHERE store.name='"
                     + productName + "' AND store.source_id=" + sourceID);

         while (rs.next())
         {
            result.add(rs.getString("unit"));
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

   public List<Double> getPricesByProductAndUnit(String productName, int unitID)
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

         rs = s.executeQuery("SELECT DISTINCT price FROM store WHERE name='" + productName + "' AND unit_id=" + unitID);

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

   public List<Double> getPricesByProductAndSourceAndUnit(String productName, int sourceID, int unitID)
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

         rs = s.executeQuery("SELECT DISTINCT price FROM store WHERE name='" + productName + "' AND source_id="
               + sourceID + " AND unit_id=" + unitID);

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

   public Product getProductByNameAndPriceAndSourceAndUnit(String productName,
                                                           double productPrice,
                                                           int sourceID,
                                                           int unitID)
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
               + " AND source_id = " + sourceID + " AND unit_id = " + unitID);

         while (rs.next())
         {
            result = new Product();

            result.setName(rs.getString("name"));

            result.setPrice(rs.getDouble("price"));

            result.setAmount(rs.getDouble("amount"));

            result.setSource(rs.getInt("source_id"));

            result.setUnit(rs.getInt("unit_id"));
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

         ps = conn.prepareStatement(
               "UPDATE store SET amount = ? WHERE name = ? AND price = ? AND source_id = ? AND unit_id = ?");

         ps.setDouble(1, product.getAmount());
         ps.setString(2, product.getName());
         ps.setDouble(3, product.getPrice());
         ps.setInt(4, product.getSource());
         ps.setInt(5, product.getUnit());
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

         ps = conn
               .prepareStatement("INSERT INTO store(name, price, amount, source_id, unit_id) VALUES (?, ?, ?, ?, ?)");

         ps.setString(1, product.getName());
         ps.setDouble(2, product.getPrice());
         ps.setDouble(3, product.getAmount());
         ps.setInt(4, product.getSource());
         ps.setInt(5, product.getUnit());
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

         ps = conn.prepareStatement("INSERT INTO incomings(amount, incoming_date, product_id) VALUES (?, ?, "
               + "(SELECT id FROM store WHERE name = ? AND price = ? AND source_id = ? AND unit_id= ?))");

         ps.setDouble(1, product.getAmount());
         ps.setString(2, date);
         ps.setString(3, product.getName());
         ps.setDouble(4, product.getPrice());
         ps.setInt(5, product.getSource());
         ps.setInt(6, product.getUnit());

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

   public boolean productExist(String productName, double productPrice, int sourceID, int unitID)
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
               + " AND source_id = " + sourceID + " AND unit_id = " + unitID);

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

   public boolean unitExist(String unitName)
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

         rs = s.executeQuery("SELECT COUNT(*) FROM units WHERE unit = '" + unitName + "'");

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

   public void addUnit(String unitName)
   {
      java.sql.Connection conn = null;

      PreparedStatement ps = null;

      try
      {
         conn = DbConnection.getConnection();

         conn.setAutoCommit(false);

         ps = conn.prepareStatement("INSERT INTO units(unit) VALUES (?)");

         ps.setString(1, unitName);

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
}
