package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation.Annotation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.bytecode.annotation.AnnotationsWriter;

public class ParameterAnnotationsAttribute extends AttributeInfo {
   public static final String visibleTag = "RuntimeVisibleParameterAnnotations";
   public static final String invisibleTag = "RuntimeInvisibleParameterAnnotations";

   public ParameterAnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
      super(cp, attrname, info);
   }

   public ParameterAnnotationsAttribute(ConstPool cp, String attrname) {
      this(cp, attrname, new byte[]{0});
   }

   ParameterAnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
      super(cp, n, in);
   }

   public int numParameters() {
      return this.info[0] & 255;
   }

   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
      AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, newCp, classnames);

      try {
         copier.parameters();
         return new ParameterAnnotationsAttribute(newCp, this.getName(), copier.close());
      } catch (Exception e) {
         throw new RuntimeException(e.toString());
      }
   }

   public Annotation[][] getAnnotations() {
      try {
         return (new AnnotationsAttribute.Parser(this.info, this.constPool)).parseParameters();
      } catch (Exception e) {
         throw new RuntimeException(e.toString());
      }
   }

   public void setAnnotations(Annotation[][] params) {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);

      try {
         writer.numParameters(params.length);

         for(Annotation[] anno : params) {
            writer.numAnnotations(anno.length);

            for(int j = 0; j < anno.length; ++j) {
               anno[j].write(writer);
            }
         }

         writer.close();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }

      this.set(output.toByteArray());
   }

   void renameClass(String oldname, String newname) {
      Map<String, String> map = new HashMap();
      map.put(oldname, newname);
      this.renameClass(map);
   }

   void renameClass(Map<String, String> classnames) {
      AnnotationsAttribute.Renamer renamer = new AnnotationsAttribute.Renamer(this.info, this.getConstPool(), classnames);

      try {
         renamer.parameters();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   void getRefClasses(Map<String, String> classnames) {
      this.renameClass(classnames);
   }

   public String toString() {
      Annotation[][] aa = this.getAnnotations();
      StringBuilder sbuf = new StringBuilder();

      for(Annotation[] a : aa) {
         for(Annotation i : a) {
            sbuf.append(i.toString()).append(' ');
         }

         sbuf.append(", ");
      }

      return sbuf.toString().replaceAll(" (?=,)|, $", "");
   }
}
