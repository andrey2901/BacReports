package ua.com.hedgehogsoft.bacreports.view.date;

import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class DatePicker
{
   public static JDatePickerImpl getDatePicker()
   {
      UtilDateModel model = new UtilDateModel();

      Properties props = new Properties();
      props.put("text.today", "�������");
      props.put("text.month", "̳����");
      props.put("text.year", "г�");

      JDatePanelImpl datePanel = new JDatePanelImpl(model, props);

      return new JDatePickerImpl(datePanel, new DateLabelFormatter());
   }
}
