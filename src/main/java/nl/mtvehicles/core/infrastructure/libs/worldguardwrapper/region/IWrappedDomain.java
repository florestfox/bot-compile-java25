package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region;

import java.util.Set;
import java.util.UUID;

public interface IWrappedDomain {
   Set<UUID> getPlayers();

   void addPlayer(UUID var1);

   void removePlayer(UUID var1);

   Set<String> getGroups();

   void addGroup(String var1);

   void removeGroup(String var1);
}
