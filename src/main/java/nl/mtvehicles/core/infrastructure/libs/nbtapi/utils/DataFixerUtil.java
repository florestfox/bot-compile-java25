package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTCompound;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTReflectionUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class DataFixerUtil {
   public static final int VERSION1_12R1 = 1343;
   public static final int VERSION1_16R1 = 2586;
   public static final int VERSION1_17R1 = 2730;
   public static final int VERSION1_18R1 = 2975;
   public static final int VERSION1_19R1 = 3120;
   public static final int VERSION1_19R3 = 3337;
   public static final int VERSION1_20R1 = 3465;
   public static final int VERSION1_20R2 = 3578;
   public static final int VERSION1_20R3 = 3700;
   public static final int VERSION1_20R4 = 3837;
   public static final int VERSION1_21R1 = 3953;
   public static final int VERSION1_21R2 = 4080;
   public static final int VERSION1_21R3 = 4189;
   public static final int VERSION1_21R4 = 4323;
   public static final int VERSION1_21R5 = 4435;
   public static final int VERSION1_21R6 = 4554;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_12_2 = 1343;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_16_5 = 2586;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_17_1 = 2730;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_18_2 = 2975;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_19_2 = 3120;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_19_4 = 3337;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_20_1 = 3465;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_20_2 = 3578;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_20_4 = 3700;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_20_5 = 3837;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_21 = 3953;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_21_2 = 4080;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_21_3 = 4189;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_21_4 = 4323;
   /** @deprecated */
   @Deprecated
   public static final int VERSION1_21_5 = 4435;

   public static Object fixUpRawItemData(Object nbt, int fromVersion, int toVersion) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      DataFixer dataFixer = (DataFixer)ReflectionMethod.GET_DATAFIXER.run((Object)null);
      DSL.TypeReference itemStackReference = (DSL.TypeReference)ReflectionUtil.getMappedField(ClassWrapper.NMS_REFERENCES.getClazz(), "net.minecraft.util.datafix.fixes.References#ITEM_STACK").get((Object)null);
      DynamicOps<Object> nbtOps = (DynamicOps)ReflectionUtil.getMappedField(ClassWrapper.NMS_NBTOPS.getClazz(), "net.minecraft.nbt.NbtOps#INSTANCE").get((Object)null);
      Dynamic<Object> fixed = dataFixer.update(itemStackReference, new Dynamic(nbtOps, nbt), fromVersion, toVersion);
      return fixed.getValue();
   }

   public static ReadWriteNBT fixUpItemData(ReadWriteNBT nbt, int fromVersion, int toVersion) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      return NBT.wrapNMSTag(fixUpRawItemData(NBTReflectionUtil.getToCompount(((NBTCompound)nbt).getCompound(), (NBTCompound)nbt), fromVersion, toVersion));
   }

   public static int getCurrentVersion() {
      if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
         return 4554;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
         return 4435;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
         return 4323;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R3)) {
         return 4189;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R2)) {
         return 4080;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R1)) {
         return 3953;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
         return 3837;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R3)) {
         return 3700;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R1)) {
         return 3465;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_19_R3)) {
         return 3337;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_19_R1)) {
         return 3120;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1)) {
         return 2975;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1)) {
         return 2730;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1)) {
         return 2586;
      } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_12_R1)) {
         return 1343;
      } else {
         throw new NbtApiException("Trying to update data *to* a version before 1.12.2? Something is probably going wrong, contact the plugin author.");
      }
   }
}
