package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
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
   private static final Logger logger = Logger.getLogger(IncomingsFrame.class);

   public IncomingsFrame(MainFrame mainFrame)
   {
      final JFrame incomingsFrame = new JFrame("БакОтчеты - приход");

      incomingsFrame.pack();

      incomingsFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("IncomingsFrame was closed.");

            incomingsFrame.dispose();
         }
      });

      closeButton = new JButton("Закрыть");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            incomingsFrame.dispose();

            logger.info("IncomingsFrame was closed.");
         }
      });

      incomingButton = new JButton("Оприходовать");

      incomingButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            if (checkInputData())
            {
               Product product = new Product();

               product.setName((String) incomingNameComboBox.getSelectedItem());

               product.setPrice(Double.valueOf(((String) incomingCostComboBox.getSelectedItem()).replace(",", ".")));

               product.setAmount(Double.valueOf(incomingAmountTextField.getText().replace(",", ".")));

               if (new Connection().productExist(product.getName(), product.getPrice()))
               {
                  Product existedProduct = new Connection().getProductByNameAndPrice(product.getName(),
                        product.getPrice());

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

               new Connection().addIncoming(product, datePicker.getJFormattedTextField().getText());

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
      UtilDateModel model = new UtilDateModel();

      Properties props = new Properties();
      props.put("text.today", "Сегодня");
      props.put("text.month", "Месяц");
      props.put("text.year", "Год");

      JDatePanelImpl datePanel = new JDatePanelImpl(model, props);

      datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
      /*--------------------------------------------------------------*/
      JPanel incomingPanel = new JPanel(new GridLayout(5, 2));

      incomingPanel.add(new JLabel("Наименование товара:"));

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

      incomingPanel.add(incomingNameComboBox);

      incomingPanel.add(new JLabel("Цена, грн./ед.:"));

      incomingCostComboBox = new JComboBox<String>();

      incomingCostComboBox.setEditable(true);

      incomingPanel.add(incomingCostComboBox);

      incomingNameComboBox.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            incomingCostComboBox.removeAllItems();

            List<Double> prices = new Connection().getPricesByProduct((String) incomingNameComboBox.getSelectedItem());

            if (!prices.isEmpty())
            {
               for (Double price : prices)
               {
                  incomingCostComboBox.addItem(Double.toString(price));
               }
            }
         }
      });

      incomingPanel.add(new JLabel("Количество, ед.:"));

      incomingAmountTextField = new JTextField();

      incomingPanel.add(incomingAmountTextField);

      incomingSourceComboBox = new JComboBox<String>();

      Iterator<String> it = mainFrame.getSources().getValues().keySet().iterator();

      while (it.hasNext())
      {
         incomingSourceComboBox.addItem(it.next());
      }

      incomingPanel.add(new JLabel("Группа данных:"));

      incomingPanel.add(incomingSourceComboBox);

      incomingPanel.add(new JLabel("Дата:"));

      incomingPanel.add(datePicker);

      incomingsFrame.add(incomingPanel, BorderLayout.CENTER);

      incomingsFrame.add(buttonsPanel, BorderLayout.SOUTH);

      incomingsFrame.pack();

      incomingsFrame.setResizable(false);

      incomingsFrame.setLocationRelativeTo(null);

      incomingsFrame.setVisible(true);

      logger.info("IncomingsFrame was started.");
   }

   private boolean checkInputData()
   {
      boolean result = true;

      if (incomingNameComboBox.getSelectedItem() == null || ((String) incomingNameComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заполните поле наименования товара", "Ошибка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (incomingCostComboBox.getSelectedItem() == null || ((String) incomingCostComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "Заполните поле стоимости", "Ошибка", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (incomingAmountTextField.getText() == null || incomingAmountTextField.getText().isEmpty())
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
