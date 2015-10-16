package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ua.com.hedgehogsoft.bacreports.db.Connection;
import ua.com.hedgehogsoft.bacreports.model.Product;
import ua.com.hedgehogsoft.bacreports.model.Source;
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
   private static final Logger logger = Logger.getLogger(OutcomingsFrame.class);

   public OutcomingsFrame(MainFrame mainFrame)
   {
      final JFrame outcomingsFrame = new JFrame("БакОтчеты - списание");

      outcomingsFrame.pack();

      outcomingsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("OutcomingsFrame was closed.");

            outcomingsFrame.dispose();
         }
      });

      closeButton = new JButton("Закрыть");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            outcomingsFrame.dispose();

            logger.info("OutcomingsFrame was closed.");
         }
      });

      outcomingButton = new JButton("Списать");

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

               Product existedProduct = new Connection().getProductByNameAndPriceAndSource(product.getName(),
                     product.getPrice(), product.getSource());

               if (existedProduct.getAmount() >= product.getAmount())
               {
                  existedProduct.setAmount(existedProduct.getAmount() - product.getAmount());

                  new Connection().updateProduct(existedProduct);

                  ProductStoreTableModel model = (ProductStoreTableModel) mainFrame.getTable().getModel();

                  model.updateAmount(existedProduct);

                  new Connection().addOutcoming(product, datePicker.getJFormattedTextField().getText());

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
      UtilDateModel model = new UtilDateModel();

      Properties props = new Properties();
      props.put("text.today", "Сегодня");
      props.put("text.month", "Месяц");
      props.put("text.year", "Год");

      JDatePanelImpl datePanel = new JDatePanelImpl(model, props);

      datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
      /*--------------------------------------------------------------*/
      JPanel outcomingPanel = new JPanel(new GridLayout(5, 2));

      outcomingSourceComboBox = new JComboBox<String>();

      for (Source source : mainFrame.getSources().values())
      {
         outcomingSourceComboBox.addItem(source.getName());
      }

      outcomingPanel.add(new JLabel("Группа данных:"));

      outcomingPanel.add(outcomingSourceComboBox);

      outcomingPanel.add(new JLabel("Наименование товара:"));

      outcomingNameComboBox = new JComboBox<String>();

      List<String> names = new Connection().getUniqueProductNames();

      Collections.sort(names);

      if (!names.isEmpty())
      {
         for (String name : names)
         {
            outcomingNameComboBox.addItem(name);
         }
      }

      outcomingPanel.add(outcomingNameComboBox);

      outcomingPanel.add(new JLabel("Цена, грн./ед.:"));

      outcomingCostComboBox = new JComboBox<String>();

      outcomingPanel.add(outcomingCostComboBox);

      outcomingPanel.add(new JLabel("Количество, ед.:"));

      outcomingAmountTextField = new JTextField();

      outcomingPanel.add(outcomingAmountTextField);

      outcomingPanel.add(new JLabel("Дата:"));

      outcomingPanel.add(datePicker);

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

      outcomingNameComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            outcomingCostComboBox.removeAllItems();

            List<Double> prices = new Connection().getPricesByProductAndSource(
                  (String) outcomingNameComboBox.getSelectedItem(),
                  mainFrame.getSources().indexOf((String) outcomingSourceComboBox.getSelectedItem()));

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

      outcomingsFrame.setResizable(false);

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
         JOptionPane.showMessageDialog(null, "Заполните поле наименования товара", "Ошибка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (outcomingCostComboBox.getSelectedItem() == null
            || ((String) outcomingCostComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заполните поле стоимости", "Ошибка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (outcomingAmountTextField.getText() == null || outcomingAmountTextField.getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заполните поле количества", "Ошибка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (datePicker.getJFormattedTextField().getText() == null
            || datePicker.getJFormattedTextField().getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заполните поле даты", "Ошибка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }

      return result;
   }
}
