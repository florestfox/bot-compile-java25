package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Spawn an MTV vehicle")
@Description({"Spawn a vehicle on a specific location"})
@Examples({"spawn mtv vehicle {_car} at location {_loc}", "spawn mtv vehicle with license plate \"DF-4J-2R\" at {_loc}"})
public class EffSpawnVehicle extends Effect {
   private Expression<Vehicle> vehicle;
   private Expression<String> licensePlate;
   private Expression<Location> location;
   private int pattern;

   public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
      if (matchedPattern == 0) {
         this.licensePlate = expressions[0];
         this.location = expressions[1];
      } else {
         this.vehicle = expressions[0];
         this.location = expressions[1];
      }

      this.pattern = matchedPattern;
      return true;
   }

   public String toString(@Nullable Event event, boolean debug) {
      return String.format("Spawn vehicle at location %s", this.location.toString(event, debug));
   }

   protected void execute(Event event) {
      if (this.location.getSingle(event) != null) {
         if (this.pattern == 1) {
            if (this.vehicle.getSingle(event) != null) {
               VehicleUtils.spawnVehicle(((Vehicle)this.vehicle.getSingle(event)).getLicensePlate(), (Location)this.location.getSingle(event));
            }
         } else {
            try {
               VehicleUtils.spawnVehicle((String)this.licensePlate.getSingle(event), (Location)this.location.getSingle(event));
            } catch (IllegalArgumentException var3) {
               Main.logSevere("Skript error: Provided license plate is not valid (\"spawn vehicle by license plate %string% at location %location%\").");
            }

         }
      }
   }

   static {
      Skript.registerEffect(EffSpawnVehicle.class, new String[]{"spawn [mtv] vehicle (by|with) license [plate] %string% at [location] %location%", "spawn [mtv] vehicle %mtvehicle% at [location] %location%"});
   }
}
