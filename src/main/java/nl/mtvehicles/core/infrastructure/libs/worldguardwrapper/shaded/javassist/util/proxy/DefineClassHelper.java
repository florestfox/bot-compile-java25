package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CannotCompileException;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ClassFile;

public class DefineClassHelper {
   private static final Helper privileged;

   public static Class<?> toClass(String className, Class<?> neighbor, ClassLoader loader, ProtectionDomain domain, byte[] bcode) throws CannotCompileException {
      try {
         return privileged.defineClass(className, bcode, 0, bcode.length, neighbor, loader, domain);
      } catch (RuntimeException e) {
         throw e;
      } catch (CannotCompileException e) {
         throw e;
      } catch (ClassFormatError e) {
         Throwable t = e.getCause();
         throw new CannotCompileException((Throwable)(t == null ? e : t));
      } catch (Exception e) {
         throw new CannotCompileException(e);
      }
   }

   public static Class<?> toClass(Class<?> neighbor, byte[] bcode) throws CannotCompileException {
      try {
         DefineClassHelper.class.getModule().addReads(neighbor.getModule());
         MethodHandles.Lookup lookup = MethodHandles.lookup();
         MethodHandles.Lookup prvlookup = MethodHandles.privateLookupIn(neighbor, lookup);
         return prvlookup.defineClass(bcode);
      } catch (IllegalArgumentException | IllegalAccessException e) {
         throw new CannotCompileException(((Exception)e).getMessage() + ": " + neighbor.getName() + " has no permission to define the class");
      }
   }

   public static Class<?> toClass(MethodHandles.Lookup lookup, byte[] bcode) throws CannotCompileException {
      try {
         return lookup.defineClass(bcode);
      } catch (IllegalArgumentException | IllegalAccessException e) {
         throw new CannotCompileException(((Exception)e).getMessage());
      }
   }

   static Class<?> toPublicClass(String className, byte[] bcode) throws CannotCompileException {
      try {
         MethodHandles.Lookup lookup = MethodHandles.lookup();
         lookup = lookup.dropLookupMode(2);
         return lookup.defineClass(bcode);
      } catch (Throwable t) {
         throw new CannotCompileException(t);
      }
   }

   private DefineClassHelper() {
   }

   static {
      privileged = (Helper)(ClassFile.MAJOR_VERSION > 54 ? new Java11() : (ClassFile.MAJOR_VERSION >= 53 ? new Java9() : (ClassFile.MAJOR_VERSION >= 51 ? new Java7() : new JavaOther())));
   }

   private abstract static class Helper {
      private Helper() {
      }

      abstract Class<?> defineClass(String var1, byte[] var2, int var3, int var4, Class<?> var5, ClassLoader var6, ProtectionDomain var7) throws ClassFormatError, CannotCompileException;
   }

   private static class Java11 extends JavaOther {
      private Java11() {
      }

      Class<?> defineClass(String name, byte[] bcode, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
         return neighbor != null ? DefineClassHelper.toClass(neighbor, bcode) : super.defineClass(name, bcode, off, len, neighbor, loader, protectionDomain);
      }
   }

   private static class Java9 extends Helper {
      private final Object stack;
      private final Method getCallerClass;
      private final ReferencedUnsafe sunMiscUnsafe = this.getReferencedUnsafe();

      Java9() {
         Class<?> stackWalkerClass = null;

         try {
            stackWalkerClass = Class.forName("java.lang.StackWalker");
         } catch (ClassNotFoundException var4) {
         }

         if (stackWalkerClass != null) {
            try {
               Class<?> optionClass = Class.forName("java.lang.StackWalker$Option");
               this.stack = stackWalkerClass.getMethod("getInstance", optionClass).invoke((Object)null, optionClass.getEnumConstants()[0]);
               this.getCallerClass = stackWalkerClass.getMethod("getCallerClass");
            } catch (Throwable e) {
               throw new RuntimeException("cannot initialize", e);
            }
         } else {
            this.stack = null;
            this.getCallerClass = null;
         }

      }

