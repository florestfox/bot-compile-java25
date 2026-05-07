package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CannotCompileException;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ClassFile;

public class DefinePackageHelper {
   private static final Helper privileged;

   public static void definePackage(String className, ClassLoader loader) throws CannotCompileException {
      try {
         privileged.definePackage(loader, className, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
      } catch (IllegalArgumentException var3) {
      } catch (Exception e) {
         throw new CannotCompileException(e);
      }
   }

   private DefinePackageHelper() {
   }

   static {
      privileged = (Helper)(ClassFile.MAJOR_VERSION >= 53 ? new Java9() : (ClassFile.MAJOR_VERSION >= 51 ? new Java7() : new JavaOther()));
   }

   private abstract static class Helper {
      private Helper() {
      }

      abstract Package definePackage(ClassLoader var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, URL var9) throws IllegalArgumentException;
   }

   private static class Java9 extends Helper {
      private Java9() {
      }

      Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
         throw new RuntimeException("define package has been disabled for jigsaw");
      }
   }

   private static class Java7 extends Helper {
      private final SecurityActions stack;
      private final MethodHandle definePackage;

      private Java7() {
         this.stack = SecurityActions.stack;
         this.definePackage = this.getDefinePackageMethodHandle();
      }

      private MethodHandle getDefinePackageMethodHandle() {
         if (this.stack.getCallerClass() != this.getClass()) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               return SecurityActions.getMethodHandle(ClassLoader.class, "definePackage", new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class});
            } catch (NoSuchMethodException e) {
               throw new RuntimeException("cannot initialize", e);
            }
         }
      }

      Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
         if (this.stack.getCallerClass() != DefinePackageHelper.class) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               return (Package)this.definePackage.invokeWithArguments(loader, name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
            } catch (Throwable e) {
               if (e instanceof IllegalArgumentException) {
                  throw (IllegalArgumentException)e;
               } else if (e instanceof RuntimeException) {
                  throw (RuntimeException)e;
               } else {
                  return null;
               }
            }
         }
      }
   }

   private static class JavaOther extends Helper {
      private final SecurityActions stack;
      private final Method definePackage;

      private JavaOther() {
         this.stack = SecurityActions.stack;
         this.definePackage = this.getDefinePackageMethod();
      }

      private Method getDefinePackageMethod() {
         if (this.stack.getCallerClass() != this.getClass()) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               return SecurityActions.getDeclaredMethod(ClassLoader.class, "definePackage", new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class});
            } catch (NoSuchMethodException e) {
               throw new RuntimeException("cannot initialize", e);
            }
         }
      }

      Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
         if (this.stack.getCallerClass() != DefinePackageHelper.class) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               this.definePackage.setAccessible(true);
               return (Package)this.definePackage.invoke(loader, name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
            } catch (Throwable var12) {
               if (var12 instanceof InvocationTargetException) {
                  Throwable t = ((InvocationTargetException)var12).getTargetException();
                  if (t instanceof IllegalArgumentException) {
                     throw (IllegalArgumentException)t;
                  }
               }

               if (var12 instanceof RuntimeException) {
                  throw (RuntimeException)var12;
               } else {
                  return null;
               }
            }
         }
      }
   }
}
