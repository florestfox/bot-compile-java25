package nl.mtvehicles.core.movement;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.movement.versions.VehicleMovement1_12;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Player;

@VersionSpecific
public class PacketHandler {
   public static void movement_1_21_R6(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         private PacketPlayInSteerVehicle lastPacket = null;
         private int taskId = -1;

         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               this.lastPacket = ppisv;
               if (this.taskId != -1) {
                  Bukkit.getScheduler().cancelTask(this.taskId);
               }

               this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                  if (player.isInsideVehicle()) {
                     VehicleMovement movement = new VehicleMovement();
                     movement.vehicleMovement(player, this.lastPacket);
                  } else {
                     Bukkit.getScheduler().cancelTask(this.taskId);
                     this.taskId = -1;
                  }

               }, 0L, 1L);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("g");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_21_R6(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_21_R5(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         private PacketPlayInSteerVehicle lastPacket = null;
         private int taskId = -1;

         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               this.lastPacket = ppisv;
               if (this.taskId != -1) {
                  Bukkit.getScheduler().cancelTask(this.taskId);
               }

               this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                  if (player.isInsideVehicle()) {
                     VehicleMovement movement = new VehicleMovement();
                     movement.vehicleMovement(player, this.lastPacket);
                  } else {
                     Bukkit.getScheduler().cancelTask(this.taskId);
                     this.taskId = -1;
                  }

               }, 0L, 1L);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("g");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_21_R5(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_21_R4(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         private PacketPlayInSteerVehicle lastPacket = null;
         private int taskId = -1;

         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               this.lastPacket = ppisv;
               if (this.taskId != -1) {
                  Bukkit.getScheduler().cancelTask(this.taskId);
               }

               this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                  if (player.isInsideVehicle()) {
                     VehicleMovement movement = new VehicleMovement();
                     movement.vehicleMovement(player, this.lastPacket);
                  } else {
                     Bukkit.getScheduler().cancelTask(this.taskId);
                     this.taskId = -1;
                  }

               }, 0L, 1L);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("f");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_21_R4(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_21_R3(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         private PacketPlayInSteerVehicle lastPacket = null;
         private int taskId = -1;

         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               this.lastPacket = ppisv;
               if (this.taskId != -1) {
                  Bukkit.getScheduler().cancelTask(this.taskId);
               }

               this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                  if (player.isInsideVehicle()) {
                     VehicleMovement movement = new VehicleMovement();
                     movement.vehicleMovement(player, this.lastPacket);
                  } else {
                     Bukkit.getScheduler().cancelTask(this.taskId);
                     this.taskId = -1;
                  }

               }, 0L, 1L);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("f");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_21_R3(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_21_R2(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         private PacketPlayInSteerVehicle lastPacket = null;
         private int taskId = -1;

         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               this.lastPacket = ppisv;
               if (this.taskId != -1) {
                  Bukkit.getScheduler().cancelTask(this.taskId);
               }

               this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
                  if (player.isInsideVehicle()) {
                     VehicleMovement movement = new VehicleMovement();
                     movement.vehicleMovement(player, this.lastPacket);
                  } else {
                     Bukkit.getScheduler().cancelTask(this.taskId);
                     this.taskId = -1;
                  }

               }, 0L, 1L);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("f");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_21_R2(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_21_R1(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("c");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_21_R1(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_20_R4(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("c");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_20_R4(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_20_R3(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("c");
         playerConnectionField.setAccessible(true);
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("c");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_20_R3(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_20_R2(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("c");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = ServerCommonPacketListenerImpl.class.getDeclaredField("c");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("n");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_20_R2(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_20_R1(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("c");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = playerConnection.getClass().getDeclaredField("h");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("m");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_20_R1(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_19_R3(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("b");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = playerConnection.getClass().getDeclaredField("h");
         networkManagerField.setAccessible(true);
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("m");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_19_R3(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_19_R2(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("b");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = playerConnection.getClass().getField("b");
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("m");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_19_R2(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_19(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer)player).getHandle();
         Field playerConnectionField = entityPlayer.getClass().getField("b");
         PlayerConnection playerConnection = (PlayerConnection)playerConnectionField.get(entityPlayer);
         Field networkManagerField = playerConnection.getClass().getField("b");
         NetworkManager networkManager = (NetworkManager)networkManagerField.get(playerConnection);
         Field channelField = networkManager.getClass().getField("m");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_19(player);
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_18_R2(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         Object networkManager = ((org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer)player).getHandle().b.a;
         Field channelField = networkManager.getClass().getField("m");
         channel = (Channel)channelField.get(networkManager);
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_18_R2(player);
      } catch (NoSuchElementException var6) {
      } catch (NoSuchFieldException | IllegalAccessException e) {
         unexpectedException(e);
      }

   }

   public static void movement_1_18_R1(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         channel = ((org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer)player).getHandle().b.a.k;
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_18_R1(player);
      } catch (NoSuchElementException var5) {
      }

   }

   public static void movement_1_17(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof PacketPlayInSteerVehicle) {
               PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         channel = ((org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer)player).getHandle().b.a.k;
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_17(player);
      } catch (NoSuchElementException var5) {
      }

   }

   public static void movement_1_16(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle) {
               net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         channel = ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_16(player);
      } catch (NoSuchElementException var5) {
      }

   }

   public static void movement_1_15(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle) {
               net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         channel = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_15(player);
      } catch (NoSuchElementException var5) {
      }

   }

   public static void movement_1_13(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle) {
               net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle)packet;
               VehicleMovement movement = new VehicleMovement();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         channel = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_13(player);
      } catch (NoSuchElementException var5) {
      }

   }

   public static void movement_1_12(final Player player) {
      ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
         public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
            super.channelRead(channelHandlerContext, packet);
            if (packet instanceof net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle) {
               net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle)packet;
               VehicleMovement1_12 movement = new VehicleMovement1_12();
               movement.vehicleMovement(player, ppisv);
            }

         }
      };
      Channel channel = null;

      try {
         channel = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
         channel.pipeline().addBefore("packet_handler", player.getName(), channelDuplexHandler);
      } catch (IllegalArgumentException e) {
         if (channel == null) {
            unexpectedException(e);
            return;
         }

         if (!channel.pipeline().names().contains(player.getName())) {
            return;
         }

         channel.pipeline().remove(player.getName());
         movement_1_16(player);
      } catch (NoSuchElementException var5) {
      }

   }

   private static void unexpectedException() {
      Main.logSevere("An unexpected error occurred. Disabling the plugin...");
      Main.disablePlugin();
   }

   private static void unexpectedException(Exception e) {
      Main.logSevere("An unexpected error occurred, disabling the plugin... Check the exception log:");
      e.printStackTrace();
      Main.disablePlugin();
   }

   @VersionSpecific
   public static boolean isObjectPacket(Object object) {
      String errorMessage = "An unexpected error occurred (given object is not a valid steering packet). Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle";
      if (VersionModule.getServerVersion().is1_12()) {
         if (!(object instanceof net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle)) {
            Main.logSevere("An unexpected error occurred (given object is not a valid steering packet). Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle");
            return false;
         }
      } else if (VersionModule.getServerVersion().is1_13()) {
         if (!(object instanceof net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle)) {
            Main.logSevere("An unexpected error occurred (given object is not a valid steering packet). Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle");
            return false;
         }
      } else if (VersionModule.getServerVersion().is1_15()) {
         if (!(object instanceof net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle)) {
            Main.logSevere("An unexpected error occurred (given object is not a valid steering packet). Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle");
            return false;
         }
      } else if (VersionModule.getServerVersion().is1_16()) {
         if (!(object instanceof net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle)) {
            Main.logSevere("An unexpected error occurred (given object is not a valid steering packet). Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle");
            return false;
         }
      } else if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_17) && !(object instanceof PacketPlayInSteerVehicle)) {
         Main.logSevere("An unexpected error occurred (given object is not a valid steering packet). Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle");
         return false;
      }

      return true;
   }
}
