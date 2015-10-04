package ua.com.hedgehogsoft.bacreports.model.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ua.com.hedgehogsoft.bacreports.model.Money;

public class TestMoney
{
   @Test
   public void getKopecksAsString()
   {
      Money money1 = new Money("5.60");
      Money money2 = new Money("7,70");
      Money money3 = new Money("2450.70");

      assertEquals(money1.getKopecks(), 560);
      assertEquals(money2.getKopecks(), 770);
      assertEquals(money3.getKopecks(), 245070);
      assertEquals(money3.getMoneyAsString(), "2450.70");
   }
}
