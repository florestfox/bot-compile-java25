package nl.mtvehicles.core.infrastructure.enums;

public enum RegionAction {
   PLACE,
   PICKUP,
   ENTER,
   RIDE;

   // $FF: synthetic method
   private static RegionAction[] $values() {
      return new RegionAction[]{PLACE, PICKUP, ENTER, RIDE};
   }

   public static enum ListType {
      DISABLED,
      WHITELIST,
      BLACKLIST;

      public boolean isEnabled() {
         return !this.equals(DISABLED);
      }

      public boolean isWhitelist() {
         return this.equals(WHITELIST);
      }

      public boolean isBlacklist() {
         return this.equals(BLACKLIST);
      }

      // $FF: synthetic method
      private static ListType[] $values() {
         return new ListType[]{DISABLED, WHITELIST, BLACKLIST};
      }
   }
}
