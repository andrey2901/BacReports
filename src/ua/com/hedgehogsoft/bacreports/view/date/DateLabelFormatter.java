package ua.com.hedgehogsoft.bacreports.view.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.apache.log4j.Logger;

public class DateLabelFormatter extends AbstractFormatter
{
   private static final long serialVersionUID = 1L;
   private String datePattern = "dd-MM-yyyy";
   private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
   private static final Logger logger = Logger.getLogger(DateLabelFormatter.class);

   @Override
   public Object stringToValue(String text)
   {
      Object result = null;

      try
      {
         result = dateFormatter.parseObject(text);
      }
      catch (ParseException e)
      {
         logger.error("Date parsing from string error.", e);
      }
      return result;
   }

   @Override
   public String valueToString(Object value)
   {
      if (value != null)
      {
         Calendar cal = (Calendar) value;

         return dateFormatter.format(cal.getTime());
      }

      return "";
   }

}