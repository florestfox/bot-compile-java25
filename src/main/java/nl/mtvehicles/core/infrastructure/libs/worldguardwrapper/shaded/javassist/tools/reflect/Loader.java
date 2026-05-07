package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.tools.reflect;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CannotCompileException;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.NotFoundException;

public class Loader extends nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.Loader {
   protected Reflection reflection;

   public static void main(String[] args) throws Throwable {
      Loader cl = new Loader();
      cl.run(args);
   }

   public Loader() throws CannotCompileException, NotFoundException {
      this.delegateLoadingOf("nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.tools.reflect.Loader");
      this.reflection = new Reflection();
      ClassPool pool = ClassPool.getDefault();
      this.addTranslator(pool, this.reflection);
   }

   public boolean makeReflective(String clazz, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
      return this.reflection.makeReflective(clazz, metaobject, metaclass);
   }
}
