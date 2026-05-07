package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.convert;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtMethod;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.CodeIterator;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.Descriptor;

public class TransformCallToStatic extends TransformCall {
   public TransformCallToStatic(Transformer next, CtMethod origMethod, CtMethod substMethod) {
      super(next, origMethod, substMethod);
      this.methodDescriptor = origMethod.getMethodInfo2().getDescriptor();
   }

   protected int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) {
      if (this.newIndex == 0) {
         String desc = Descriptor.insertParameter(this.classname, this.methodDescriptor);
         int nt = cp.addNameAndTypeInfo(this.newMethodname, desc);
         int ci = cp.addClassInfo(this.newClassname);
         this.newIndex = cp.addMethodrefInfo(ci, nt);
         this.constPool = cp;
      }

      iterator.writeByte(184, pos);
      iterator.write16bit(this.newIndex, pos + 1);
      return pos;
   }
}
