package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's location")
@Description({"Get the vehicle location"})
@Examples({"set {_loc} to {_car}'s vehicle location", "set {_loc} to vehicle location of {_car}", "set {_loc} to the vehicle location of (mtv vehicle with license plate \"DF-4J-2R\")"})
public class ExprVehicleLocation extends SimpleExpression<Location> {
   private Expression<Vehicle> vehicle;

   public Class<? extends Location> getReturnType() {
      return Location.class;
   }

   public boolean isSingle() {
      return true;
   }

   public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
      this.vehicle = expressions[0];
      return true;
   }

   public String toString(@Nullable Event event, boolean debug) {
      return "MTVehicle's location";
   }

   protected Location[] get(Event event) {
      return this.vehicle.getSingle(event) == null ? null : new Location[]{VehicleUtils.getLocation((Vehicle)this.vehicle.getSingle(event))};
   }

   static {
      Skript.registerExpression(ExprVehicleLocation.class, Location.class, ExpressionType.PROPERTY, new String[]{"%mtvehicle%'s [mtv] vehicle location", "[the] [mtv] vehicle location of %mtvehicle%"});
   }
}
