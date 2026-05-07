package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.CompileError;

public class StringL extends ASTree {
   private static final long serialVersionUID = 1L;
   protected String text;

   public StringL(String t) {
      this.text = t;
   }

   public String get() {
      return this.text;
   }

   public String toString() {
      return "\"" + this.text + "\"";
   }

   public void accept(Visitor v) throws CompileError {
      v.atStringL(this);
   }
}
