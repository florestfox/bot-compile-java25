package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteItemNBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadableNBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NBTItem extends NBTCompound implements ReadWriteItemNBT {
   private ItemStack bukkitItem;
   private final boolean directApply;
   private final boolean finalizer;
   private ItemStack originalSrcStack;
   private Object cachedCompound;
   private boolean closed;

   /** @deprecated */
   @Deprecated
   public NBTItem(ItemStack item) {
      this(item, false);
   }

   protected NBTItem(ItemStack item, boolean directApply, boolean readOnly, boolean finalizer) {
      super((NBTCompound)null, (String)null, readOnly);
      this.originalSrcStack = null;
      this.cachedCompound = null;
      this.closed = false;
      if (item != null && item.getType() != Material.AIR && item.getAmount() > 0) {
         this.finalizer = finalizer;
         if (finalizer) {
            this.bukkitItem = item;
            this.originalSrcStack = item;
            this.directApply = false;
         } else if (readOnly) {
            this.bukkitItem = item;
            this.directApply = false;
         } else {
            this.directApply = directApply;
            this.bukkitItem = item.clone();
            if (directApply) {
               this.originalSrcStack = item;
            }
         }

      } else {
         throw new NullPointerException("ItemStack can't be null/air/amount of 0! This is not a NBTAPI bug!");
      }
   }

   /** @deprecated */
   @Deprecated
   public NBTItem(ItemStack item, boolean directApply) {
      super((NBTCompound)null, (String)null);
      this.originalSrcStack = null;
      this.cachedCompound = null;
      this.closed = false;
      if (item != null && item.getType() != Material.AIR && item.getAmount() > 0) {
         this.finalizer = false;
         this.directApply = directApply;
         this.bukkitItem = item.clone();
         if (directApply) {
            this.originalSrcStack = item;
         }

      } else {
         throw new NullPointerException("ItemStack can't be null/air/amount of 0! This is not a NBTAPI bug!");
      }
   }

   public Object getCompound() {
      if (this.closed) {
         throw new NbtApiException("Tried using closed NBT data!");
      } else if (!this.isReadOnly() || this.cachedCompound == null && !ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.bukkitItem.getClass())) {
         if (this.finalizer) {
            if (this.cachedCompound == null) {
               this.updateCachedCompound();
            }

            return this.cachedCompound;
         } else {
            return NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run((Object)null, this.bukkitItem));
         }
      } else {
         if (this.cachedCompound == null) {
            this.cachedCompound = NBTReflectionUtil.getItemRootNBTTagCompound(NBTReflectionUtil.getCraftItemHandle(this.bukkitItem));
         }

         return this.cachedCompound;
      }
   }

   private void updateCachedCompound() {
      if (this.finalizer) {
         this.cachedCompound = NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run((Object)null, this.bukkitItem));
      }

   }

   protected void finalizeChanges() {
      if (this.finalizer && this.cachedCompound != null) {
         if (NBTReflectionUtil.getKeys(this).isEmpty()) {
            this.cachedCompound = null;
         }

         if (ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.originalSrcStack.getClass())) {
            Object nmsStack = NBTReflectionUtil.getCraftItemHandle(this.originalSrcStack);
            NBTReflectionUtil.setItemStackCompound(nmsStack, this.cachedCompound);
            this.bukkitItem = this.originalSrcStack;
         } else {
            Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run((Object)null, this.bukkitItem);
            NBTReflectionUtil.setItemStackCompound(stack, this.cachedCompound);
            this.bukkitItem = (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run((Object)null, stack);
            this.originalSrcStack.setItemMeta(this.bukkitItem.getItemMeta());
         }

      }
   }

   protected void setClosed() {
      this.closed = true;
   }

   protected boolean isClosed() {
      return this.closed;
   }

   protected void setCompound(Object compound) {
      if (this.isReadOnly()) {
         throw new NbtApiException("Tried setting data in read only mode!");
      } else if (this.closed) {
         throw new NbtApiException("Tried using closed NBT data!");
      } else if (this.finalizer) {
         this.cachedCompound = compound;
      } else {
         if (compound != null && ((Set)ReflectionMethod.COMPOUND_GET_KEYS.run(compound)).isEmpty()) {
            compound = null;
         }

         if (ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.bukkitItem.getClass())) {
            Object nmsStack = NBTReflectionUtil.getCraftItemHandle(this.bukkitItem);
            NBTReflectionUtil.setItemStackCompound(nmsStack, compound);
         } else {
            Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run((Object)null, this.bukkitItem);
            NBTReflectionUtil.setItemStackCompound(stack, compound);
            this.bukkitItem = (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run((Object)null, stack);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public void applyNBT(ItemStack item) {
      if (item != null && item.getType() != Material.AIR) {
         NBTItem nbti = new NBTItem(new ItemStack(item.getType()));
         nbti.mergeCompound(this);
         item.setItemMeta(nbti.getItem().getItemMeta());
      } else {
         throw new NullPointerException("ItemStack can't be null/Air! This is not a NBTAPI bug!");
      }
   }

   /** @deprecated */
   @Deprecated
   public void mergeNBT(ItemStack item) {
      NBTItem nbti = new NBTItem(item);
      nbti.mergeCompound(this);
      item.setItemMeta(nbti.getItem().getItemMeta());
   }

   /** @deprecated */
   @Deprecated
   public void mergeCustomNBT(ItemStack item) {
      if (item != null && item.getType() != Material.AIR) {
         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            NBT.modify((ItemStack)item, (Consumer)((nbt) -> nbt.mergeCompound(this)));
         } else {
            ItemMeta meta = item.getItemMeta();
            NBTReflectionUtil.getUnhandledNBTTags(meta).putAll(NBTReflectionUtil.getUnhandledNBTTags(this.bukkitItem.getItemMeta()));
            item.setItemMeta(meta);
         }
      } else {
         throw new NullPointerException("ItemStack can't be null/Air!");
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean hasCustomNbtData() {
      if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
         return this.hasNBTData();
      } else {
         this.finalizeChanges();
         ItemMeta meta = this.bukkitItem.getItemMeta();
         return !NBTReflectionUtil.getUnhandledNBTTags(meta).isEmpty();
      }
   }

   /** @deprecated */
   @Deprecated
   public void clearCustomNBT() {
      this.finalizeChanges();
      if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
         this.setCompound((Object)null);
      } else {
         ItemMeta meta = this.bukkitItem.getItemMeta();
         NBTReflectionUtil.getUnhandledNBTTags(meta).clear();
         this.bukkitItem.setItemMeta(meta);
         this.updateCachedCompound();
      }
   }

   public ItemStack getItem() {
      return this.bukkitItem;
   }

   protected void setItem(ItemStack item) {
      this.bukkitItem = item;
   }

   public boolean hasNBTData() {
      return this.getCompound() != null;
   }

   public void modifyMeta(BiConsumer<ReadableNBT, ItemMeta> handler) {
      this.finalizeChanges();
      ItemMeta meta = this.bukkitItem.getItemMeta();
      handler.accept((new NBTContainer(this.getResolvedObject())).setReadOnly(true), meta);
      this.bukkitItem.setItemMeta(meta);
      this.updateCachedCompound();
      if (this.directApply) {
         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            throw new NbtApiException("Direct apply mode meta changes don't work anymore in 1.20.5+. Please switch to the modern NBT.modify sytnax!");
         }

         this.applyNBT(this.originalSrcStack);
      }

   }

   public <T extends ItemMeta> void modifyMeta(Class<T> type, BiConsumer<ReadableNBT, T> handler) {
      this.finalizeChanges();
      T meta = this.bukkitItem.getItemMeta();
      handler.accept((new NBTContainer(this.getResolvedObject())).setReadOnly(true), meta);
      this.bukkitItem.setItemMeta(meta);
      this.updateCachedCompound();
      if (this.directApply) {
         this.applyNBT(this.originalSrcStack);
      }

   }

   /** @deprecated */
   @Deprecated
   public static NBTContainer convertItemtoNBT(ItemStack item) {
      return NBTReflectionUtil.convertNMSItemtoNBTCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run((Object)null, item));
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public static ItemStack convertNBTtoItem(NBTCompound comp) {
      return (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run((Object)null, NBTReflectionUtil.convertNBTCompoundtoNMSItem(comp));
   }

   /** @deprecated */
   @Deprecated
   public static NBTContainer convertItemArraytoNBT(ItemStack[] items) {
      NBTContainer container = new NBTContainer();
      container.setInteger("size", items.length);
      NBTCompoundList list = container.getCompoundList("items");

      for(int i = 0; i < items.length; ++i) {
         ItemStack item = items[i];
         if (item != null && item.getType() != Material.AIR) {
            NBTListCompound entry = list.addCompound();
            entry.setInteger("Slot", i);
            entry.mergeCompound(convertItemtoNBT(item));
         }
      }

      return container;
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public static ItemStack[] convertNBTtoItemArray(NBTCompound comp) {
      if (!comp.hasTag("size")) {
         return null;
      } else {
         ItemStack[] rebuild = new ItemStack[comp.getInteger("size")];

         for(int i = 0; i < rebuild.length; ++i) {
            rebuild[i] = new ItemStack(Material.AIR);
         }

         if (!comp.hasTag("items")) {
            return rebuild;
         } else {
            for(ReadWriteNBT lcomp : comp.getCompoundList("items")) {
               if (lcomp instanceof NBTCompound) {
                  int slot = lcomp.getInteger("Slot");
                  rebuild[slot] = convertNBTtoItem((NBTCompound)lcomp);
               }
            }

            return rebuild;
         }
      }
   }

   protected void saveCompound() {
      if (this.directApply) {
         this.applyNBT(this.originalSrcStack);
      }

   }
}
