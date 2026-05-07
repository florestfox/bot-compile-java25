package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings;

import java.util.logging.Level;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import org.bukkit.Bukkit;

public enum ClassWrapper {
   CRAFT_ITEMSTACK(PackageWrapper.CRAFTBUKKIT, "inventory.CraftItemStack", (MinecraftVersion)null, (MinecraftVersion)null),
   CRAFT_METAITEM(PackageWrapper.CRAFTBUKKIT, "inventory.CraftMetaItem", (MinecraftVersion)null, (MinecraftVersion)null),
   CRAFT_ENTITY(PackageWrapper.CRAFTBUKKIT, "entity.CraftEntity", (MinecraftVersion)null, (MinecraftVersion)null),
   CRAFT_WORLD(PackageWrapper.CRAFTBUKKIT, "CraftWorld", (MinecraftVersion)null, (MinecraftVersion)null),
   CRAFT_SERVER(PackageWrapper.CRAFTBUKKIT, "CraftServer", (MinecraftVersion)null, (MinecraftVersion)null),
   CRAFT_PERSISTENTDATACONTAINER(PackageWrapper.CRAFTBUKKIT, "persistence.CraftPersistentDataContainer", MinecraftVersion.MC1_14_R1, (MinecraftVersion)null),
   NMS_NBTBASE(PackageWrapper.NMS, "NBTBase", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.Tag"),
   NMS_TAGTYPE(PackageWrapper.NMS, "NBTTagType", MinecraftVersion.MC1_21_R4, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.TagType"),
   NMS_NBTTAGSTRING(PackageWrapper.NMS, "NBTTagString", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.StringTag"),
   NMS_NBTTAGINT(PackageWrapper.NMS, "NBTTagInt", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.IntTag"),
   NMS_NBTTAGINTARRAY(PackageWrapper.NMS, "NBTTagIntArray", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.IntArrayTag"),
   NMS_NBTTAGFLOAT(PackageWrapper.NMS, "NBTTagFloat", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.FloatTag"),
   NMS_NBTTAGDOUBLE(PackageWrapper.NMS, "NBTTagDouble", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.DoubleTag"),
   NMS_NBTTAGLONG(PackageWrapper.NMS, "NBTTagLong", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.LongTag"),
   NMS_ITEMSTACK(PackageWrapper.NMS, "ItemStack", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.world.item", "net.minecraft.world.item.ItemStack"),
   NMS_NBTTAGCOMPOUND(PackageWrapper.NMS, "NBTTagCompound", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.CompoundTag"),
   NMS_NBTTAGLIST(PackageWrapper.NMS, "NBTTagList", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.ListTag"),
   NMS_NBTCOMPRESSEDSTREAMTOOLS(PackageWrapper.NMS, "NBTCompressedStreamTools", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.NbtIo"),
   NMS_MOJANGSONPARSER(PackageWrapper.NMS, "MojangsonParser", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.TagParser"),
   NMS_TILEENTITY(PackageWrapper.NMS, "TileEntity", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.world.level.block.entity", "net.minecraft.world.level.block.entity.BlockEntity"),
   NMS_BLOCKPOSITION(PackageWrapper.NMS, "BlockPosition", MinecraftVersion.MC1_8_R3, (MinecraftVersion)null, "net.minecraft.core", "net.minecraft.core.BlockPos"),
   NMS_WORLDSERVER(PackageWrapper.NMS, "WorldServer", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.server.level", "net.minecraft.server.level.ServerLevel"),
   NMS_MINECRAFTSERVER(PackageWrapper.NMS, "MinecraftServer", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.server", "net.minecraft.server.MinecraftServer"),
   NMS_WORLD(PackageWrapper.NMS, "World", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.world.level", "net.minecraft.world.level.Level"),
   NMS_ENTITY(PackageWrapper.NMS, "Entity", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.world.entity", "net.minecraft.world.entity.Entity"),
   NMS_ENTITYTYPES(PackageWrapper.NMS, "EntityTypes", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.world.entity", "net.minecraft.world.entity.EntityType"),
   NMS_REGISTRYSIMPLE(PackageWrapper.NMS, "RegistrySimple", MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_12_R1),
   NMS_REGISTRYMATERIALS(PackageWrapper.NMS, "RegistryMaterials", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.core", "net.minecraft.core.MappedRegistry"),
   NMS_IREGISTRY(PackageWrapper.NMS, "IRegistry", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.core", "net.minecraft.core.Registry"),
   NMS_MINECRAFTKEY(PackageWrapper.NMS, "MinecraftKey", MinecraftVersion.MC1_8_R3, (MinecraftVersion)null, "net.minecraft.resources", "net.minecraft.resources.ResourceKey"),
   NMS_GAMEPROFILESERIALIZER(PackageWrapper.NMS, "GameProfileSerializer", (MinecraftVersion)null, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.NbtUtils"),
   NMS_IBLOCKDATA(PackageWrapper.NMS, "IBlockData", MinecraftVersion.MC1_8_R3, (MinecraftVersion)null, "net.minecraft.world.level.block.state", "net.minecraft.world.level.block.state.BlockState"),
   NMS_NBTACCOUNTER(PackageWrapper.NMS, "NBTReadLimiter", MinecraftVersion.MC1_20_R3, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.NbtAccounter"),
   NMS_CUSTOMDATA(PackageWrapper.NMS, "CustomData", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.world.item.component", "net.minecraft.world.item.component.CustomData"),
   NMS_DATACOMPONENTTYPE(PackageWrapper.NMS, "DataComponentType", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponentType"),
   NMS_DATACOMPONENTS(PackageWrapper.NMS, "DataComponents", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponents"),
   NMS_DATACOMPONENTHOLDER(PackageWrapper.NMS, "DataComponentHolder", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponentHolder"),
   NMS_PROVIDER(PackageWrapper.NMS, "HolderLookup$a", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.core", "net.minecraft.core.HolderLookup$Provider"),
   NMS_SERVER(PackageWrapper.NMS, "MinecraftServer", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.server", "net.minecraft.server.MinecraftServer"),
   NMS_DATAFIXERS(PackageWrapper.NMS, "DataConverterRegistry", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.util.datafix", "net.minecraft.util.datafix.DataFixers"),
   NMS_REFERENCES(PackageWrapper.NMS, "DataConverterTypes", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.util.datafix.fixes", "net.minecraft.util.datafix.fixes.References"),
   NMS_NBTOPS(PackageWrapper.NMS, "DynamicOpsNBT", MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, "net.minecraft.nbt", "net.minecraft.nbt.NbtOps"),
   NMS_PROBLEM_REPORTER(PackageWrapper.NMS, "ProblemReporter", MinecraftVersion.MC1_21_R5, (MinecraftVersion)null, "net.minecraft.util", "net.minecraft.util.ProblemReporter"),
   NMS_TAG_VALUE_INPUT(PackageWrapper.NMS, "TagValueInput", MinecraftVersion.MC1_21_R5, (MinecraftVersion)null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.TagValueInput"),
   NMS_VALUE_INPUT(PackageWrapper.NMS, "ValueInput", MinecraftVersion.MC1_21_R5, (MinecraftVersion)null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.ValueInput"),
   NMS_TAG_VALUE_OUTPUT(PackageWrapper.NMS, "TagValueOutput", MinecraftVersion.MC1_21_R5, (MinecraftVersion)null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.TagValueOutput"),
   NMS_VALUE_OUTPUT(PackageWrapper.NMS, "ValueOutput", MinecraftVersion.MC1_21_R5, (MinecraftVersion)null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.ValueOutput"),
   NMS_DYNAMICOPS(PackageWrapper.NONE, "DynamicOps", MinecraftVersion.MC1_21_R5, (MinecraftVersion)null, "com.mojang.serialization", "com.mojang.serialization.DynamicOps"),
   GAMEPROFILE(PackageWrapper.NONE, "com.mojang.authlib.GameProfile", MinecraftVersion.MC1_8_R3, (MinecraftVersion)null);

   private Class<?> clazz;
   private boolean enabled;
   private final String mojangName;

   private ClassWrapper(PackageWrapper packageId, String clazzName, MinecraftVersion from, MinecraftVersion to) {
      this(packageId, clazzName, from, to, (String)null, (String)null);
   }

   private ClassWrapper(PackageWrapper packageId, String clazzName, MinecraftVersion from, MinecraftVersion to, String mojangMap, String mojangName) {
      this.enabled = false;
      this.mojangName = mojangName;
      if (from == null || MinecraftVersion.getVersion().getVersionId() >= from.getVersionId()) {
         if (to == null || MinecraftVersion.getVersion().getVersionId() <= to.getVersionId()) {
            this.enabled = true;

            try {
               if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1) && mojangName != null) {
                  try {
                     this.clazz = Class.forName(mojangName);
                     return;
                  } catch (ClassNotFoundException var10) {
                  }
               }

               if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1) && mojangMap != null) {
                  this.clazz = Class.forName(mojangMap + "." + clazzName);
               } else if (packageId == PackageWrapper.NONE) {
                  this.clazz = Class.forName(clazzName);
               } else if (MinecraftVersion.isForgePresent() && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4 && Forge1710Mappings.getClassMappings().get(this.name()) != null) {
                  this.clazz = Class.forName((String)Forge1710Mappings.getClassMappings().get(this.name()));
               } else if (packageId == PackageWrapper.CRAFTBUKKIT) {
                  this.clazz = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + "." + clazzName);
               } else {
                  String version = MinecraftVersion.getVersion().getPackageName();
                  this.clazz = Class.forName(packageId.getUri() + "." + version + "." + clazzName);
               }
            } catch (Throwable ex) {
               MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error while trying to resolve the class '" + clazzName + "'!", ex);
            }

         }
      }
   }

   public Class<?> getClazz() {
      return this.clazz;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public String getMojangName() {
      return this.mojangName;
   }

   // $FF: synthetic method
   private static ClassWrapper[] $values() {
      return new ClassWrapper[]{CRAFT_ITEMSTACK, CRAFT_METAITEM, CRAFT_ENTITY, CRAFT_WORLD, CRAFT_SERVER, CRAFT_PERSISTENTDATACONTAINER, NMS_NBTBASE, NMS_TAGTYPE, NMS_NBTTAGSTRING, NMS_NBTTAGINT, NMS_NBTTAGINTARRAY, NMS_NBTTAGFLOAT, NMS_NBTTAGDOUBLE, NMS_NBTTAGLONG, NMS_ITEMSTACK, NMS_NBTTAGCOMPOUND, NMS_NBTTAGLIST, NMS_NBTCOMPRESSEDSTREAMTOOLS, NMS_MOJANGSONPARSER, NMS_TILEENTITY, NMS_BLOCKPOSITION, NMS_WORLDSERVER, NMS_MINECRAFTSERVER, NMS_WORLD, NMS_ENTITY, NMS_ENTITYTYPES, NMS_REGISTRYSIMPLE, NMS_REGISTRYMATERIALS, NMS_IREGISTRY, NMS_MINECRAFTKEY, NMS_GAMEPROFILESERIALIZER, NMS_IBLOCKDATA, NMS_NBTACCOUNTER, NMS_CUSTOMDATA, NMS_DATACOMPONENTTYPE, NMS_DATACOMPONENTS, NMS_DATACOMPONENTHOLDER, NMS_PROVIDER, NMS_SERVER, NMS_DATAFIXERS, NMS_REFERENCES, NMS_NBTOPS, NMS_PROBLEM_REPORTER, NMS_TAG_VALUE_INPUT, NMS_VALUE_INPUT, NMS_TAG_VALUE_OUTPUT, NMS_VALUE_OUTPUT, NMS_DYNAMICOPS, GAMEPROFILE};
   }
}
