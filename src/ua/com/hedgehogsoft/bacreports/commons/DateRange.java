package ua.com.hedgehogsoft.bacreports.commons;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateRange
{
   private Date from;
   private Date to;

   public DateRange(int month, int year)
   {
      Calendar cal = Calendar.getInstance();
      cal.set(year, month, 1);
      from = cal.getTime();

      switch (month)
      {
         case 1:
            if (new GregorianCalendar().isLeapYear(year))
            {
               cal.set(year, month, 29);
               to = cal.getTime();
            }
            else
            {
               cal.set(year, month, 28);
               to = cal.getTime();
            }
            break;
         case 0:
         case 2:
         case 4:
         case 6:
         case 7:
         case 9:
         case 11:
            cal.set(year, month, 31);
            to = cal.getTime();
            break;
         case 3:
         case 5:
         case 8:
         case 10:
            cal.set(year, month, 30);
            to = cal.getTime();
            break;
      }
   }

   public Date from()
   {
      return from;
   }

   public Date to()
   {
      return to;
   }
}
