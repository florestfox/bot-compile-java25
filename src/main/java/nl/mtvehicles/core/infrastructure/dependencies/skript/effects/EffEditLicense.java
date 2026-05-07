package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/** @deprecated */
@Deprecated
public class EffEditLicense extends Effect {
   private Expression<Object> vehicle;
   private Expression<String> newPlate;

   public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
      this.newPlate = expressions[1];
      this.vehicle = expressions[0];
      return true;
   }

   public String toString(@Nullable Event event, boolean debug) {
      return String.format("Edit vehicle's license plate to %s.", this.newPlate.toString(event, debug));
   }

   protected void execute(Event event) {
      if (this.vehicle.getSingle(event) instanceof Vehicle && this.vehicle.getSingle(event) != null) {
         Vehicle vehicle = (Vehicle)this.vehicle.getSingle(event);
         vehicle.setLicensePlate((String)this.newPlate.getSingle(event));
         vehicle.save();
      } else {
         Main.logSevere("Skript error: Provided variable is not a vehicle (\"edit license plate of [mtv] vehicle %vehicle% to %string%\").");
      }
   }

   static {
      Skript.registerEffect(EffEditLicense.class, new String[]{"edit [the] license [plate] of [a[n]] [mtv] vehicle %object% to %string%", "edit [a[n]] [mtv] [vehicle] %object%'s [mtv] license [plate] to %string%"});
   }
}
