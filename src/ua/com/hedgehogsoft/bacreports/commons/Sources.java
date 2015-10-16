package ua.com.hedgehogsoft.bacreports.commons;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.hedgehogsoft.bacreports.model.Source;

public class Sources
{
   private Map<Integer, Source> sources = null;

   public Sources(List<Source> sources)
   {
      this.sources = new HashMap<Integer, Source>();

      for (Source src : sources)
      {
         this.sources.put(src.getId(), src);
      }
   }

   public Source valueOf(int i)
   {
      return sources.get(i);
   }

   public int indexOf(String sourceName)
   {
      for (Source src : sources.values())
      {
         if (src.getName().equals(sourceName))
         {
            return src.getId();
         }
      }

      return -1;
   }

   public Collection<Source> values()
   {
      return sources.values();
   }
}
