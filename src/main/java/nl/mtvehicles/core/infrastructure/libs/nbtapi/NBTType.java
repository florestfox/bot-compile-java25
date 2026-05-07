package nl.mtvehicles.core.infrastructure.libs.nbtapi;

public enum NBTType {
   NBTTagEnd(0, ""),
   NBTTagByte(1, "BYTE"),
   NBTTagShort(2, "SHORT"),
   NBTTagInt(3, "INT"),
   NBTTagLong(4, "LONG"),
   NBTTagFloat(5, "FLOAT"),
   NBTTagDouble(6, "DOUBLE"),
   NBTTagByteArray(7, "BYTE[]"),
   NBTTagString(8, "STRING"),
   NBTTagList(9, "LIST"),
   NBTTagCompound(10, "COMPOUND"),
   NBTTagIntArray(11, "INT[]"),
   NBTTagLongArray(12, "LONG[]");

   private final int id;
   private final String name;

   private NBTType(int i, String name) {
      this.id = i;
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public static NBTType valueOf(int id) {
      for(NBTType t : values()) {
         if (t.getId() == id) {
            return t;
         }
      }

      return NBTTagEnd;
   }

   public static NBTType fromName(String name) {
      for(NBTType t : values()) {
         if (t.getName().equals(name)) {
            return t;
         }
      }

      return NBTTagEnd;
   }

   // $FF: synthetic method
   private static NBTType[] $values() {
      return new NBTType[]{NBTTagEnd, NBTTagByte, NBTTagShort, NBTTagInt, NBTTagLong, NBTTagFloat, NBTTagDouble, NBTTagByteArray, NBTTagString, NBTTagList, NBTTagCompound, NBTTagIntArray, NBTTagLongArray};
   }
}
