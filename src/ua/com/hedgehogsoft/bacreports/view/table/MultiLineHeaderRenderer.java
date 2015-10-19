package ua.com.hedgehogsoft.bacreports.view.table;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("rawtypes")
public class MultiLineHeaderRenderer extends JList implements TableCellRenderer
{
   private static final long serialVersionUID = 1L;

   @SuppressWarnings("unchecked")
   public MultiLineHeaderRenderer()
   {
      setOpaque(true);

      setForeground(UIManager.getColor("TableHeader.foreground"));

      setBackground(UIManager.getColor("TableHeader.background"));

      setBorder(UIManager.getBorder("TableHeader.cellBorder"));

      ListCellRenderer renderer = getCellRenderer();

      ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);

      setCellRenderer(renderer);
   }

   @SuppressWarnings("unchecked")
   public Component getTableCellRendererComponent(JTable table,
                                                  Object value,
                                                  boolean isSelected,
                                                  boolean hasFocus,
                                                  int row,
                                                  int column)
   {
      setFont(table.getFont());

      String str = (value == null) ? "" : value.toString();

      BufferedReader br = new BufferedReader(new StringReader(str));

      String line;

      Vector v = new Vector();

      try
      {
         while ((line = br.readLine()) != null)
         {
            v.addElement(line);
         }
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
      }
      setListData(v);

      return this;
   }
}