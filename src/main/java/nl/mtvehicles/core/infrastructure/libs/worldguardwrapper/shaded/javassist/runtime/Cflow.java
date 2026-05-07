package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.runtime;

public class Cflow extends ThreadLocal<Depth> {
   protected synchronized Depth initialValue() {
      return new Depth();
   }

   public void enter() {
      ((Depth)this.get()).inc();
   }

   public void exit() {
      ((Depth)this.get()).dec();
   }

   public int value() {
      return ((Depth)this.get()).value();
   }

   protected static class Depth {
      private int depth = 0;

      Depth() {
      }

      int value() {
         return this.depth;
      }

      void inc() {
         ++this.depth;
      }

      void dec() {
         --this.depth;
      }
   }
}
