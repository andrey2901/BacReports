package ua.com.hedgehogsoft.bacreports.model;

public class Money
{
   private long kopecks;

   public Money(long kopecks)
   {
      this.kopecks = kopecks;
   }

   public Money(String kopecks)
   {
      this.kopecks = convert(kopecks);
   }

   public long getKopecks()
   {
      return kopecks;
   }

   public String getMoneyAsString()
   {
      return Long.toString(kopecks / 100) + "." + Long.toString(kopecks % 100);
   }

   private long convert(String kopecks)
   {
      if (kopecks.contains("."))
      {
         kopecks = kopecks.replace(".", "");
      }
      else if (kopecks.contains(","))
      {
         kopecks = kopecks.replace(",", "");
      }
      return Long.valueOf(kopecks);
   }
}
