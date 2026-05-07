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
import nl.mtvehicles.core.events.VehicleLeaveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Vehicle Leave Event")
@Description({"Called when a vehicle is left"})
@Examples({"on vehicle leave:", "set {_player} to event-player", "set {_licensePlate} to event-text"})
public class EvtVehicleLeave extends SkriptEvent {
   public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
      return true;
   }

   public boolean check(Event e) {
      return true;
   }

   public String toString(@Nullable Event e, boolean debug) {
      return "Vehicle leave event";
   }

   static {
      Skript.registerEvent("VehicleLeaveEvent", EvtVehicleLeave.class, VehicleLeaveEvent.class, new String[]{"[mtv] vehicle leave"});
      EventValues.registerEventValue(VehicleLeaveEvent.class, Player.class, new Getter<Player, VehicleLeaveEvent>() {
         public Player get(VehicleLeaveEvent event) {
            return event.getPlayer();
         }
      }, 0);
      EventValues.registerEventValue(VehicleLeaveEvent.class, String.class, new Getter<String, VehicleLeaveEvent>() {
         public String get(VehicleLeaveEvent event) {
            return event.getLicensePlate();
         }
      }, 0);
   }
}
