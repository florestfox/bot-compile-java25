package nl.mtvehicles.core.infrastructure.dependencies.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is vehicle friction blocked")
@Description({"Check if the vehicle friction of an MTV Vehicle is blocked"})
@Examples({"if mtv vehicle friction of vehicle {_car} is blocked:", "if vehicle friction of license plate \"MT-12-34\" is not blocked:"})
@Since({"2.5.6"})
public class CondIsFrictionBlocked extends Condition {
   private Expression<Vehicle> vehicle;
   private Expression<String> licensePlate;
   private boolean usingLicensePlate;

   public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
      this.setNegated(matchedPattern > 1);
      this.usingLicensePlate = matchedPattern == 1 || matchedPattern == 3;
      if (!this.usingLicensePlate) {
         this.vehicle = exprs[0];
      } else {
         this.licensePlate = exprs[0];
      }

      return true;
   }

   public boolean check(Event event) {
      boolean check;
      if (this.usingLicensePlate) {
         check = VehicleData.frictionBlocked.contains(this.licensePlate.getSingle(event));
      } else {
         if (this.vehicle.getSingle(event) == null) {
            return this.isNegated();
         }

         check = VehicleData.frictionBlocked.contains(((Vehicle)this.vehicle.getSingle(event)).getLicensePlate());
      }

      if (!this.isNegated()) {
         return check;
      } else {
         return !check;
      }
   }

   public String toString(@Nullable Event e, boolean d) {
      String neg = this.isNegated() ? " not" : "";
      return "Check if vehicle friction is" + neg + " blocked.";
   }

   static {
      Skript.registerCondition(CondIsFrictionBlocked.class, new String[]{"[mtv] vehicle friction of [mtv] vehicle %mtvehicle% is blocked", "[mtv] vehicle friction of license [plate] %string% is blocked", "[mtv] vehicle friction of [mtv] vehicle %mtvehicle% (isn't|is not) blocked", "[mtv] vehicle friction of license [plate] %string% (isn't|is not) blocked"});
   }
}
