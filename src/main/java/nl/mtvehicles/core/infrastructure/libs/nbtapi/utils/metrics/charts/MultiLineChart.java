package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts;

import java.util.Map;
import java.util.concurrent.Callable;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class MultiLineChart extends CustomChart {
   private final Callable<Map<String, Integer>> callable;

   public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
      super(chartId);
      this.callable = callable;
   }

   protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
      JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
      Map<String, Integer> map = (Map)this.callable.call();
      if (map != null && !map.isEmpty()) {
         boolean allSkipped = true;

         for(Map.Entry<String, Integer> entry : map.entrySet()) {
            if ((Integer)entry.getValue() != 0) {
               allSkipped = false;
               valuesBuilder.appendField((String)entry.getKey(), (Integer)entry.getValue());
            }
         }

         if (allSkipped) {
            return null;
         } else {
            return (new JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
         }
      } else {
         return null;
      }
   }
}
