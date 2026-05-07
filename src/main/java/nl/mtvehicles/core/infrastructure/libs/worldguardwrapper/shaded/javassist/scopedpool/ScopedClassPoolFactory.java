package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.scopedpool;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;

public interface ScopedClassPoolFactory {
   ScopedClassPool create(ClassLoader var1, ClassPool var2, ScopedClassPoolRepository var3);

   ScopedClassPool create(ClassPool var1, ScopedClassPoolRepository var2);
}
