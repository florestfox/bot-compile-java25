package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.expr;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CannotCompileException;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtBehavior;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtClass;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.NotFoundException;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.BadBytecode;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.Bytecode;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.CodeAttribute;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.CodeIterator;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.MethodInfo;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.CompileError;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.Javac;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.JvstCodeGen;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.JvstTypeChecker;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ProceedHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.ASTList;

public class Instanceof extends Expr {
   protected Instanceof(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
      super(pos, i, declaring, m);
   }

   public CtBehavior where() {
      return super.where();
   }

   public int getLineNumber() {
      return super.getLineNumber();
   }

   public String getFileName() {
      return super.getFileName();
   }

   public CtClass getType() throws NotFoundException {
      ConstPool cp = this.getConstPool();
      int pos = this.currentPos;
      int index = this.iterator.u16bitAt(pos + 1);
      String name = cp.getClassInfo(index);
      return this.thisClass.getClassPool().getCtClass(name);
   }

   public CtClass[] mayThrow() {
      return super.mayThrow();
   }

   public void replace(String statement) throws CannotCompileException {
      this.thisClass.getClassFile();
      ConstPool constPool = this.getConstPool();
      int pos = this.currentPos;
      int index = this.iterator.u16bitAt(pos + 1);
      Javac jc = new Javac(this.thisClass);
      ClassPool cp = this.thisClass.getClassPool();
      CodeAttribute ca = this.iterator.get();

      try {
         CtClass[] params = new CtClass[]{cp.get("java.lang.Object")};
         CtClass retType = CtClass.booleanType;
         int paramVar = ca.getMaxLocals();
         jc.recordParams("java.lang.Object", params, true, paramVar, this.withinStatic());
         int retVar = jc.recordReturnType(retType, true);
         jc.recordProceed(new ProceedForInstanceof(index));
         jc.recordType(this.getType());
         checkResultValue(retType, statement);
         Bytecode bytecode = jc.getBytecode();
         storeStack(params, true, paramVar, bytecode);
         jc.recordLocalVariables(ca, pos);
         bytecode.addConstZero(retType);
         bytecode.addStore(retVar, retType);
         jc.compileStmnt(statement);
         bytecode.addLoad(retVar, retType);
         this.replace0(pos, bytecode, 3);
      } catch (CompileError e) {
         throw new CannotCompileException(e);
      } catch (NotFoundException e) {
         throw new CannotCompileException(e);
      } catch (BadBytecode var15) {
         throw new CannotCompileException("broken method");
      }
   }

   static class ProceedForInstanceof implements ProceedHandler {
      int index;

      ProceedForInstanceof(int i) {
         this.index = i;
      }

      public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
         if (gen.getMethodArgsLength(args) != 1) {
            throw new CompileError("$proceed() cannot take more than one parameter for instanceof");
         } else {
            gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
            bytecode.addOpcode(193);
            bytecode.addIndex(this.index);
            gen.setType(CtClass.booleanType);
         }
      }

      public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
         c.atMethodArgs(args, new int[1], new int[1], new String[1]);
         c.setType(CtClass.booleanType);
      }
   }
}
