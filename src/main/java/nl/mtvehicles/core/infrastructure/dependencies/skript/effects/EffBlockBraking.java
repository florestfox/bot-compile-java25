package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Block/Unblock braking for an MTV vehicle")
@Description({"Block or unblock braking for an MTV vehicle"})
@Examples({"block vehicle breaking of mtv vehicle {_car}", "block mtv vehicle breaking of license \"MT-12-34\"", "unblock mtv vehicle breaking of vehicle {_car}", "unblock vehicle breaking of license plate \"MT-12-34\""})
@Since({"2.5.6"})
public class EffBlockBraking extends Effect {
   private Expression<Vehicle> vehicle;
   private Expression<String> licensePlate;
   private boolean block;
   private boolean usingLicensePlate;

   public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
      this.block = matchedPattern < 2;
      this.usingLicensePlate = matchedPattern == 1 || matchedPattern == 3;
      if (!this.usingLicensePlate) {
         this.vehicle = expressions[0];
      } else {
         this.licensePlate = expressions[0];
      }

      return true;
   }

   public String toString(@Nullable Event event, boolean debug) {
      return "(Un)Block braking of a vehicle.";
   }

   protected void execute(Event event) {
      if (this.usingLicensePlate) {
         if (this.block) {
            VehicleData.brakingBlocked.add((String)this.licensePlate.getSingle(event));
         } else {
            VehicleData.brakingBlocked.remove(this.licensePlate.getSingle(event));
         }

      } else if (this.vehicle.getSingle(event) != null) {
         String license = ((Vehicle)this.vehicle.getSingle(event)).getLicensePlate();
         if (this.block) {
            VehicleData.brakingBlocked.add(license);
         } else {
            VehicleData.brakingBlocked.remove(license);
         }

      }
   }

   static {
      Skript.registerEffect(EffBlockBraking.class, new String[]{"block [mtv] vehicle breaking of [mtv] [vehicle] %mtvehicle%", "block [mtv] vehicle breaking of license [plate] %string%", "unblock [mtv] vehicle breaking of [mtv] [vehicle] %mtvehicle%", "unblock [mtv] vehicle breaking of license [plate] %string%"});
   }
}
