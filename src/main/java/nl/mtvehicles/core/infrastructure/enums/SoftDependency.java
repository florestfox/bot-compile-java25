package nl.mtvehicles.core.infrastructure.enums;

public enum SoftDependency {
   WORLD_GUARD("WorldGuard"),
   VAULT("Vault"),
   PLACEHOLDER_API("PlaceholderAPI"),
   SKRIPT("Skript");

   private final String name;

   private SoftDependency(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   // $FF: synthetic method
   private static SoftDependency[] $values() {
      return new SoftDependency[]{WORLD_GUARD, VAULT, PLACEHOLDER_API, SKRIPT};
   }
}
