package ua.com.hedgehogsoft.bacreports.view.date;

import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ua.com.hedgehogsoft.bacreports.commons.DateLabelFormatter;

public class DatePicker
{
   public static JDatePickerImpl getDatePicker()
   {
      UtilDateModel model = new UtilDateModel();

      Properties props = new Properties();
      props.put("text.today", "Сьогодні");
      props.put("text.month", "Місяць");
      props.put("text.year", "Рік");

      JDatePanelImpl datePanel = new JDatePanelImpl(model, props);

      return new JDatePickerImpl(datePanel, new DateLabelFormatter());
   }
}
