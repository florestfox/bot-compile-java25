package nl.mtvehicles.core.infrastructure.enums;

import java.util.Arrays;
import java.util.List;

public enum WGFlag {
   GAS_STATION("mtv-gasstation"),
   PLACE("mtv-place"),
   ENTER("mtv-enter"),
   PICKUP("mtv-pickup"),
   USE_CAR("mtv-use-car"),
   USE_HOVER("mtv-use-hover"),
   USE_TANK("mtv-use-tank"),
   USE_HELICOPTER("mtv-use-helicopter"),
   USE_AIRPLANE("mtv-use-airplane"),
   USE_BOAT("mtv-use-boat"),
   RIDE("mtv-ride");

   private String key;

   private WGFlag(String key) {
      this.key = key;
   }

   public String getKey() {
      return this.key;
   }

   public static List<WGFlag> getFlagList() {
      return Arrays.asList(values());
   }

   // $FF: synthetic method
   private static WGFlag[] $values() {
      return new WGFlag[]{GAS_STATION, PLACE, ENTER, PICKUP, USE_CAR, USE_HOVER, USE_TANK, USE_HELICOPTER, USE_AIRPLANE, USE_BOAT, RIDE};
   }
}
