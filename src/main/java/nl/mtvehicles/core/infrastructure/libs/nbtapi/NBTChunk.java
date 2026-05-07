package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.CheckUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import org.bukkit.Chunk;

public class NBTChunk {
   private final Chunk chunk;

   public NBTChunk(Chunk chunk) {
      this.chunk = chunk;
   }

   public NBTCompound getPersistentDataContainer() {
      CheckUtil.assertAvailable(MinecraftVersion.MC1_16_R3);
      return new NBTPersistentDataContainer(this.chunk.getPersistentDataContainer());
   }
}
