package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
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

public class OutcomingsFrame
{
   private JButton closeButton = null;
   private JButton outcomingButton = null;
   private JDatePickerImpl datePicker = null;
   private JComboBox<String> outcomingNameComboBox = null;
   private JComboBox<String> outcomingCostComboBox = null;
   private JTextField outcomingAmountTextField = null;
   private JComboBox<String> outcomingSourceComboBox = null;
   private JComboBox<String> outcomingUnitComboBox = null;
   private Units units = null;
   private static final Logger logger = Logger.getLogger(OutcomingsFrame.class);

   public OutcomingsFrame(MainFrame mainFrame)
   {
      units = new Units(new Connection().getUnits());

      JFrame outcomingsFrame = new JFrame("БакЗвіт - списання");

      outcomingsFrame.pack();

      outcomingsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("OutcomingsFrame was closed.");

            outcomingsFrame.dispose();
         }
      });

      closeButton = new JButton("Закрити");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            outcomingsFrame.dispose();

            logger.info("OutcomingsFrame was closed.");
         }
      });

      outcomingButton = new JButton("Списати");

      outcomingButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (checkInputData())
            {
               Product product = new Product();

               product.setName((String) outcomingNameComboBox.getSelectedItem());

               product.setPrice(Double.valueOf(((String) outcomingCostComboBox.getSelectedItem()).replace(",", ".")));

               product.setAmount(Double.valueOf(outcomingAmountTextField.getText().replace(",", ".")));

               product.setSource(mainFrame.getSources().indexOf((String) outcomingSourceComboBox.getSelectedItem()));

               product.setUnit(units.indexOf((String) outcomingUnitComboBox.getSelectedItem()));

               Product existedProduct = new Connection().getProductByNameAndPriceAndSourceAndUnit(product.getName(),
                     product.getPrice(), product.getSource(), product.getUnit());

               if (existedProduct.getAmount() >= product.getAmount())
               {
                  existedProduct.setAmount(existedProduct.getAmount() - product.getAmount());

                  new Connection().updateProduct(existedProduct);

                  ProductStoreTableModel model = (ProductStoreTableModel) mainFrame.getTable().getModel();

                  model.updateAmount(existedProduct);

                  new Connection().addOutcoming(product,
                        datePicker.getJFormattedTextField().getText());

                  logger.info("Outcomings were performed.");

                  outcomingsFrame.dispose();

                  logger.info("OutcomingsFrame was closed.");
               }
               else
               {
                  JOptionPane.showMessageDialog(null,
                        "Вы пытаетесь списать большее количество товара, чем имеется на складе.\n Укажите верное значение.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
               }
            }
         }
      });

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(outcomingButton);

      buttonsPanel.add(closeButton);

      /*--------------------------------------------------------------*/

      datePicker = DatePicker.getDatePicker();

      /*--------------------------------------------------------------*/
      JPanel outcomingPanel = new JPanel(new GridBagLayout());

      outcomingSourceComboBox = new JComboBox<String>();

      for (Source source : mainFrame.getSources().values())
      {
         outcomingSourceComboBox.addItem(source.getName());
      }

      outcomingPanel.add(new JLabel("Група даних:"), position(0, 0));

      outcomingPanel.add(outcomingSourceComboBox, position(1, 0));

      outcomingPanel.add(new JLabel("Найменування засобу:"), position(0, 1));

      outcomingNameComboBox = new JComboBox<String>();

      outcomingPanel.add(outcomingNameComboBox, position(1, 1));

      outcomingPanel.add(new JLabel("Одиниця виміру:"), position(0, 2));

      outcomingUnitComboBox = new JComboBox<String>();

      outcomingPanel.add(outcomingUnitComboBox, position(1, 2));

      outcomingPanel.add(new JLabel("Ціна, грн./од.:"), position(0, 3));

      outcomingCostComboBox = new JComboBox<String>();

      outcomingPanel.add(outcomingCostComboBox, position(1, 3));

      outcomingPanel.add(new JLabel("Кількість, од.:"), position(0, 4));

      outcomingAmountTextField = new JTextField();

      outcomingPanel.add(outcomingAmountTextField, position(1, 4));

      outcomingPanel.add(new JLabel("Дата:"), position(0, 5));

      outcomingPanel.add(datePicker, position(1, 5));

      outcomingCostComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (outcomingCostComboBox.getItemCount() != 0)
            {
               outcomingAmountTextField
                     .setText(
                           Double.toString(
                                 new Connection().getAmountByProductNameAndPriceAndSource(
                                       (String) outcomingNameComboBox.getSelectedItem(), Double
                                             .valueOf((String) outcomingCostComboBox.getSelectedItem()),
                                 mainFrame.getSources().indexOf((String) outcomingSourceComboBox.getSelectedItem()))));
            }
         }
      });

      outcomingUnitComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            outcomingCostComboBox.removeAllItems();

            List<Double> prices = new Connection().getPricesByProductAndSourceAndUnit(
                  (String) outcomingNameComboBox.getSelectedItem(),
                  mainFrame.getSources().indexOf((String) outcomingSourceComboBox.getSelectedItem()),
                  units.indexOf((String) outcomingUnitComboBox.getSelectedItem()));

            if (!prices.isEmpty())
            {
               Collections.sort(prices);
               for (Double price : prices)
               {
                  outcomingCostComboBox.addItem(Double.toString(price));
               }
            }
         }
      });

      outcomingNameComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            List<String> unitNames = new Connection().getUniqueUnitNamesByProductNameAndSourceName(
                  (String) outcomingNameComboBox.getSelectedItem(),
                  mainFrame.getSources().indexOf((String) outcomingSourceComboBox.getSelectedItem()));

            outcomingUnitComboBox.removeAllItems();

            for (String unit : unitNames)
            {
               outcomingUnitComboBox.addItem(unit);
            }
         }
      });

      outcomingSourceComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            outcomingNameComboBox.removeAllItems();

            List<String> productNames = new Connection().getUniqueProductNamesBySource(
                  mainFrame.getSources().indexOf((String) outcomingSourceComboBox.getSelectedItem()));

            if (!productNames.isEmpty())
            {
               Collections.sort(productNames);

               for (String name : productNames)
               {
                  outcomingNameComboBox.addItem(name);
               }
            }
         }
      });

      outcomingSourceComboBox.setSelectedIndex(0);

      outcomingsFrame.add(outcomingPanel, BorderLayout.CENTER);

      outcomingsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      outcomingsFrame.pack();

      outcomingsFrame.setSize(700, 225);

      outcomingsFrame.setMinimumSize(new Dimension(375, 225));

      outcomingsFrame.setResizable(true);

      outcomingsFrame.setLocationRelativeTo(null);

      outcomingsFrame.setVisible(true);

      logger.info("OutcomingsFrame was started.");
   }

   private boolean checkInputData()
   {
      boolean result = true;

      if (outcomingNameComboBox.getSelectedItem() == null
            || ((String) outcomingNameComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле найменування товару", "Помилка",
               JOptionPane.ERROR_MESSAGE);

         result = false;
      }

      if (outcomingUnitComboBox.getSelectedItem() == null
            || ((String) outcomingUnitComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле одиниць виміру", "Помилка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }

      if (outcomingCostComboBox.getSelectedItem() == null
            || ((String) outcomingCostComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заповніть поле вартості", "Помилка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (outcomingAmountTextField.getText() == null || outcomingAmountTextField.getText().isEmpty())
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
