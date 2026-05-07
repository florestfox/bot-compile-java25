package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.MojangToMapping;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.inventory.ItemStack;

public class NBTJsonUtil {
   public static JsonElement itemStackToJson(ItemStack itemStack) {
      try {
         Codec<Object> itemStackCodec = (Codec)ClassWrapper.NMS_ITEMSTACK.getClazz().getField((String)MojangToMapping.getMapping().get("net.minecraft.world.item.ItemStack#CODEC")).get((Object)null);
         Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run((Object)null, itemStack);
         DataResult<JsonElement> result = itemStackCodec.encode(stack, JsonOps.INSTANCE, (JsonElement)JsonOps.INSTANCE.emptyMap());
         Optional<JsonElement> opt = (Optional)result.getClass().getMethod("result").invoke(result);
         return (JsonElement)opt.orElse((Object)null);
      } catch (Exception ex) {
         throw new NbtApiException("Error trying to get Json of an ItemStack.", ex);
      }
   }
}
