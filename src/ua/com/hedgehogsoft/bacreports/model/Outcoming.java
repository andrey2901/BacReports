package ua.com.hedgehogsoft.bacreports.model;

import java.sql.Date;

public class Outcoming
{
   private int id;
   private Product product;
   private Date date;

   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

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
