package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.BadBytecode;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.Descriptor;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.SignatureAttribute;

public class ClassMemberValue extends MemberValue {
   int valueIndex;

   public ClassMemberValue(int index, ConstPool cp) {
      super('c', cp);
      this.valueIndex = index;
   }

   public ClassMemberValue(String className, ConstPool cp) {
      super('c', cp);
      this.setValue(className);
   }

   public ClassMemberValue(ConstPool cp) {
      super('c', cp);
      this.setValue("java.lang.Class");
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
      String classname = this.getValue();
      if (classname.equals("void")) {
         return Void.TYPE;
      } else if (classname.equals("int")) {
         return Integer.TYPE;
      } else if (classname.equals("byte")) {
         return Byte.TYPE;
      } else if (classname.equals("long")) {
         return Long.TYPE;
      } else if (classname.equals("double")) {
         return Double.TYPE;
      } else if (classname.equals("float")) {
         return Float.TYPE;
      } else if (classname.equals("char")) {
         return Character.TYPE;
      } else if (classname.equals("short")) {
         return Short.TYPE;
      } else {
         return classname.equals("boolean") ? Boolean.TYPE : loadClass(cl, classname);
      }
   }

   Class<?> getType(ClassLoader cl) throws ClassNotFoundException {
      return loadClass(cl, "java.lang.Class");
   }

   public void renameClass(String oldname, String newname) {
      String value = this.cp.getUtf8Info(this.valueIndex);
      String newValue = Descriptor.rename(value, oldname, newname);
      this.setValue(Descriptor.toClassName(newValue));
   }

   public void renameClass(Map<String, String> classnames) {
      String value = this.cp.getUtf8Info(this.valueIndex);
      String newValue = Descriptor.rename(value, classnames);
      this.setValue(Descriptor.toClassName(newValue));
   }

   public String getValue() {
      String v = this.cp.getUtf8Info(this.valueIndex);

      try {
         return SignatureAttribute.toTypeSignature(v).jvmTypeName();
      } catch (BadBytecode e) {
         throw new RuntimeException(e);
      }
   }

   public void setValue(String newClassName) {
      String setTo = Descriptor.of(newClassName);
      this.valueIndex = this.cp.addUtf8Info(setTo);
   }

   public String toString() {
      return this.getValue().replace('$', '.') + ".class";
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.classInfoIndex(this.cp.getUtf8Info(this.valueIndex));
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitClassMemberValue(this);
   }
}
