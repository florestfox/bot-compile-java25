package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import com.mojang.authlib.GameProfile;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.GameprofileUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ObjectCreator;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTGameProfile {
   /** @deprecated */
   @Deprecated
   public static NBTCompound toNBT(GameProfile profile) {
      return (NBTCompound)(MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? (NBTCompound)GameprofileUtil.writeGameProfile(NBT.createNBTObject(), profile) : new NBTContainer(ReflectionMethod.GAMEPROFILE_SERIALIZE.run((Object)null, ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(), profile)));
   }

   /** @deprecated */
   @Deprecated
   public static GameProfile fromNBT(NBTCompound compound) {
      return MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? GameprofileUtil.readGameProfile(compound) : (GameProfile)ReflectionMethod.GAMEPROFILE_DESERIALIZE.run((Object)null, NBTReflectionUtil.getToCompount(compound.getCompound(), compound));
   }
}
