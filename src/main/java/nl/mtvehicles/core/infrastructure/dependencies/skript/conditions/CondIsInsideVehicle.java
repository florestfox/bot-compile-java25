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
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is inside a vehicle")
@Description({"Check if a player is seated in an MTV Vehicle"})
@Examples({"if player {_p} is seated in an mtv vehicle:", "if player is not inside mtv vehicle:"})
@Since({"2.5.6"})
public class CondIsInsideVehicle extends Condition {
   private Expression<Player> player;

   public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
      this.setNegated(matchedPattern == 1);
      this.player = exprs[0];
      return true;
   }

   public boolean check(Event event) {
      boolean check = this.player.getSingle(event) != null && isInsideVehicle((Player)this.player.getSingle(event));
      if (!this.isNegated()) {
         return check;
      } else {
         return !check;
      }
   }

   private static boolean isInsideVehicle(Player p) {
      return VehicleUtils.isInsideVehicle(p);
   }

   public String toString(@Nullable Event e, boolean d) {
      String neg = this.isNegated() ? " not" : "";
      return "Check if player is" + neg + " seated in MTV vehicle.";
   }

   static {
      Skript.registerCondition(CondIsInsideVehicle.class, new String[]{"[player] %player% is [(seated in|inside)] [(a|an)] mtv vehicle", "[player] %player% (isn't|is not) [(seated in|inside)] [(a|an)] mtv vehicle"});
   }
}
