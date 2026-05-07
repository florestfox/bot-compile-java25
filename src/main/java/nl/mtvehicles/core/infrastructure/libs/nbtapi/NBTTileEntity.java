package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.CheckUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;

public class NBTTileEntity extends NBTCompound {
   private final BlockState tile;
   private final boolean readonly;
   private final Object compound;
   private boolean closed = false;

   protected NBTTileEntity(BlockState tile, boolean readonly) {
      super((NBTCompound)null, (String)null);
      if (tile != null && (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_8_R3) || tile.isPlaced())) {
         this.tile = tile;
         this.readonly = readonly;
         if (readonly) {
            this.compound = this.getCompound();
         } else {
            this.compound = null;
         }

      } else {
         throw new NullPointerException("Tile can't be null/not placed!");
      }
   }

   /** @deprecated */
   @Deprecated
   public NBTTileEntity(BlockState tile) {
      super((NBTCompound)null, (String)null);
      if (tile != null && (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_8_R3) || tile.isPlaced())) {
         this.readonly = false;
         this.compound = null;
         this.tile = tile;
      } else {
         throw new NullPointerException("Tile can't be null/not placed!");
      }
   }

   protected void setClosed() {
      this.closed = true;
   }

   protected boolean isClosed() {
      return this.closed;
   }

   protected boolean isReadOnly() {
      return this.readonly;
   }

   public Object getCompound() {
      if (this.readonly && this.compound != null) {
         return this.compound;
      } else if (!Bukkit.isPrimaryThread()) {
         throw new NbtApiException("BlockEntity NBT needs to be accessed sync!");
      } else {
         return NBTReflectionUtil.getTileEntityNBTTagCompound(this.tile);
      }
   }

   protected void setCompound(Object compound) {
      if (this.readonly) {
         throw new NbtApiException("Tried setting data in read only mode!");
      } else if (!Bukkit.isPrimaryThread()) {
         throw new NbtApiException("BlockEntity NBT needs to be accessed sync!");
      } else {
         NBTReflectionUtil.setTileEntityNBTTagCompound(this.tile, compound);
      }
   }

   public NBTCompound getPersistentDataContainer() {
      CheckUtil.assertAvailable(MinecraftVersion.MC1_14_R1);
      if (this.hasTag("PublicBukkitValues")) {
         return this.getCompound("PublicBukkitValues");
      } else {
         NBTContainer container = new NBTContainer();
         container.addCompound("PublicBukkitValues").setString("__nbtapi", "Marker to make the PersistentDataContainer have content");
         this.mergeCompound(container);
         return this.getCompound("PublicBukkitValues");
      }
   }
}
