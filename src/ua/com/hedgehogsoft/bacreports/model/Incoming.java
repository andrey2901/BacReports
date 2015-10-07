package ua.com.hedgehogsoft.bacreports.model;

import java.sql.Date;

public class Incoming
{
   private Product product;
   private Date date;

   public Product getProduct()
   {
      return product;
   }

   public void setProduct(Product product)
   {
      this.product = product;
   }

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }
}
