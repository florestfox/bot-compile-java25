package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.io.InputStream;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ObjectCreator;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTContainer extends NBTCompound {
   private Object nbt;
   private boolean closed;
   private boolean readOnly;

   /** @deprecated */
   @Deprecated
   public NBTContainer() {
      super((NBTCompound)null, (String)null);
      this.nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
   }

   /** @deprecated */
   @Deprecated
   public NBTContainer(Object nbt) {
      super((NBTCompound)null, (String)null);
      if (nbt == null) {
         nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
      }

      if (!ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().isAssignableFrom(nbt.getClass())) {
         throw new NbtApiException("The object '" + nbt.getClass() + "' is not a valid NBT-Object!");
      } else {
         this.nbt = nbt;
      }
   }

   /** @deprecated */
   @Deprecated
   public NBTContainer(InputStream inputsteam) {
      super((NBTCompound)null, (String)null);
      this.nbt = NBTReflectionUtil.readNBT(inputsteam);
   }

   /** @deprecated */
   @Deprecated
   public NBTContainer(String nbtString) {
      super((NBTCompound)null, (String)null);
      if (nbtString == null) {
         throw new NullPointerException("The String can't be null!");
      } else {
         try {
            this.nbt = ReflectionMethod.PARSE_NBT.run((Object)null, nbtString);
         } catch (Exception ex) {
            throw new NbtApiException("Unable to parse Malformed Json!", ex);
         }
      }
   }

   public Object getCompound() {
      return this.nbt;
   }

   public void setCompound(Object tag) {
      this.nbt = tag;
   }

   protected void setClosed() {
      this.closed = true;
   }

   protected boolean isClosed() {
      return this.closed;
   }

   protected boolean isReadOnly() {
      return this.readOnly;
   }

   protected NBTContainer setReadOnly(boolean readOnly) {
      this.readOnly = true;
      return this;
   }
}
