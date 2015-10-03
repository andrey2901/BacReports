package ua.com.hedgehogsoft.bacreports.model;

public class Price
{
   private Integer hryvnias;
   private Integer kopecks;

   public Integer getHryvnias()
   {
      return hryvnias;
   }

   public void setHryvnias(Integer hryvnias)
   {
      this.hryvnias = hryvnias;
   }

   public Integer getKopecks()
   {
      return kopecks;
   }

   public void setKopecks(Integer kopecks)
   {
      this.kopecks = kopecks;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (!(obj instanceof Price))
      {
         return false;
      }

      if (obj == this)
      {
         return true;
      }

      Price price = (Price) obj;

      if (hryvnias == price.getHryvnias() && kopecks == price.getKopecks())
      {
         return true;
      }
      return false;
   }
}
