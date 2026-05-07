package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts;

import java.util.Map;
import java.util.concurrent.Callable;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class DrilldownPie extends CustomChart {
   private final Callable<Map<String, Map<String, Integer>>> callable;

   public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
      super(chartId);
      this.callable = callable;
   }

   public JsonObjectBuilder.JsonObject getChartData() throws Exception {
      JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
      Map<String, Map<String, Integer>> map = (Map)this.callable.call();
      if (map != null && !map.isEmpty()) {
         boolean reallyAllSkipped = true;

         for(Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
            JsonObjectBuilder valueBuilder = new JsonObjectBuilder();
            boolean allSkipped = true;

            for(Map.Entry<String, Integer> valueEntry : ((Map)map.get(entryValues.getKey())).entrySet()) {
               valueBuilder.appendField((String)valueEntry.getKey(), (Integer)valueEntry.getValue());
               allSkipped = false;
            }

            if (!allSkipped) {
               reallyAllSkipped = false;
               valuesBuilder.appendField((String)entryValues.getKey(), valueBuilder.build());
            }
         }

         if (reallyAllSkipped) {
            return null;
         } else {
            return (new JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
         }
      } else {
         return null;
      }
   }
}
