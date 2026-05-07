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
import nl.mtvehicles.core.events.VehicleRegionEnterEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Vehicle Region Enter Event")
@Description({"Called when a vehicle enters a region"})
@Examples({"on vehicle region enter:", "set {_driver} to event-player", "set {_enteredRegion} to event-text"})
public class EvtVehicleRegionEnter extends SkriptEvent {
   public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
      return true;
   }

   public boolean check(Event e) {
      return true;
   }

   public String toString(@Nullable Event e, boolean debug) {
      return "Vehicle region enter event";
   }

   static {
      Skript.registerEvent("VehicleRegionEnter", EvtVehicleRegionEnter.class, VehicleRegionEnterEvent.class, new String[]{"[mtv] vehicle region enter"});
      EventValues.registerEventValue(VehicleRegionEnterEvent.class, Player.class, new Getter<Player, VehicleRegionEnterEvent>() {
         public Player get(VehicleRegionEnterEvent event) {
            return event.getPlayer();
         }
      }, 0);
      EventValues.registerEventValue(VehicleRegionEnterEvent.class, String.class, new Getter<String, VehicleRegionEnterEvent>() {
         public String get(VehicleRegionEnterEvent event) {
            return event.getRegionName();
         }
      }, 0);
   }
}
