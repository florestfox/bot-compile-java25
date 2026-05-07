package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.util.List;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Add rider/member to MTV vehicle")
@Description({"Add a rider or member to an MTV vehicle."})
@Examples({"add {_player} as a rider of the vehicle {_car}", "add player {_offlinePlayer} as a member to mtv vehicle {_car}"})
@Since({"2.5.5"})
public class EffAddRiderMember extends Effect {
   private Expression<Vehicle> vehicle;
   private Expression<OfflinePlayer> player;
   private MemberType type;

   public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
      this.player = expressions[0];
      this.vehicle = expressions[1];
      this.type = matchedPattern == 0 ? EffAddRiderMember.MemberType.RIDER : EffAddRiderMember.MemberType.MEMBER;
      return true;
   }

   public String toString(@Nullable Event event, boolean debug) {
      return String.format("Add offline player %s as a rider/member to an mtv vehicle.", this.player.toString(event, debug));
   }

   protected void execute(Event event) {
      if (Main.isNotNull(this.vehicle.getSingle(event), this.player.getSingle(event))) {
         Vehicle vehicle = (Vehicle)this.vehicle.getSingle(event);
         String playerUUID = ((OfflinePlayer)this.player.getSingle(event)).getUniqueId().toString();
         if (this.type.equals(EffAddRiderMember.MemberType.RIDER)) {
            List<String> riders = vehicle.getRiders();
            if (riders.contains(playerUUID)) {
               return;
            }

            riders.add(playerUUID);
            vehicle.setRiders(riders);
         } else {
            List<String> members = vehicle.getMembers();
            if (members.contains(playerUUID)) {
               return;
            }

            members.add(playerUUID);
            vehicle.setMembers(members);
         }

         vehicle.save();
      }
   }

   static {
      Skript.registerEffect(EffAddRiderMember.class, new String[]{"add [player] %offlineplayer% as [a] rider (of|to) [the] [mtv] vehicle %mtvehicle%", "add [player] %offlineplayer% as [a] member (of|to) [the] [mtv] vehicle %mtvehicle%"});
   }

   private static enum MemberType {
      RIDER,
      MEMBER;

      // $FF: synthetic method
      private static MemberType[] $values() {
         return new MemberType[]{RIDER, MEMBER};
      }
   }
}
