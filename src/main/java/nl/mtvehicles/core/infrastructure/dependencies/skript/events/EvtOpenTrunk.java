package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Vehicle Trunk Open Event")
@Description({"Called when a vehicle's trunk is opened'"})
@Examples({"on vehicle trunk open:", "set {_player} to event-player", "set {_licensePlate} to event-text"})
public class EvtOpenTrunk extends SkriptEvent {
   public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
      return true;
   }

   public boolean check(Event e) {
      return true;
   }

   public String toString(@Nullable Event e, boolean debug) {
      return "Vehicle open trunk event";
   }

   static {
      Skript.registerEvent("VehicleOpenTrunkEvent", EvtOpenTrunk.class, VehicleOpenTrunkEvent.class, new String[]{"[mtv] vehicle trunk open"});
      EventValues.registerEventValue(VehicleOpenTrunkEvent.class, Player.class, new Getter<Player, VehicleOpenTrunkEvent>() {
         public Player get(VehicleOpenTrunkEvent event) {
            return event.getPlayer();
         }
      }, 0);
      EventValues.registerEventValue(VehicleOpenTrunkEvent.class, String.class, new Getter<String, VehicleOpenTrunkEvent>() {
         public String get(VehicleOpenTrunkEvent event) {
            return event.getLicensePlate();
         }
      }, 0);
   }
}
