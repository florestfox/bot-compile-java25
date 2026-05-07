package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.expr;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtClass;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtConstructor;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtMethod;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.NotFoundException;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.CodeIterator;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.MethodInfo;

public class ConstructorCall extends MethodCall {
   protected ConstructorCall(int pos, CodeIterator i, CtClass decl, MethodInfo m) {
      super(pos, i, decl, m);
   }

   public String getMethodName() {
      return this.isSuper() ? "super" : "this";
   }

   public CtMethod getMethod() throws NotFoundException {
      throw new NotFoundException("this is a constructor call.  Call getConstructor().");
   }

   public CtConstructor getConstructor() throws NotFoundException {
      return this.getCtClass().getConstructor(this.getSignature());
   }

   public boolean isSuper() {
      return super.isSuper();
   }
}
