package nl.mtvehicles.core.infrastructure.dependencies.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

public class Types {
   static {
      if (Classes.getClassInfoNoError("mtvehicle") == null) {
         Classes.registerClass((new ClassInfo(Vehicle.class, "mtvehicle")).user(new String[]{"mtvehicles?"}).name("MTVehicle").description(new String[]{"Represents an MTV Vehicle."}).since("2.5.5").parser(new Parser<Vehicle>() {
            public boolean canParse(ParseContext context) {
               return false;
            }

            public String toString(Vehicle v, int flags) {
               return v.toString();
            }

            public String toVariableNameString(Vehicle v) {
               return v.toString();
            }
         }));
      }

   }
}
