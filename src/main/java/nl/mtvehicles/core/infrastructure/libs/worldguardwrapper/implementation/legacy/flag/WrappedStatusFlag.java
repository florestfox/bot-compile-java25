package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.flag;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedStatusFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;

public class WrappedStatusFlag extends AbstractWrappedFlag<WrappedState> implements IWrappedStatusFlag {
   public WrappedStatusFlag(Flag<StateFlag.State> handle) {
      super(handle);
   }

   public Optional<WrappedState> fromWGValue(Object value) {
      return Optional.ofNullable(value).map((state) -> state == State.ALLOW ? WrappedState.ALLOW : WrappedState.DENY);
   }

   public Optional<Object> fromWrapperValue(WrappedState value) {
      return Optional.ofNullable(value).map((state) -> state == WrappedState.ALLOW ? State.ALLOW : State.DENY);
   }
}
