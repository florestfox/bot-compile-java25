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
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Condition - Is player can ride a vehicle")
@Description({"Check if a player can ride an MTV Vehicle"})
@Examples({"if player can ride [mtv] vehicle {_car}:", "if {_p} cannot ride [mtv] vehicle {_car}:"})
@Since({"2.5.5"})
public class CondCanRide extends Condition {
   private Expression<Vehicle> vehicle;
   private Expression<Player> player;

   public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
      this.setNegated(matchedPattern == 1);
      this.vehicle = exprs[1];
      this.player = exprs[0];
      return true;
   }

   public boolean check(Event event) {
      if (!Main.isNotNull(this.vehicle.getSingle(event), this.player.getSingle(event))) {
         return this.isNegated();
      } else {
         boolean check = ((Vehicle)this.vehicle.getSingle(event)).canRide((Player)this.player.getSingle(event)) || ((Vehicle)this.vehicle.getSingle(event)).isOwner((OfflinePlayer)this.player.getSingle(event));
         if (!this.isNegated()) {
            return check;
         } else {
            return !check;
         }
      }
   }

   public String toString(@Nullable Event e, boolean d) {
      String neg = this.isNegated() ? "not" : "";
      return "Check if player can" + neg + " ride an MTV vehicle.";
   }

   static {
      Skript.registerCondition(CondCanRide.class, new String[]{"[player] %player% can ride [the] [mtv] vehicle %mtvehicle%", "[player] %player% (cannot|can't) ride [the] [mtv] vehicle %mtvehicle%"});
   }
}
