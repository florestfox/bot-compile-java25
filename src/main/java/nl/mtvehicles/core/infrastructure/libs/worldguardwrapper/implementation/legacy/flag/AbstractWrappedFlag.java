package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.flag;

import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;

public abstract class AbstractWrappedFlag<T> implements IWrappedFlag<T> {
   private final Flag<?> handle;

   public String getName() {
      return this.handle.getName();
   }

   public abstract Optional<T> fromWGValue(Object var1);

   public abstract Optional<Object> fromWrapperValue(T var1);

   public Optional<T> getDefaultValue() {
      return this.fromWGValue(this.handle.getDefault());
   }

   public AbstractWrappedFlag(Flag<?> handle) {
      this.handle = handle;
   }

   public Flag<?> getHandle() {
      return this.handle;
   }
}
