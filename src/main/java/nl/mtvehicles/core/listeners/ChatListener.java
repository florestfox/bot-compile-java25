package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.events.ChatEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends MTVListener {
   public ChatListener() {
      super(new ChatEvent());
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onLicensePlateChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".kenteken") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".kenteken")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editLicensePlate(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".kenteken", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.menuEdit(this.player));
                     }

                  } else {
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.menuEdit(this.player));
                     }

                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".kenteken", false);
                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onVehicleNameChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".naam") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".naam")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editName(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".naam", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.menuEdit(this.player));
                     }

                  } else {
                     MenuUtils.menuEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".naam", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.menuEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onFuelChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".benzine") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".benzine")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isInt(message)) {
                     MenuUtils.benzineEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".benzine", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editFuel(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".benzine", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.benzineEdit(this.player));
                     }

                  } else {
                     MenuUtils.benzineEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".benzine", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.benzineEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onFuelUsageChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".benzineverbruik") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".benzineverbruik")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isDouble(message)) {
                     MenuUtils.benzineEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".benzineverbruik", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editFuelUsage(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".benzineverbruik", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.benzineEdit(this.player));
                     }

                  } else {
                     MenuUtils.benzineEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".benzine", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.benzineEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onTrunkRowsChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".kofferbakRows") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".kofferbakRows")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (message.toLowerCase().contains("!q")) {
                     MenuUtils.trunkEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".kofferbakRows", false);
                     if (event.isAsynchronous()) {
                        Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.trunkEdit(this.player));
                     }

                  } else if (!this.isInt(message)) {
                     MenuUtils.trunkEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".kofferbakRows", false);
                  } else {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editTrunkRows(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".kofferbakRows", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.trunkEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onAccelerationSpeedChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".acceleratieSpeed") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".acceleratieSpeed")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isDouble(message)) {
                     MenuUtils.speedEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".acceleratieSpeed", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editAccelerationSpeed(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".acceleratieSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  } else {
                     MenuUtils.speedEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".acceleratieSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onMaxSpeedChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".maxSpeed") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".maxSpeed")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isDouble(message)) {
                     MenuUtils.speedEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeed", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editMaxSpeed(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  } else {
                     MenuUtils.speedEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onBrakingSpeedChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".brakingSpeed") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".brakingSpeed")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isDouble(message)) {
                     MenuUtils.speedEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".brakingSpeed", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editBrakingSpeed(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".brakingSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  } else {
                     MenuUtils.speedEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".brakingSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onFrictionSpeedChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".aftrekkenSpeed") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".aftrekkenSpeed")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isDouble(message)) {
                     MenuUtils.speedEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".aftrekkenSpeed", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editFrictionSpeed(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".aftrekkenSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  } else {
                     MenuUtils.speedEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".aftrekkenSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onMaxSpeedBackwardsChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".maxSpeedBackwards") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".maxSpeedBackwards")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isDouble(message)) {
                     MenuUtils.speedEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeedBackwards", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editMaxSpeedBackwards(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeedBackwards", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  } else {
                     MenuUtils.speedEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeedBackwards", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onRotateSpeedChange(AsyncPlayerChatEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      String realMessage = event.getMessage().trim();
      if (ItemUtils.edit.get(this.player.getUniqueId() + ".rotateSpeed") != null) {
         if ((Boolean)ItemUtils.edit.get(this.player.getUniqueId() + ".rotateSpeed")) {
            event.setCancelled(true);
            Main.schedulerRun(() -> {
               ChatEvent api = (ChatEvent)this.getAPI();
               api.setMessage(realMessage);
               this.callAPI();
               if (!this.isCancelled()) {
                  String message = api.getMessage();
                  if (!this.isInt(message)) {
                     MenuUtils.speedEdit(this.player);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".rotateSpeed", false);
                  } else if (!message.toLowerCase().contains("!q")) {
                     String licensePlate = this.getLicensePlate(this.player);
                     if (VehicleEdit.editRotationSpeed(this.player, licensePlate, message)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
                     }

                     ItemUtils.edit.put(this.player.getUniqueId() + ".rotateSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  } else {
                     MenuUtils.speedEdit(this.player);
                     ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_CANCELLED);
                     ItemUtils.edit.put(this.player.getUniqueId() + ".rotateSpeed", false);
                     if (event.isAsynchronous()) {
                        Main.schedulerRun(() -> MenuUtils.speedEdit(this.player));
                     }

                  }
               }
            });
         }
      }
   }

   private boolean isInt(String str) {
      try {
         Integer.parseInt(str);
         return true;
      } catch (Throwable var3) {
         ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.MUST_BE_INTEGER);
         return false;
      }
   }

   private boolean isDouble(String str) {
      try {
         Double.valueOf(str);
         return true;
      } catch (Throwable var3) {
         ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.MUST_BE_DOUBLE);
         return false;
      }
   }

   private String getLicensePlate(Player player) {
      NBTItem nbt = new NBTItem(player.getInventory().getItemInMainHand());
      return nbt.getString("mtvehicles.kenteken");
   }
}
