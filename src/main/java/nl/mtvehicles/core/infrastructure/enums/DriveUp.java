package nl.mtvehicles.core.infrastructure.enums;

public enum DriveUp {
   SLABS,
   BLOCKS,
   BOTH;

   public boolean isSlabs() {
      return this.equals(SLABS);
   }

   public boolean isBlocks() {
      return this.equals(BLOCKS);
   }

   public boolean isBoth() {
      return this.equals(BOTH);
   }

   // $FF: synthetic method
   private static DriveUp[] $values() {
      return new DriveUp[]{SLABS, BLOCKS, BOTH};
   }
}
