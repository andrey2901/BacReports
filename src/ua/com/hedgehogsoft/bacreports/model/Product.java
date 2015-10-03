package ua.com.hedgehogsoft.bacreports.model;

public class Product
{
   private String name;
   private Integer amount;
   private Price price;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Integer getAmount()
   {
      return amount;
   }

   public void setAmount(Integer amount)
   {
      this.amount = amount;
   }

   public Price getPrice()
   {
      return price;
   }

   public void setPrice(Price price)
   {
      this.price = price;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (!(obj instanceof Product))
      {
         return false;
      }

      if (obj == this)
      {
         return true;
      }

      Product product = (Product) obj;

      if (name == product.getName() && price == product.getPrice())
      {
         return true;
      }
      return false;
   }
}
