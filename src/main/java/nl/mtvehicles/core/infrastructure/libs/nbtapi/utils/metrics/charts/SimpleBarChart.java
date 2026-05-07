package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts;

import java.util.Map;
import java.util.concurrent.Callable;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class SimpleBarChart extends CustomChart {
   private final Callable<Map<String, Integer>> callable;

   public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
      super(chartId);
      this.callable = callable;
   }

   protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
      JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
      Map<String, Integer> map = (Map)this.callable.call();
      if (map != null && !map.isEmpty()) {
         for(Map.Entry<String, Integer> entry : map.entrySet()) {
            valuesBuilder.appendField((String)entry.getKey(), new int[]{(Integer)entry.getValue()});
         }

         return (new JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
      } else {
         return null;
      }
   }
}
