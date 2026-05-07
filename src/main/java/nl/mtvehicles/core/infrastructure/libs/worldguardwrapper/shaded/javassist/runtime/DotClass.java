package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.runtime;

public class DotClass {
   public static NoClassDefFoundError fail(ClassNotFoundException e) {
      return new NoClassDefFoundError(e.getMessage());
   }
}
