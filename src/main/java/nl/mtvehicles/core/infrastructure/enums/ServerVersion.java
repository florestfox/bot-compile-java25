package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import org.jetbrains.annotations.NotNull;

@VersionSpecific
public enum ServerVersion {
   v1_12,
   v1_13,
   v1_15,
   v1_16,
   v1_17,
   v1_18_R1,
   v1_18_R2,
   v1_19_R1,
   v1_19_R2,
   v1_19_R3,
   v1_20_R1,
   v1_20_R2,
   v1_20_R3,
   v1_20_R4,
   v1_21_R1,
   v1_21_R2,
   v1_21_R3,
   v1_21_R4,
   v1_21_R5,
   v1_21_R6;

   public boolean is1_12() {
      return this.equals(v1_12);
   }

   public boolean is1_13() {
      return this.equals(v1_13);
   }

   public boolean is1_15() {
      return this.equals(v1_15);
   }

   public boolean is1_16() {
      return this.equals(v1_16);
   }

   public boolean is1_17() {
      return this.equals(v1_17);
   }

   public boolean is1_18_R1() {
      return this.equals(v1_18_R1);
   }

   public boolean is1_18_R2() {
      return this.equals(v1_18_R2);
   }

   public boolean is1_19() {
      return this.equals(v1_19_R1);
   }

   public boolean is1_19_R2() {
      return this.equals(v1_19_R2);
   }

   public boolean is1_19_R3() {
      return this.equals(v1_19_R3);
   }

   public boolean is1_20_R1() {
      return this.equals(v1_20_R1);
   }

   public boolean is1_20_R2() {
      return this.equals(v1_20_R2);
   }

   public boolean is1_20_R3() {
      return this.equals(v1_20_R3);
   }

   public boolean is1_20_R4() {
      return this.equals(v1_20_R4);
   }

   public boolean is1_21_R1() {
      return this.equals(v1_21_R1);
   }

   public boolean is1_21_R2() {
      return this.equals(v1_21_R2);
   }

   public boolean is1_21_R3() {
      return this.equals(v1_21_R3);
   }

   public boolean is1_21_R4() {
      return this.equals(v1_21_R4);
   }

   public boolean is1_21_R5() {
      return this.equals(v1_21_R5);
   }

   public boolean is1_21_R6() {
      return this.equals(v1_21_R6);
   }

   public boolean isOlderThan(@NotNull ServerVersion version) {
      return this.ordinal() < version.ordinal();
   }

   public boolean isOlderOrEqualTo(@NotNull ServerVersion version) {
      return this.ordinal() <= version.ordinal();
   }

   public boolean isNewerThan(@NotNull ServerVersion version) {
      return this.ordinal() > version.ordinal();
   }

   public boolean isNewerOrEqualTo(@NotNull ServerVersion version) {
      return this.ordinal() >= version.ordinal();
   }

   // $FF: synthetic method
   private static ServerVersion[] $values() {
      return new ServerVersion[]{v1_12, v1_13, v1_15, v1_16, v1_17, v1_18_R1, v1_18_R2, v1_19_R1, v1_19_R2, v1_19_R3, v1_20_R1, v1_20_R2, v1_20_R3, v1_20_R4, v1_21_R1, v1_21_R2, v1_21_R3, v1_21_R4, v1_21_R5, v1_21_R6};
   }
}
