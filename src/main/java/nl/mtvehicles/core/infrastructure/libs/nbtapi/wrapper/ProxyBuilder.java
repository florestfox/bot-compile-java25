package nl.mtvehicles.core.infrastructure.libs.nbtapi.wrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.NBTHandler;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBT;

public class ProxyBuilder<T extends NBTProxy> implements InvocationHandler {
   private static final Map<Method, Function<Arguments, Object>> METHOD_CACHE = new ConcurrentHashMap();
   private final Class<T> target;
   private final ReadWriteNBT nbt;
   private boolean readOnly;

   public ProxyBuilder(ReadWriteNBT nbt, Class<T> target) {
      if (!target.isInterface()) {
         throw new NbtApiException("A proxy can only be built from an interface! Check the wiki for examples.");
      } else {
         this.target = target;
         this.nbt = nbt;
      }
   }

   public ProxyBuilder<T> readOnly() {
      this.readOnly = true;
      return this;
   }

   public T build() {
      T inst = (T)(Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{this.target}, this));
      inst.init();
      return inst;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      METHOD_CACHE.computeIfAbsent(method, (m) -> createFunction((NBTProxy)proxy, m));
      return ((Function)METHOD_CACHE.get(method)).apply(new Arguments(this.target, (NBTProxy)proxy, this.readOnly, this.nbt, args));
   }

   private static Function<Arguments, Object> createFunction(NBTProxy proxy, Method method) {
      if ("toString".equals(method.getName()) && method.getParameterCount() == 0 && method.getReturnType() == String.class) {
         return (arguments) -> arguments.nbt.toString();
      } else if (method.isDefault()) {
         return (arguments) -> DefaultMethodInvoker.invokeDefault(arguments.target, arguments.proxy, method, arguments.args);
      } else {
         NBTTarget.Type action = getAction(method);
         if (action == NBTTarget.Type.SET) {
            String fieldName = getNBTName(proxy.getCasing(), method);
            return (arguments) -> {
               if (arguments.readOnly) {
                  throw new NbtApiException("Tried calling a set method on a read only object.");
               } else {
                  return setNBT(arguments.nbt, arguments.proxy, fieldName, arguments.args[0]);
               }
            };
         } else if (action == NBTTarget.Type.GET) {
            Class<?> retType = method.getReturnType();
            String fieldName = getNBTName(proxy.getCasing(), method);
            if (retType.isInterface() && NBTProxy.class.isAssignableFrom(retType)) {
               return (arguments) -> {
                  if (arguments.nbt.hasTag(fieldName) && arguments.nbt.getType(fieldName) != NBTType.NBTTagCompound) {
                     throw new NbtApiException("Tried getting a '" + retType + "' proxy from the field '" + fieldName + "', but it's not a TagCompound!");
                  } else {
                     return (new ProxyBuilder(arguments.nbt.getOrCreateCompound(fieldName), retType)).build();
                  }
               };
            } else {
               if (retType == ProxyList.class) {
                  Class<?> parameterType = (Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
                  if (parameterType != null && parameterType.isInterface() && NBTProxy.class.isAssignableFrom(parameterType)) {
                     return (arguments) -> new ProxiedList(arguments.nbt.getCompoundList(fieldName), parameterType);
                  }
               }

               NBTHandler<Object> handler = proxy.<Object>getHandler(retType);
               return handler != null ? (arguments) -> handler.get(arguments.nbt, fieldName) : (arguments) -> arguments.nbt.getOrNull(fieldName, retType);
            }
         } else if (action == NBTTarget.Type.HAS) {
            String fieldName = getNBTName(proxy.getCasing(), method);
            return (arguments) -> arguments.nbt.hasTag(fieldName);
         } else {
            throw new IllegalArgumentException("The method '" + method.getName() + "' in '" + method.getDeclaringClass().getName() + "' can not be handled by the NBT-API. Please check the Wiki for examples!");
         }
      }
   }

   private static NBTTarget.Type getAction(Method method) {
      NBTTarget target = (NBTTarget)method.getAnnotation(NBTTarget.class);
      if (target != null) {
         if (target.type() == NBTTarget.Type.HAS && method.getParameterCount() == 0 && method.getReturnType() == Boolean.TYPE) {
            return NBTTarget.Type.HAS;
         }

         if (target.type() == NBTTarget.Type.GET && method.getParameterCount() == 0) {
            return NBTTarget.Type.GET;
         }

         if (target.type() == NBTTarget.Type.SET && method.getParameterCount() == 1) {
            return NBTTarget.Type.SET;
         }
      }

      if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
         return NBTTarget.Type.SET;
      } else if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
         return NBTTarget.Type.GET;
      } else {
         return method.getName().startsWith("has") && method.getParameterCount() == 0 && method.getReturnType() == Boolean.TYPE ? NBTTarget.Type.HAS : null;
      }
   }

   private static String getNBTName(Casing casing, Method method) {
      NBTTarget target = (NBTTarget)method.getAnnotation(NBTTarget.class);
      return target != null ? target.value() : casing.convertString(method.getName().substring(3));
   }

   private static Object setNBT(ReadWriteNBT nbt, NBTProxy proxy, String key, Object value) {
      if (value == null) {
         nbt.removeKey(key);
      } else if (value instanceof Boolean) {
         nbt.setBoolean(key, (Boolean)value);
      } else if (value instanceof Byte) {
         nbt.setByte(key, (Byte)value);
      } else if (value instanceof Short) {
         nbt.setShort(key, (Short)value);
      } else if (value instanceof Integer) {
         nbt.setInteger(key, (Integer)value);
      } else if (value instanceof Long) {
         nbt.setLong(key, (Long)value);
      } else if (value instanceof Float) {
         nbt.setFloat(key, (Float)value);
      } else if (value instanceof Double) {
         nbt.setDouble(key, (Double)value);
      } else if (value instanceof byte[]) {
         nbt.setByteArray(key, (byte[])value);
      } else if (value instanceof int[]) {
         nbt.setIntArray(key, (int[])value);
      } else if (value instanceof long[]) {
         nbt.setLongArray(key, (long[])value);
      } else if (value instanceof String) {
         nbt.setString(key, (String)value);
      } else if (value instanceof UUID) {
         nbt.setUUID(key, (UUID)value);
      } else if (value.getClass().isEnum()) {
         nbt.setEnum(key, (Enum)value);
      } else {
         NBTHandler<Object> handler = proxy.<Object>getHandler(value.getClass());
         if (handler == null) {
            for(NBTHandler<Object> nbth : proxy.getHandlers()) {
               if (nbth.fuzzyMatch(value)) {
                  nbth.set(nbt, key, value);
                  return null;
               }
            }

            throw new IllegalArgumentException("Tried setting an object of type '" + value.getClass().getName() + "'. This is not a supported NBT value. Please check the Wiki for examples!");
         }

         handler.set(nbt, key, value);
      }

      return null;
   }

   private static class Arguments {
      Class<?> target;
      NBTProxy proxy;
      ReadWriteNBT nbt;
      Object[] args;
      boolean readOnly;

      public Arguments(Class<?> target, NBTProxy proxy, boolean readOnly, ReadWriteNBT nbt, Object[] args) {
         this.target = target;
         this.proxy = proxy;
         this.nbt = nbt;
         this.args = args;
         this.readOnly = readOnly;
      }
   }
}
