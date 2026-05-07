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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Vehicle exists")
@Description({"Check if an MTV Vehicle exists (is not deleted)"})
@Examples({"if the vehicle {_car} exists:", "if the vehicle {_car} is not deleted:"})
@Since({"2.5.6"})
public class CondVehicleExists extends Condition {
   private Expression<Vehicle> vehicle;

   public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
      this.vehicle = exprs[0];
      return true;
   }

   public boolean check(Event event) {
      return this.vehicle.getSingle(event) != null;
   }

   public String toString(@Nullable Event e, boolean d) {
      return "Check if vehicle exists.";
   }

   static {
      Skript.registerCondition(CondVehicleExists.class, new String[]{"[the] [mtv] vehicle %mtvehicle% exist[s]", "[the] [mtv] vehicle %mtvehicle% (isn't|is not) deleted"});
   }
}
