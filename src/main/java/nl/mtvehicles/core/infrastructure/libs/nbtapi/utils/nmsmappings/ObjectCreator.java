package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;

public enum ObjectCreator {
   NMS_NBTTAGCOMPOUND((MinecraftVersion)null, (MinecraftVersion)null, ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), new Class[0]),
   NMS_CUSTOMDATA(MinecraftVersion.MC1_20_R4, (MinecraftVersion)null, ClassWrapper.NMS_CUSTOMDATA.getClazz(), new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}),
   NMS_BLOCKPOSITION((MinecraftVersion)null, (MinecraftVersion)null, ClassWrapper.NMS_BLOCKPOSITION.getClazz(), new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}),
   NMS_COMPOUNDFROMITEM(MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_20_R3, ClassWrapper.NMS_ITEMSTACK.getClazz(), new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()});

   private Constructor<?> construct;
   private Class<?> targetClass;

   private ObjectCreator(MinecraftVersion from, MinecraftVersion to, Class<?> clazz, Class<?>... args) {
      if (clazz != null) {
         if (from == null || MinecraftVersion.getVersion().getVersionId() >= from.getVersionId()) {
            if (to == null || MinecraftVersion.getVersion().getVersionId() <= to.getVersionId()) {
               try {
                  this.targetClass = clazz;
                  this.construct = clazz.getDeclaredConstructor(args);
                  this.construct.setAccessible(true);
               } catch (Exception ex) {
                  MinecraftVersion.getLogger().log(Level.SEVERE, "Unable to find the constructor for the class '" + clazz.getName() + "'", ex);
               }

            }
         }
      }
   }

   public Object getInstance(Object... args) {
      try {
         return this.construct.newInstance(args);
      } catch (Exception ex) {
         throw new NbtApiException("Exception while creating a new instance of '" + this.targetClass + "'", ex);
      }
   }

   // $FF: synthetic method
   private static ObjectCreator[] $values() {
      return new ObjectCreator[]{NMS_NBTTAGCOMPOUND, NMS_CUSTOMDATA, NMS_BLOCKPOSITION, NMS_COMPOUNDFROMITEM};
   }
}
