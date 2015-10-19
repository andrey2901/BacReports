package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePickerImpl;

import ua.com.hedgehogsoft.bacreports.commons.Units;
import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.model.Source;
import ua.com.hedgehogsoft.bacreports.view.date.DatePicker;
import ua.com.hedgehogsoft.bacreports.view.table.ProductStoreTableModel;

public class IncomingsFrame
{
   private JButton closeButton = null;
   private JButton incomingButton = null;
   private JDatePickerImpl datePicker = null;
   private JComboBox<String> incomingNameComboBox = null;
   private JComboBox<String> incomingCostComboBox = null;
   private JTextField incomingAmountTextField = null;
   private JComboBox<String> incomingSourceComboBox = null;
   private JComboBox<String> incomingUnitComboBox = null;
   private Units units = null;
   private static final Logger logger = Logger.getLogger(IncomingsFrame.class);

   public IncomingsFrame(MainFrame mainFrame)
   {
      units = new Units(new Connection().getUnits());

      JFrame incomingsFrame = new JFrame("БакЗвіт - надходження");

      incomingsFrame.pack();

      incomingsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("IncomingsFrame was closed.");

            incomingsFrame.dispose();
         }
      });

      closeButton = new JButton("Закрити");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            incomingsFrame.dispose();

            logger.info("IncomingsFrame was closed.");
         }
      });

      incomingButton = new JButton("Оприбуткувати");

      incomingButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (checkInputData())
            {
               if (!new Connection().unitExist((String) incomingUnitComboBox.getSelectedItem()))
               {
                  new Connection().addUnit((String) incomingUnitComboBox.getSelectedItem());

                  units = new Units(new Connection().getUnits());
               }

               Product product = new Product();

               product.setName((String) incomingNameComboBox.getSelectedItem());

               product.setPrice(Double.valueOf(((String) incomingCostComboBox.getSelectedItem()).replace(",", ".")));

               product.setAmount(Double.valueOf(incomingAmountTextField.getText().replace(",", ".")));

               product.setSource(mainFrame.getSources().indexOf((String) incomingSourceComboBox.getSelectedItem()));

               product.setUnit(units.indexOf((String) incomingUnitComboBox.getSelectedItem()));

               if (new Connection().productExist(product.getName(), product.getPrice(), product.getSource(),
                     product.getUnit()))
               {
                  Product existedProduct = new Connection().getProductByNameAndPriceAndSourceAndUnit(product.getName(),
                        product.getPrice(), product.getSource(), product.getUnit());

                  existedProduct.setAmount(product.getAmount() + existedProduct.getAmount());

                  new Connection().updateProduct(existedProduct);

                  ProductStoreTableModel model = (ProductStoreTableModel) mainFrame.getTable().getModel();

                  model.updateAmount(existedProduct);
               }
               else
               {
                  new Connection().addProductToStore(product);

                  ProductStoreTableModel model = (ProductStoreTableModel) mainFrame.getTable().getModel();

                  model.addProduct(product);
               }

               new Connection().addIncoming(product,
                     datePicker.getJFormattedTextField().getText());

               logger.info("Incomings were performed.");

               incomingsFrame.dispose();

               logger.info("IncomingsFrame was closed.");
            }
         }
      });

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(incomingButton);

      buttonsPanel.add(closeButton);

      /*--------------------------------------------------------------*/

      datePicker = DatePicker.getDatePicker();

      /*--------------------------------------------------------------*/
      JPanel incomingPanel = new JPanel(new GridBagLayout());

      incomingPanel.add(new JLabel("Найменування засобу:"), position(0, 0));

      incomingNameComboBox = new JComboBox<String>();

      incomingNameComboBox.setEditable(true);

      List<String> names = new Connection().getUniqueProductNames();

      if (!names.isEmpty())
      {
         for (String name : names)
         {
            incomingNameComboBox.addItem(name);
         }
      }

      incomingNameComboBox.setSelectedItem("");

      incomingPanel.add(incomingNameComboBox, position(1, 0));

      incomingPanel.add(new JLabel("Одиниця виміру:"), position(0, 1));

      incomingUnitComboBox = new JComboBox<String>();

      incomingUnitComboBox.setEditable(true);

      incomingPanel.add(incomingUnitComboBox, position(1, 1));

      for (String unit : new Connection().getUniqueUnitNames())

      {
         incomingUnitComboBox.addItem(unit);
      }

      incomingUnitComboBox.setSelectedItem("");

      incomingPanel.add(new JLabel("Ціна, грн./од.:"), position(0, 2));

      incomingCostComboBox = new JComboBox<String>();

      incomingCostComboBox.setEditable(true);

      incomingPanel.add(incomingCostComboBox, position(1, 2));

      incomingPanel.add(new JLabel("Кількість, од.:"), position(0, 3));

      incomingAmountTextField = new JTextField();

      incomingPanel.add(incomingAmountTextField, position(1, 3));

      incomingSourceComboBox = new JComboBox<String>();

      for (Source source : mainFrame.getSources().values())
      {
         incomingSourceComboBox.addItem(source.getName());
      }

      incomingPanel.add(new JLabel("Група даних:"), position(0, 4));

      incomingPanel.add(incomingSourceComboBox, position(1, 4));

      incomingPanel.add(new JLabel("Дата:"), position(0, 5));

      incomingPanel.add(datePicker, position(1, 5));

      incomingUnitComboBox.addActionListener(new ActionListener()
      {

         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (!(((String) incomingNameComboBox.getSelectedItem()) == null
                  || ((String) incomingNameComboBox.getSelectedItem()).isEmpty()))
            {
               incomingCostComboBox.removeAllItems();

               List<Double> prices = new Connection().getPricesByProductAndUnit(
                     (String) incomingNameComboBox.getSelectedItem(),
                     units.indexOf((String) incomingUnitComboBox.getSelectedItem()));

               if (!prices.isEmpty())
               {
                  for (Double price : prices)
                  {
                     incomingCostComboBox.addItem(Double.toString(price));
                  }
               }
            }
         }

      });
      incomingNameComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            List<String> unitNamesForProduct = new Connection()
                  .getUniqueUnitNamesByProductName((String) incomingNameComboBox.getSelectedItem());

            List<String> unitsNames = new Connection().getUniqueUnitNames();

            unitsNames.removeAll(unitNamesForProduct);

            incomingUnitComboBox.removeAllItems();

            for (String unit : unitNamesForProduct)
            {
               incomingUnitComboBox.addItem(unit);
            }

            for (String unit : unitsNames)
            {
               incomingUnitComboBox.addItem(unit);
            }
         }
      });

      incomingsFrame.add(incomingPanel, BorderLayout.CENTER);

      incomingsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      incomingsFrame.pack();

      incomingsFrame.setSize(700, 225);

      incomingsFrame.setMinimumSize(new Dimension(375, 225));

      incomingsFrame.setResizable(true);

      incomingsFrame.setLocationRelativeTo(null);

      incomingsFrame.setVisible(true);

      logger.info("IncomingsFrame was started.");
   }

   private boolean checkInputData()
   {
      boolean result = true;

      if (incomingNameComboBox.getSelectedItem() == null || ((String) incomingNameComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле найменування товару", "Помилка",
               JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (incomingUnitComboBox.getSelectedItem() == null || ((String) incomingUnitComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле одиниць виміру", "Помилка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (incomingCostComboBox.getSelectedItem() == null || ((String) incomingCostComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле вартості", "Помилка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (incomingAmountTextField.getText() == null || incomingAmountTextField.getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле кількості", "Помилка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (datePicker.getJFormattedTextField().getText() == null
            || datePicker.getJFormattedTextField().getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле дати", "Помилка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }

      return result;
   }

   private GridBagConstraints position(int x, int y)
   {
      GridBagConstraints c = new GridBagConstraints();

      c.fill = GridBagConstraints.HORIZONTAL;

      switch (x)
      {
         case 0:

            c.gridx = 0;
            c.weightx = 0;
            c.gridwidth = 1;

            break;

         case 1:

            c.gridx = 1;
            c.weightx = 10;
            c.gridwidth = 3;

            break;
      }

      c.gridy = y;

      return c;
   }
}
