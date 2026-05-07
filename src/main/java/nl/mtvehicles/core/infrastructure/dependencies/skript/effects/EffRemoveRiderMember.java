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

@Name("Remove rider/member to MTV vehicle")
@Description({"Remove a rider or member to an MTV vehicle."})
@Examples({"remove {_player} as a rider of the vehicle {_car}", "remove player {_offlinePlayer} as a member from mtv vehicle {_car}"})
@Since({"2.5.5"})
public class EffRemoveRiderMember extends Effect {
   private Expression<Vehicle> vehicle;
   private Expression<OfflinePlayer> player;
   private MemberType type;

   public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
      this.player = expressions[0];
      this.vehicle = expressions[1];
      this.type = matchedPattern == 0 ? EffRemoveRiderMember.MemberType.RIDER : EffRemoveRiderMember.MemberType.MEMBER;
      return true;
   }

   public String toString(@Nullable Event event, boolean debug) {
      return String.format("Remove offline player %s as a rider/member from an mtv vehicle.", this.player.toString(event, debug));
   }

   protected void execute(Event event) {
      if (Main.isNotNull(this.vehicle.getSingle(event), this.player.getSingle(event))) {
         Vehicle vehicle = (Vehicle)this.vehicle.getSingle(event);
         String playerUUID = ((OfflinePlayer)this.player.getSingle(event)).getUniqueId().toString();
         if (this.type.equals(EffRemoveRiderMember.MemberType.RIDER)) {
            List<String> riders = vehicle.getRiders();
            if (!riders.contains(playerUUID)) {
               return;
            }

            riders.remove(playerUUID);
            vehicle.setRiders(riders);
         } else {
            List<String> members = vehicle.getMembers();
            if (!members.contains(playerUUID)) {
               return;
            }

            members.remove(playerUUID);
            vehicle.setMembers(members);
         }

         vehicle.save();
      }
   }

   static {
      Skript.registerEffect(EffRemoveRiderMember.class, new String[]{"remove [player] %offlineplayer% as [a] rider (of|from) [the] [mtv] vehicle %mtvehicle%", "remove [player] %offlineplayer% as [a] member (of|from) [the] [mtv] vehicle %mtvehicle%"});
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
