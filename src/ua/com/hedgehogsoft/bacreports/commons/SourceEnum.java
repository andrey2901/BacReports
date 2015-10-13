package ua.com.hedgehogsoft.bacreports.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ua.com.hedgehogsoft.bacreports.model.Source;

public class SourceEnum
{
   private Map<String, Source> values;

   public SourceEnum()
   {
      values = new HashMap<String, Source>();
   }

   public SourceEnum(Map<String, Source> values)
   {
      this.values = values;
   }

   public SourceEnum(List<Source> sources)
   {
      values = new HashMap<String, Source>();

      for (Source source : sources)
      {
         values.put(source.getName(), source);
      }
   }

   public void addValue(Source val)
   {
      values.put(val.getName(), val);
   }

   public Map<String, Source> getValues()
   {
      return values;
   }

   public Source valueOf(String name)
   {
      Source result = null;

      if (values.containsKey(name))
      {
         result = values.get(name);
      }

      return result;
   }

   public String indexOf(Source source)
   {
      String result = null;

      for (Entry<String, Source> entry : values.entrySet())
      {
         if (source.getName().equals(entry.getValue().getName()))
         {
            return entry.getKey();
         }
      }
      return result;
   }
}
