package ua.com.hedgehogsoft.bacreports.model.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ua.com.hedgehogsoft.bacreports.model.Product;

public class TestProduct
{
   Product product = null;

   @Before
   public void init()
   {
      product = new Product();
      product.setName("agar");
      product.setPrice(2.75);
      product.setAmount(1.005);
   }

   @Test
   public void testProduct()
   {
      assertEquals(product.getName(), "agar");
      assertEquals(product.getPrice(), 2.75, 0.0001);
      assertEquals(product.getAmount(), 1.005, 0.0001);
      assertEquals(product.getTotalPrice(), 2.76, 0.0001);
   }
}
