package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Vehicle Damage Event")
@Description({"Called when a vehicle is damaged"})
@Examples({"on mtv vehicle damage:", "set {_damager} to event-entity", "set {_licensePlate} to event-text", "set {_damage} to event-number"})
@Since({"2.5.6"})
public class EvtVehicleDamage extends SkriptEvent {
   public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
      return true;
   }

   public boolean check(Event e) {
      return true;
   }

   public String toString(@Nullable Event e, boolean debug) {
      return "Vehicle damage event";
   }

   static {
      Skript.registerEvent("VehicleDamageEvent", EvtVehicleDamage.class, VehicleDamageEvent.class, new String[]{"[mtv] vehicle damage"});
      EventValues.registerEventValue(VehicleDamageEvent.class, Entity.class, new Getter<Entity, VehicleDamageEvent>() {
         public Entity get(VehicleDamageEvent event) {
            return event.getDamager();
         }
      }, 0);
      EventValues.registerEventValue(VehicleDamageEvent.class, String.class, new Getter<String, VehicleDamageEvent>() {
         public String get(VehicleDamageEvent event) {
            return event.getLicensePlate();
         }
      }, 0);
      EventValues.registerEventValue(VehicleDamageEvent.class, Double.class, new Getter<Double, VehicleDamageEvent>() {
         public Double get(VehicleDamageEvent event) {
            return event.getDamage();
         }
      }, 0);
   }
}
