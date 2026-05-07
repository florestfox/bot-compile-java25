package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag;

import java.util.Optional;

public interface IWrappedFlag<T> {
   String getName();

   Optional<T> getDefaultValue();
}
