package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist;

final class ClassPathList {
   ClassPathList next;
   ClassPath path;

   ClassPathList(ClassPath p, ClassPathList n) {
      this.next = n;
      this.path = p;
   }
}
