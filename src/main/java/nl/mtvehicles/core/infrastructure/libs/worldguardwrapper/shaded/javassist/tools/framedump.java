package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.tools;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtClass;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.analysis.FramePrinter;

public class framedump {
   private framedump() {
   }

   public static void main(String[] args) throws Exception {
      if (args.length != 1) {
         System.err.println("Usage: java javassist.tools.framedump <fully-qualified class name>");
      } else {
         ClassPool pool = ClassPool.getDefault();
         CtClass clazz = pool.get(args[0]);
         System.out.println("Frame Dump of " + clazz.getName() + ":");
         FramePrinter.print(clazz, System.out);
      }
   }
}