      private final ReferencedUnsafe getReferencedUnsafe() {
         try {
            if (DefineClassHelper.privileged != null && this.getCallerClass.invoke(this.stack) != this.getClass()) {
               throw new IllegalAccessError("Access denied for caller.");
            }
         } catch (Exception e) {
            throw new RuntimeException("cannot initialize", e);
         }

         try {
            SecurityActions.TheUnsafe usf = SecurityActions.getSunMiscUnsafeAnonymously();
            List<Method> defineClassMethod = (List)usf.methods.get("defineClass");
            if (null == defineClassMethod) {
               return null;
            } else {
               MethodHandle meth = MethodHandles.lookup().unreflect((Method)defineClassMethod.get(0));
               return new ReferencedUnsafe(usf, meth);
            }
         } catch (Throwable e) {
            throw new RuntimeException("cannot initialize", e);
         }
      }

      Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
         try {
            if (this.getCallerClass.invoke(this.stack) != DefineClassHelper.class) {
               throw new IllegalAccessError("Access denied for caller.");
            }
         } catch (Exception e) {
            throw new RuntimeException("cannot initialize", e);
         }

         return this.sunMiscUnsafe.defineClass(name, b, off, len, loader, protectionDomain);
      }

      final class ReferencedUnsafe {
         private final SecurityActions.TheUnsafe sunMiscUnsafeTheUnsafe;
         private final MethodHandle defineClass;

         ReferencedUnsafe(SecurityActions.TheUnsafe usf, MethodHandle meth) {
            this.sunMiscUnsafeTheUnsafe = usf;
            this.defineClass = meth;
         }

         Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            try {
               if (Java9.this.getCallerClass.invoke(Java9.this.stack) != Java9.class) {
                  throw new IllegalAccessError("Access denied for caller.");
               }
            } catch (Exception e) {
               throw new RuntimeException("cannot initialize", e);
            }

            try {
               return (Class)this.defineClass.invokeWithArguments(this.sunMiscUnsafeTheUnsafe.theUnsafe, name, b, off, len, loader, protectionDomain);
            } catch (Throwable e) {
               if (e instanceof RuntimeException) {
                  throw (RuntimeException)e;
               } else if (e instanceof ClassFormatError) {
                  throw (ClassFormatError)e;
               } else {
                  throw new ClassFormatError(e.getMessage());
               }
            }
         }
      }
   }

   private static class Java7 extends Helper {
      private final SecurityActions stack;
      private final MethodHandle defineClass;

      private Java7() {
         this.stack = SecurityActions.stack;
         this.defineClass = this.getDefineClassMethodHandle();
      }

      private final MethodHandle getDefineClassMethodHandle() {
         if (DefineClassHelper.privileged != null && this.stack.getCallerClass() != this.getClass()) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               return SecurityActions.getMethodHandle(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
            } catch (NoSuchMethodException e) {
               throw new RuntimeException("cannot initialize", e);
            }
         }
      }

      Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
         if (this.stack.getCallerClass() != DefineClassHelper.class) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               return (Class)this.defineClass.invokeWithArguments(loader, name, b, off, len, protectionDomain);
            } catch (Throwable e) {
               if (e instanceof RuntimeException) {
                  throw (RuntimeException)e;
               } else if (e instanceof ClassFormatError) {
                  throw (ClassFormatError)e;
               } else {
                  throw new ClassFormatError(e.getMessage());
               }
            }
         }
      }
   }

   private static class JavaOther extends Helper {
      private final Method defineClass;
      private final SecurityActions stack;

      private JavaOther() {
         this.defineClass = this.getDefineClassMethod();
         this.stack = SecurityActions.stack;
      }

      private final Method getDefineClassMethod() {
         if (DefineClassHelper.privileged != null && this.stack.getCallerClass() != this.getClass()) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               return SecurityActions.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
            } catch (NoSuchMethodException e) {
               throw new RuntimeException("cannot initialize", e);
            }
         }
      }

      Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
         Class<?> klass = this.stack.getCallerClass();
         if (klass != DefineClassHelper.class && klass != this.getClass()) {
            throw new IllegalAccessError("Access denied for caller.");
         } else {
            try {
               SecurityActions.setAccessible(this.defineClass, true);
               return (Class)this.defineClass.invoke(loader, name, b, off, len, protectionDomain);
            } catch (Throwable e) {
               if (e instanceof ClassFormatError) {
                  throw (ClassFormatError)e;
               } else if (e instanceof RuntimeException) {
                  throw (RuntimeException)e;
               } else {
                  throw new CannotCompileException(e);
               }
            }
         }
      }
   }
}
