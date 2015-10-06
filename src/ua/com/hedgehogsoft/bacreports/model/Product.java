package ua.com.hedgehogsoft.bacreports.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product
{
   private String name;
   private Double amount;
   private Double price;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Double getAmount()
   {
      return amount;
   }

   public void setAmount(Double amount)
   {
      this.amount = new BigDecimal(amount).setScale(3, RoundingMode.HALF_EVEN).doubleValue();
   }

   public Double getPrice()
   {
      return price;
   }

   public void setPrice(Double price)
   {
      this.price = new BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
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

      if (product.getName().equals(name) && Double.toString(price).equals(Double.toString(product.getPrice())))
      {
         return true;
      }
      return false;
   }

   public Double getTotalPrice()
   {
      return new BigDecimal(price * amount).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
   }
}
