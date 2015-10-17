package ua.com.hedgehogsoft.bacreports.commons;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.hedgehogsoft.bacreports.model.Unit;

public class Units
{
   private Map<Integer, Unit> units = null;

   public Units(List<Unit> units)
   {
      this.units = new HashMap<Integer, Unit>();

      for (Unit unit : units)
      {
         this.units.put(unit.getId(), unit);
      }
   }

   public Unit valueOf(int i)
   {
      return units.get(i);
   }

   public int indexOf(String unitName)
   {
      for (Unit unit : units.values())
      {
         if (unit.getName().equals(unitName))
         {
            return unit.getId();
         }
      }

      return -1;
   }

   public Collection<Unit> values()
   {
      return units.values();
   }
}
