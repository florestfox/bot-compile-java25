package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import java.io.File;
import java.io.IOException;

public interface NBTFileHandle extends ReadWriteNBT {
   void save() throws IOException;

   File getFile();
}
