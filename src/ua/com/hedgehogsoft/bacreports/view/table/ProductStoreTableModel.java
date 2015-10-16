package ua.com.hedgehogsoft.bacreports.view.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ua.com.hedgehogsoft.bacreports.model.Product;

public class ProductStoreTableModel extends AbstractTableModel
{
   private static final long serialVersionUID = 1L;
   private List<Product> products = null;
   private List<Object[]> rows = null;
   private String[] columnNames = null;

   public ProductStoreTableModel(List<Product> products, String[] columnNames)
   {
      this.products = products;

      rows = new ArrayList<>(products.size());

      this.columnNames = columnNames;
   }

   @Override
   public int getRowCount()
   {
      return rows.size();
   }

   @Override
   public int getColumnCount()
   {
      return columnNames.length;
   }

   @Override
   public Object getValueAt(int rowIndex, int columnIndex)
   {
      Object[] row = rows.get(rowIndex);

      return row[columnIndex];
   }

   public void setColumnName(int i, String name)
   {
      columnNames[i] = name;

      fireTableStructureChanged();
   }

   @Override
   public String getColumnName(int col)
   {
      return columnNames[col];
   }

   public void addRow(Object[] row)
   {
      int rowCount = getRowCount();

      rows.add(row);

      fireTableRowsInserted(rowCount, rowCount);
   }

   public void addProduct(Product product)
   {
      Object[] row = new Object[] {this.getRowCount() + 1,
                                   product.getName(),
                                   product.getPrice(),
                                   product.getAmount(),
                                   product.getTotalPrice(),
                                   product.getSource().getName()};
      int rowCount = getRowCount();

      rows.add(row);

      products.add(product);

      fireTableRowsInserted(rowCount, rowCount);
   }

   public void updateAmount(Product product)
   {
      int rowIndex = 0;

      for (int i = 0; i < products.size(); i++)
      {
         if (product.equals(products.get(i)))
         {
            rowIndex = i;

            break;
         }
      }

      Object[] row = rows.get(rowIndex);

      row[3] = product.getAmount();

      row[4] = product.getTotalPrice();

      fireTableCellUpdated(rowIndex, 3);

      fireTableCellUpdated(rowIndex, 4);
   }

   @Override
   public void setValueAt(Object value, int row, int col)
   {
      fireTableCellUpdated(row, col);
   }
}
