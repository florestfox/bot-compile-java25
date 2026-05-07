package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.stackmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.ClassPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.CtClass;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.NotFoundException;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.BadBytecode;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.ConstPool;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.Descriptor;

public abstract class TypeData {
   public static TypeData[] make(int size) {
      TypeData[] array = new TypeData[size];

      for(int i = 0; i < size; ++i) {
         array[i] = TypeTag.TOP;
      }

      return array;
   }

   protected TypeData() {
   }

   private static void setType(TypeData td, String className, ClassPool cp) throws BadBytecode {
      td.setType(className, cp);
   }

   public abstract int getTypeTag();

   public abstract int getTypeData(ConstPool var1);

   public TypeData join() {
      return new TypeVar(this);
   }

   public abstract BasicType isBasicType();

   public abstract boolean is2WordType();

   public boolean isNullType() {
      return false;
   }

   public boolean isUninit() {
      return false;
   }

   public abstract boolean eq(TypeData var1);

   public abstract String getName();

   public abstract void setType(String var1, ClassPool var2) throws BadBytecode;

   public abstract TypeData getArrayType(int var1) throws NotFoundException;

   public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
      return index;
   }

   protected TypeVar toTypeVar(int dim) {
      return null;
   }

   public void constructorCalled(int offset) {
   }

   public String toString() {
      return super.toString() + "(" + this.toString2(new HashSet()) + ")";
   }

   abstract String toString2(Set<TypeData> var1);

   public static CtClass commonSuperClassEx(CtClass one, CtClass two) throws NotFoundException {
      if (one == two) {
         return one;
      } else if (one.isArray() && two.isArray()) {
         CtClass ele1 = one.getComponentType();
         CtClass ele2 = two.getComponentType();
         CtClass element = commonSuperClassEx(ele1, ele2);
         if (element == ele1) {
            return one;
         } else {
            return element == ele2 ? two : one.getClassPool().get(element == null ? "java.lang.Object" : element.getName() + "[]");
         }
      } else if (!one.isPrimitive() && !two.isPrimitive()) {
         return !one.isArray() && !two.isArray() ? commonSuperClass(one, two) : one.getClassPool().get("java.lang.Object");
      } else {
         return null;
      }
   }

   public static CtClass commonSuperClass(CtClass one, CtClass two) throws NotFoundException {
      CtClass deep = one;
      CtClass shallow = two;
      CtClass backupDeep = one;

      while(true) {
         if (eq(deep, shallow) && deep.getSuperclass() != null) {
            return deep;
         }

         CtClass deepSuper = deep.getSuperclass();
         CtClass shallowSuper = shallow.getSuperclass();
         if (shallowSuper == null) {
            shallow = two;
            break;
         }

         if (deepSuper == null) {
            backupDeep = two;
            deep = shallow;
            shallow = one;
            break;
         }

         deep = deepSuper;
         shallow = shallowSuper;
      }

      while(true) {
         deep = deep.getSuperclass();
         if (deep == null) {
            for(deep = backupDeep; !eq(deep, shallow); shallow = shallow.getSuperclass()) {
               deep = deep.getSuperclass();
            }

            return deep;
         }

         backupDeep = backupDeep.getSuperclass();
      }
   }

   static boolean eq(CtClass one, CtClass two) {
      return one == two || one != null && two != null && one.getName().equals(two.getName());
   }

   public static void aastore(TypeData array, TypeData value, ClassPool cp) throws BadBytecode {
      if (array instanceof AbsTypeVar && !value.isNullType()) {
         ((AbsTypeVar)array).merge(TypeData.ArrayType.make(value));
      }

      if (value instanceof AbsTypeVar) {
         if (array instanceof AbsTypeVar) {
            TypeData.ArrayElement.make(array);
         } else {
            if (!(array instanceof ClassName)) {
               throw new BadBytecode("bad AASTORE: " + array);
            }

            if (!array.isNullType()) {
               String type = TypeData.ArrayElement.typeName(array.getName());
               value.setType(type, cp);
            }
         }
      }

   }

   protected static class BasicType extends TypeData {
      private String name;
      private int typeTag;
      private char decodedName;

      public BasicType(String type, int tag, char decoded) {
         this.name = type;
         this.typeTag = tag;
         this.decodedName = decoded;
      }

      public int getTypeTag() {
         return this.typeTag;
      }

      public int getTypeData(ConstPool cp) {
         return 0;
      }

      public TypeData join() {
         return (TypeData)(this == TypeTag.TOP ? this : super.join());
      }

      public BasicType isBasicType() {
         return this;
      }

      public boolean is2WordType() {
         return this.typeTag == 4 || this.typeTag == 3;
      }

      public boolean eq(TypeData d) {
         return this == d;
      }

      public String getName() {
         return this.name;
      }

      public char getDecodedName() {
         return this.decodedName;
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         throw new BadBytecode("conflict: " + this.name + " and " + s);
      }

      public TypeData getArrayType(int dim) throws NotFoundException {
         if (this == TypeTag.TOP) {
            return this;
         } else if (dim < 0) {
            throw new NotFoundException("no element type: " + this.name);
         } else if (dim == 0) {
            return this;
         } else {
            char[] name = new char[dim + 1];

            for(int i = 0; i < dim; ++i) {
               name[i] = '[';
            }

            name[dim] = this.decodedName;
            return new ClassName(new String(name));
         }
      }

      String toString2(Set<TypeData> set) {
         return this.name;
      }
   }

   public abstract static class AbsTypeVar extends TypeData {
      public abstract void merge(TypeData var1);

      public int getTypeTag() {
         return 7;
      }

      public int getTypeData(ConstPool cp) {
         return cp.addClassInfo(this.getName());
      }

      public boolean eq(TypeData d) {
         return d.isUninit() ? d.eq(this) : this.getName().equals(d.getName());
      }
   }

   public static class TypeVar extends AbsTypeVar {
      protected List<TypeData> lowers = new ArrayList(2);
      protected List<TypeData> usedBy = new ArrayList(2);
      protected List<String> uppers = null;
      protected String fixedType;
      private boolean is2WordType;
      private int visited = 0;
      private int smallest = 0;
      private boolean inList = false;
      private int dimension = 0;

      public TypeVar(TypeData t) {
         this.merge(t);
         this.fixedType = null;
         this.is2WordType = t.is2WordType();
      }

      public String getName() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).getName() : this.fixedType;
      }

      public BasicType isBasicType() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).isBasicType() : null;
      }

      public boolean is2WordType() {
         return this.fixedType == null ? this.is2WordType : false;
      }

      public boolean isNullType() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).isNullType() : false;
      }

      public boolean isUninit() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).isUninit() : false;
      }

      public void merge(TypeData t) {
         this.lowers.add(t);
         if (t instanceof TypeVar) {
            ((TypeVar)t).usedBy.add(this);
         }

      }

      public int getTypeTag() {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).getTypeTag() : super.getTypeTag();
      }

      public int getTypeData(ConstPool cp) {
         return this.fixedType == null ? ((TypeData)this.lowers.get(0)).getTypeData(cp) : super.getTypeData(cp);
      }

      public void setType(String typeName, ClassPool cp) throws BadBytecode {
         if (this.uppers == null) {
            this.uppers = new ArrayList();
         }

         this.uppers.add(typeName);
      }

      protected TypeVar toTypeVar(int dim) {
         this.dimension = dim;
         return this;
      }

      public TypeData getArrayType(int dim) throws NotFoundException {
         if (dim == 0) {
            return this;
         } else {
            BasicType bt = this.isBasicType();
            if (bt == null) {
               return (TypeData)(this.isNullType() ? new NullType() : (new ClassName(this.getName())).getArrayType(dim));
            } else {
               return bt.getArrayType(dim);
            }
         }
      }

      public int dfs(List<TypeData> preOrder, int index, ClassPool cp) throws NotFoundException {
         if (this.visited > 0) {
            return index;
         } else {
            ++index;
            this.visited = this.smallest = index;
            preOrder.add(this);
            this.inList = true;
            int n = this.lowers.size();

            for(int i = 0; i < n; ++i) {
               TypeVar child = ((TypeData)this.lowers.get(i)).toTypeVar(this.dimension);
               if (child != null) {
                  if (child.visited == 0) {
                     index = child.dfs(preOrder, index, cp);
                     if (child.smallest < this.smallest) {
                        this.smallest = child.smallest;
                     }
                  } else if (child.inList && child.visited < this.smallest) {
                     this.smallest = child.visited;
                  }
               }
            }

            if (this.visited == this.smallest) {
               List<TypeData> scc = new ArrayList();

               TypeVar cv;
               do {
                  cv = (TypeVar)preOrder.remove(preOrder.size() - 1);
                  cv.inList = false;
                  scc.add(cv);
               } while(cv != this);

               this.fixTypes(scc, cp);
            }

            return index;
         }
      }

      private void fixTypes(List<TypeData> scc, ClassPool cp) throws NotFoundException {
         Set<String> lowersSet = new HashSet();
         boolean isBasicType = false;
         TypeData kind = null;
         int size = scc.size();

         for(int i = 0; i < size; ++i) {
            TypeVar tvar = (TypeVar)scc.get(i);
            List<TypeData> tds = tvar.lowers;
            int size2 = tds.size();

            for(int j = 0; j < size2; ++j) {
               TypeData td = (TypeData)tds.get(j);
               TypeData d = td.getArrayType(tvar.dimension);
               BasicType bt = d.isBasicType();
               if (kind == null) {
                  if (bt == null) {
                     isBasicType = false;
                     kind = d;
                     if (d.isUninit()) {
                        break;
                     }
                  } else {
                     isBasicType = true;
                     kind = bt;
                  }
               } else if (bt == null && isBasicType || bt != null && kind != bt) {
                  isBasicType = true;
                  kind = TypeTag.TOP;
                  break;
               }

               if (bt == null && !d.isNullType()) {
                  lowersSet.add(d.getName());
               }
            }
         }

         if (isBasicType) {
            this.is2WordType = kind.is2WordType();
            this.fixTypes1(scc, kind);
         } else {
            String typeName = this.fixTypes2(scc, lowersSet, cp);
            this.fixTypes1(scc, new ClassName(typeName));
         }

      }

      private void fixTypes1(List<TypeData> scc, TypeData kind) throws NotFoundException {
         int size = scc.size();

         for(int i = 0; i < size; ++i) {
            TypeVar cv = (TypeVar)scc.get(i);
            TypeData kind2 = kind.getArrayType(-cv.dimension);
            if (kind2.isBasicType() == null) {
               cv.fixedType = kind2.getName();
            } else {
               cv.lowers.clear();
               cv.lowers.add(kind2);
               cv.is2WordType = kind2.is2WordType();
            }
         }

      }

      private String fixTypes2(List<TypeData> scc, Set<String> lowersSet, ClassPool cp) throws NotFoundException {
         Iterator<String> it = lowersSet.iterator();
         if (lowersSet.isEmpty()) {
            return null;
         } else if (lowersSet.size() == 1) {
            return (String)it.next();
         } else {
            CtClass cc;
            for(cc = cp.get((String)it.next()); it.hasNext(); cc = commonSuperClassEx(cc, cp.get((String)it.next()))) {
            }

            if (cc.getSuperclass() == null || isObjectArray(cc)) {
               cc = this.fixByUppers(scc, cp, new HashSet(), cc);
            }

            return cc.isArray() ? Descriptor.toJvmName(cc) : cc.getName();
         }
      }

      private static boolean isObjectArray(CtClass cc) throws NotFoundException {
         return cc.isArray() && cc.getComponentType().getSuperclass() == null;
      }

      private CtClass fixByUppers(List<TypeData> users, ClassPool cp, Set<TypeData> visited, CtClass type) throws NotFoundException {
         if (users == null) {
            return type;
         } else {
            int size = users.size();

            for(int i = 0; i < size; ++i) {
               TypeVar t = (TypeVar)users.get(i);
               if (!visited.add(t)) {
                  return type;
               }

               if (t.uppers != null) {
                  int s = t.uppers.size();

                  for(int k = 0; k < s; ++k) {
                     CtClass cc = cp.get((String)t.uppers.get(k));
                     if (cc.subtypeOf(type)) {
                        type = cc;
                     }
                  }
               }

               type = this.fixByUppers(t.usedBy, cp, visited, type);
            }

            return type;
         }
      }

      String toString2(Set<TypeData> hash) {
         hash.add(this);
         if (this.lowers.size() > 0) {
            TypeData e = (TypeData)this.lowers.get(0);
            if (e != null && !hash.contains(e)) {
               return e.toString2(hash);
            }
         }

         return "?";
      }
   }

   public static class ArrayType extends AbsTypeVar {
      private AbsTypeVar element;

      private ArrayType(AbsTypeVar elementType) {
         this.element = elementType;
      }

      static TypeData make(TypeData element) throws BadBytecode {
         if (element instanceof ArrayElement) {
            return ((ArrayElement)element).arrayType();
         } else if (element instanceof AbsTypeVar) {
            return new ArrayType((AbsTypeVar)element);
         } else if (element instanceof ClassName && !element.isNullType()) {
            return new ClassName(typeName(element.getName()));
         } else {
            throw new BadBytecode("bad AASTORE: " + element);
         }
      }

      public void merge(TypeData t) {
         try {
            if (!t.isNullType()) {
               this.element.merge(TypeData.ArrayElement.make(t));
            }

         } catch (BadBytecode e) {
            throw new RuntimeException("fatal: " + e);
         }
      }

      public String getName() {
         return typeName(this.element.getName());
      }

      public AbsTypeVar elementType() {
         return this.element;
      }

      public BasicType isBasicType() {
         return null;
      }

      public boolean is2WordType() {
         return false;
      }

      public static String typeName(String elementType) {
         return elementType.charAt(0) == '[' ? "[" + elementType : "[L" + elementType.replace('.', '/') + ";";
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         this.element.setType(TypeData.ArrayElement.typeName(s), cp);
      }

      protected TypeVar toTypeVar(int dim) {
         return this.element.toTypeVar(dim + 1);
      }

      public TypeData getArrayType(int dim) throws NotFoundException {
         return this.element.getArrayType(dim + 1);
      }

      public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
         return this.element.dfs(order, index, cp);
      }

      String toString2(Set<TypeData> set) {
         return "[" + this.element.toString2(set);
      }
   }

   public static class ArrayElement extends AbsTypeVar {
      private AbsTypeVar array;

      private ArrayElement(AbsTypeVar a) {
         this.array = a;
      }

      public static TypeData make(TypeData array) throws BadBytecode {
         if (array instanceof ArrayType) {
            return ((ArrayType)array).elementType();
         } else if (array instanceof AbsTypeVar) {
            return new ArrayElement((AbsTypeVar)array);
         } else if (array instanceof ClassName && !array.isNullType()) {
            return new ClassName(typeName(array.getName()));
         } else {
            throw new BadBytecode("bad AASTORE: " + array);
         }
      }

      public void merge(TypeData t) {
         try {
            if (!t.isNullType()) {
               this.array.merge(TypeData.ArrayType.make(t));
            }

         } catch (BadBytecode e) {
            throw new RuntimeException("fatal: " + e);
         }
      }

      public String getName() {
         return typeName(this.array.getName());
      }

      public AbsTypeVar arrayType() {
         return this.array;
      }

      public BasicType isBasicType() {
         return null;
      }

      public boolean is2WordType() {
         return false;
      }

      private static String typeName(String arrayType) {
         if (arrayType.length() > 1 && arrayType.charAt(0) == '[') {
            char c = arrayType.charAt(1);
            if (c == 'L') {
               return arrayType.substring(2, arrayType.length() - 1).replace('/', '.');
            }

            if (c == '[') {
               return arrayType.substring(1);
            }
         }

         return "java.lang.Object";
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         this.array.setType(TypeData.ArrayType.typeName(s), cp);
      }

      protected TypeVar toTypeVar(int dim) {
         return this.array.toTypeVar(dim - 1);
      }

      public TypeData getArrayType(int dim) throws NotFoundException {
         return this.array.getArrayType(dim - 1);
      }

      public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
         return this.array.dfs(order, index, cp);
      }

      String toString2(Set<TypeData> set) {
         return "*" + this.array.toString2(set);
      }
   }

   public static class UninitTypeVar extends AbsTypeVar {
      protected TypeData type;

      public UninitTypeVar(UninitData t) {
         this.type = t;
      }

      public int getTypeTag() {
         return this.type.getTypeTag();
      }

      public int getTypeData(ConstPool cp) {
         return this.type.getTypeData(cp);
      }

      public BasicType isBasicType() {
         return this.type.isBasicType();
      }

      public boolean is2WordType() {
         return this.type.is2WordType();
      }

      public boolean isUninit() {
         return this.type.isUninit();
      }

      public boolean eq(TypeData d) {
         return this.type.eq(d);
      }

      public String getName() {
         return this.type.getName();
      }

      protected TypeVar toTypeVar(int dim) {
         return null;
      }

      public TypeData join() {
         return this.type.join();
      }

      public void setType(String s, ClassPool cp) throws BadBytecode {
         this.type.setType(s, cp);
      }

      public void merge(TypeData t) {
         if (!t.eq(this.type)) {
            this.type = TypeTag.TOP;
         }

      }

      public void constructorCalled(int offset) {
         this.type.constructorCalled(offset);
      }

      public int offset() {
         if (this.type instanceof UninitData) {
            return ((UninitData)this.type).offset;
         } else {
            throw new RuntimeException("not available");
         }
      }

      public TypeData getArrayType(int dim) throws NotFoundException {
         return this.type.getArrayType(dim);
      }

      String toString2(Set<TypeData> set) {
         return "";
      }
   }

   public static class ClassName extends TypeData {
      private String name;

      public ClassName(String n) {
         this.name = n;
      }

      public String getName() {
         return this.name;
      }

      public BasicType isBasicType() {
         return null;
      }

      public boolean is2WordType() {
         return false;
      }

      public int getTypeTag() {
         return 7;
      }

      public int getTypeData(ConstPool cp) {
         return cp.addClassInfo(this.getName());
      }

      public boolean eq(TypeData d) {
         return d.isUninit() ? d.eq(this) : this.name.equals(d.getName());
      }

      public void setType(String typeName, ClassPool cp) throws BadBytecode {
      }

      public TypeData getArrayType(int dim) throws NotFoundException {
         if (dim == 0) {
            return this;
         } else if (dim <= 0) {
            for(int i = 0; i < -dim; ++i) {
               if (this.name.charAt(i) != '[') {
                  throw new NotFoundException("no " + dim + " dimensional array type: " + this.getName());
               }
            }

            char type = this.name.charAt(-dim);
            if (type == '[') {
               return new ClassName(this.name.substring(-dim));
            } else if (type == 'L') {
               return new ClassName(this.name.substring(-dim + 1, this.name.length() - 1).replace('/', '.'));
            } else if (type == TypeTag.DOUBLE.decodedName) {
               return TypeTag.DOUBLE;
            } else if (type == TypeTag.FLOAT.decodedName) {
               return TypeTag.FLOAT;
            } else if (type == TypeTag.LONG.decodedName) {
               return TypeTag.LONG;
            } else {
               return TypeTag.INTEGER;
            }
         } else {
            char[] dimType = new char[dim];

            for(int i = 0; i < dim; ++i) {
               dimType[i] = '[';
            }

            String elementType = this.getName();
            if (elementType.charAt(0) != '[') {
               elementType = "L" + elementType.replace('.', '/') + ";";
            }

            return new ClassName(new String(dimType) + elementType);
         }
      }

      String toString2(Set<TypeData> set) {
         return this.name;
      }
   }

   public static class NullType extends ClassName {
      public NullType() {
         super("null-type");
      }

      public int getTypeTag() {
         return 5;
      }

      public boolean isNullType() {
         return true;
      }

      public int getTypeData(ConstPool cp) {
         return 0;
      }

      public TypeData getArrayType(int dim) {
         return this;
      }
   }

   public static class UninitData extends ClassName {
      int offset;
      boolean initialized;

      UninitData(int offset, String className) {
         super(className);
         this.offset = offset;
         this.initialized = false;
      }

      public UninitData copy() {
         return new UninitData(this.offset, this.getName());
      }

      public int getTypeTag() {
         return 8;
      }

      public int getTypeData(ConstPool cp) {
         return this.offset;
      }

      public TypeData join() {
         return (TypeData)(this.initialized ? new TypeVar(new ClassName(this.getName())) : new UninitTypeVar(this.copy()));
      }

      public boolean isUninit() {
         return true;
      }

      public boolean eq(TypeData d) {
         if (!(d instanceof UninitData)) {
            return false;
         } else {
            UninitData ud = (UninitData)d;
            return this.offset == ud.offset && this.getName().equals(ud.getName());
         }
      }

      public int offset() {
         return this.offset;
      }

      public void constructorCalled(int offset) {
         if (offset == this.offset) {
            this.initialized = true;
         }

      }

      String toString2(Set<TypeData> set) {
         return this.getName() + "," + this.offset;
      }
   }

   public static class UninitThis extends UninitData {
      UninitThis(String className) {
         super(-1, className);
      }

      public UninitData copy() {
         return new UninitThis(this.getName());
      }

      public int getTypeTag() {
         return 6;
      }

      public int getTypeData(ConstPool cp) {
         return 0;
      }

      String toString2(Set<TypeData> set) {
         return "uninit:this";
      }
   }
}
