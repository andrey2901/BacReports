package ua.com.hedgehogsoft.bacreports.model;

public class Amount
{
   private long integer;
   private long fraction;
   private long digit;

   public Amount(String amount)
   {
      convert(amount);
   }

   private void convert(String amount)
   {
      if (amount.contains("."))
      {
         String[] amounts = amount.split(".");

         integer = Long.valueOf(amounts[0]);

         fraction = Long.valueOf(amounts[1]);

         digit = Long.valueOf(amounts[1].length());
      }
      else if (amount.contains(","))
      {
         String[] amounts = amount.split(",");

         integer = Long.valueOf(amounts[0]);

         fraction = Long.valueOf(amounts[1]);

         digit = Long.valueOf(amounts[1].length());
      }
   }
   
   public String getTotalPriceAsString(Money price)
   {
      long _integer = integer * price.getKopecks();
      long _fraction = fraction * price.getKopecks();
      
      return "";
   }

   private int index(int x)
   {
      int n = 1;

      for (int i = 0; i < x; i++)
      {
         n *= 10;
      }

      return n;
   }
}
