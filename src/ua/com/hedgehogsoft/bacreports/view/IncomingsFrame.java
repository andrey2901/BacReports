package ua.com.hedgehogsoft.bacreports.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class IncomingsFrame
{
   private JButton closeButton = null;
   private JButton incomingButton = null;
   private JDatePickerImpl datePicker = null;
   private JComboBox<String> incomingNameComboBox = null;
   private JComboBox<String> incomingCostComboBox = null;
   private JTextField incomingAmountTextField = null;
   private static final Logger logger = Logger.getLogger(IncomingsFrame.class);

   public IncomingsFrame()
   {
      final JFrame mainFrame = new JFrame("��������� - ������");

      mainFrame.pack();

      mainFrame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent we)
         {
            logger.info("IncomingsFrame was closed.");

            mainFrame.dispose();
         }
      });

      closeButton = new JButton("�������");

      closeButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            mainFrame.dispose();

            logger.info("IncomingsFrame was closed.");
         }
      });

      incomingButton = new JButton("������������");

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
               }
               else
               {
                  new Connection().addProductToStore(product);
               }

               new Connection().addIncoming(product, datePicker.getJFormattedTextField().getText());
            }

            logger.info("Incomings were performed.");
         }
      });

      JPanel buttonsPanel = new JPanel();

      buttonsPanel.add(incomingButton);

      buttonsPanel.add(closeButton);

      /*--------------------------------------------------------------*/
      UtilDateModel model = new UtilDateModel();

      Properties props = new Properties();
      props.put("text.today", "�������");
      props.put("text.month", "�����");
      props.put("text.year", "���");

      JDatePanelImpl datePanel = new JDatePanelImpl(model, props);

      datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
      /*--------------------------------------------------------------*/
      JPanel incomingPanel = new JPanel(new GridLayout(4, 2));

      incomingPanel.add(new JLabel("������������ ������:"));

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

      incomingPanel.add(new JLabel("����, ���./��.:"));

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

      incomingPanel.add(new JLabel("����������, ��.:"));

      incomingAmountTextField = new JTextField();

      incomingPanel.add(incomingAmountTextField);

      incomingPanel.add(new JLabel("����:"));

      incomingPanel.add(datePicker);

      mainFrame.add(incomingPanel, BorderLayout.CENTER);

      mainFrame.add(buttonsPanel, BorderLayout.SOUTH);

      mainFrame.pack();

      mainFrame.setResizable(false);

      mainFrame.setLocationRelativeTo(null);

      mainFrame.setVisible(true);

      logger.info("IncomingsFrame was started.");
   }

   private boolean checkInputData()
   {
      boolean result = true;

      if (incomingNameComboBox.getSelectedItem() == null || ((String) incomingNameComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "��������� ���� ������������ ������", "������", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (incomingCostComboBox.getSelectedItem() == null || ((String) incomingCostComboBox.getSelectedItem()).isEmpty())
      {
         JOptionPane.showMessageDialog(null, "��������� ���� ���������", "������", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (incomingAmountTextField.getText() == null || incomingAmountTextField.getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "��������� ���� ����������", "������", JOptionPane.ERROR_MESSAGE);

         result = false;
      }
      if (datePicker.getJFormattedTextField().getText() == null
            || datePicker.getJFormattedTextField().getText().isEmpty())
      {
         JOptionPane.showMessageDialog(null, "��������� ���� ����", "������", JOptionPane.ERROR_MESSAGE);

         result = false;
      }

      return result;
   }
}
