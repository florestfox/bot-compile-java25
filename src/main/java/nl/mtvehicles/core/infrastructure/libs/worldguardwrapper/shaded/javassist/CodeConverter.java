package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.BadBytecode;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.CodeAttribute;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.CodeIterator;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.MethodInfo;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformAccessArrayField;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformAfter;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformBefore;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformCall;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformCallToStatic;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformFieldAccess;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformNew;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformNewClass;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformReadField;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.TransformWriteField;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert.Transformer;

public class CodeConverter {
   protected Transformer transformers = null;

   public void replaceNew(CtClass newClass, CtClass calledClass, String calledMethod) {
      this.transformers = new TransformNew(this.transformers, newClass.getName(), calledClass.getName(), calledMethod);
   }

   public void replaceNew(CtClass oldClass, CtClass newClass) {
      this.transformers = new TransformNewClass(this.transformers, oldClass.getName(), newClass.getName());
   }

   public void redirectFieldAccess(CtField field, CtClass newClass, String newFieldname) {
      this.transformers = new TransformFieldAccess(this.transformers, field, newClass.getName(), newFieldname);
   }

   public void replaceFieldRead(CtField field, CtClass calledClass, String calledMethod) {
      this.transformers = new TransformReadField(this.transformers, field, calledClass.getName(), calledMethod);
   }

   public void replaceFieldWrite(CtField field, CtClass calledClass, String calledMethod) {
      this.transformers = new TransformWriteField(this.transformers, field, calledClass.getName(), calledMethod);
   }

   public void replaceArrayAccess(CtClass calledClass, ArrayAccessReplacementMethodNames names) throws NotFoundException {
      this.transformers = new TransformAccessArrayField(this.transformers, calledClass.getName(), names);
   }

   public void redirectMethodCall(CtMethod origMethod, CtMethod substMethod) throws CannotCompileException {
      String d1 = origMethod.getMethodInfo2().getDescriptor();
      String d2 = substMethod.getMethodInfo2().getDescriptor();
      if (!d1.equals(d2)) {
         throw new CannotCompileException("signature mismatch: " + substMethod.getLongName());
      } else {
         int mod1 = origMethod.getModifiers();
         int mod2 = substMethod.getModifiers();
         if (Modifier.isStatic(mod1) == Modifier.isStatic(mod2) && (!Modifier.isPrivate(mod1) || Modifier.isPrivate(mod2)) && origMethod.getDeclaringClass().isInterface() == substMethod.getDeclaringClass().isInterface()) {
            this.transformers = new TransformCall(this.transformers, origMethod, substMethod);
         } else {
            throw new CannotCompileException("invoke-type mismatch " + substMethod.getLongName());
         }
      }
   }

   public void redirectMethodCall(String oldMethodName, CtMethod newMethod) throws CannotCompileException {
      this.transformers = new TransformCall(this.transformers, oldMethodName, newMethod);
   }

   public void redirectMethodCallToStatic(CtMethod origMethod, CtMethod staticMethod) {
      this.transformers = new TransformCallToStatic(this.transformers, origMethod, staticMethod);
   }

   public void insertBeforeMethod(CtMethod origMethod, CtMethod beforeMethod) throws CannotCompileException {
      try {
         this.transformers = new TransformBefore(this.transformers, origMethod, beforeMethod);
      } catch (NotFoundException e) {
         throw new CannotCompileException(e);
      }
   }

   public void insertAfterMethod(CtMethod origMethod, CtMethod afterMethod) throws CannotCompileException {
      try {
         this.transformers = new TransformAfter(this.transformers, origMethod, afterMethod);
      } catch (NotFoundException e) {
         throw new CannotCompileException(e);
      }
   }

   protected void doit(CtClass clazz, MethodInfo minfo, ConstPool cp) throws CannotCompileException {
      CodeAttribute codeAttr = minfo.getCodeAttribute();
      if (codeAttr != null && this.transformers != null) {
         for(Transformer t = this.transformers; t != null; t = t.getNext()) {
            t.initialize(cp, clazz, minfo);
         }

         CodeIterator iterator = codeAttr.iterator();

         while(iterator.hasNext()) {
            try {
               int pos = iterator.next();

               for(Transformer var12 = this.transformers; var12 != null; var12 = var12.getNext()) {
                  pos = var12.transform(clazz, pos, iterator, cp);
               }
            } catch (BadBytecode e) {
               throw new CannotCompileException(e);
            }
         }

         int locals = 0;
         int stack = 0;

         for(Transformer var13 = this.transformers; var13 != null; var13 = var13.getNext()) {
            int s = var13.extraLocals();
            if (s > locals) {
               locals = s;
            }

            s = var13.extraStack();
            if (s > stack) {
               stack = s;
            }
         }

         for(Transformer var14 = this.transformers; var14 != null; var14 = var14.getNext()) {
            var14.clean();
         }

         if (locals > 0) {
            codeAttr.setMaxLocals(codeAttr.getMaxLocals() + locals);
         }

         if (stack > 0) {
            codeAttr.setMaxStack(codeAttr.getMaxStack() + stack);
         }

         try {
            minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz.getClassFile2());
         } catch (BadBytecode b) {
            throw new CannotCompileException(b.getMessage(), b);
         }
      }
   }

   public static class DefaultArrayAccessReplacementMethodNames implements ArrayAccessReplacementMethodNames {
      public String byteOrBooleanRead() {
         return "arrayReadByteOrBoolean";
      }

      public String byteOrBooleanWrite() {
         return "arrayWriteByteOrBoolean";
      }

      public String charRead() {
         return "arrayReadChar";
      }

      public String charWrite() {
         return "arrayWriteChar";
      }

      public String doubleRead() {
         return "arrayReadDouble";
      }

      public String doubleWrite() {
         return "arrayWriteDouble";
      }

      public String floatRead() {
         return "arrayReadFloat";
      }

      public String floatWrite() {
         return "arrayWriteFloat";
      }

      public String intRead() {
         return "arrayReadInt";
      }

      public String intWrite() {
         return "arrayWriteInt";
      }

      public String longRead() {
         return "arrayReadLong";
      }

      public String longWrite() {
         return "arrayWriteLong";
      }

      public String objectRead() {
         return "arrayReadObject";
      }

      public String objectWrite() {
         return "arrayWriteObject";
      }

      public String shortRead() {
         return "arrayReadShort";
      }

      public String shortWrite() {
         return "arrayWriteShort";
      }
   }

   public interface ArrayAccessReplacementMethodNames {
      String byteOrBooleanRead();

      String byteOrBooleanWrite();

      String charRead();

      String charWrite();

      String doubleRead();

      String doubleWrite();

      String floatRead();

      String floatWrite();

      String intRead();

      String intWrite();

      String longRead();

      String longWrite();

      String objectRead();

      String objectWrite();

      String shortRead();

      String shortWrite();
   }
}
