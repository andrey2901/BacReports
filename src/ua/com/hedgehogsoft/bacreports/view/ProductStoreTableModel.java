package ua.com.hedgehogsoft.bacreports.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ua.com.hedgehogsoft.bacreports.model.Product;

public class ProductStoreTableModel extends AbstractTableModel
{
   private static final long serialVersionUID = 1L;
   private List<Object[]> rows = null;
   private String[] columnNames = null;

   public ProductStoreTableModel(List<Product> products, String[] columnNames)
   {
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
}
