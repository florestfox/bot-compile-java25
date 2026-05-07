package nl.mtvehicles.core.listeners.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleMenu;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;
import nl.mtvehicles.core.infrastructure.utils.MenuUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener extends MTVListener {
   public HashMap<UUID, ItemStack> vehicleMenu = new HashMap();
   public static HashMap<UUID, Inventory> skinMenu = new HashMap();
   public static HashMap<UUID, Integer> intSave = new HashMap();
   public static HashMap<UUID, Integer> id = new HashMap();
   public static HashMap<UUID, Integer> raw = new HashMap();
   private ItemStack clickedItem;
   private int clickedSlot;
   private InventoryTitle title;

   @EventHandler
   public void onClick(InventoryClickEvent event) {
      this.event = event;
      if (event.getCurrentItem() != null) {
         if (event.getCurrentItem().hasItemMeta()) {
            String stringTitle = event.getView().getTitle();
            this.clickedItem = event.getCurrentItem();
            this.clickedSlot = event.getRawSlot();
            this.player = (Player)event.getWhoClicked();
            if (InventoryTitle.getByStringTitle(stringTitle) != null) {
               this.title = InventoryTitle.getByStringTitle(stringTitle);
               this.setAPI(new nl.mtvehicles.core.events.inventory.InventoryClickEvent(this.title));
               nl.mtvehicles.core.events.inventory.InventoryClickEvent api = (nl.mtvehicles.core.events.inventory.InventoryClickEvent)this.getAPI();
               api.setClickedSlot(this.clickedSlot);
               this.callAPI();
               if (!this.isCancelled()) {
                  this.clickedSlot = api.getClickedSlot();
                  this.title = api.getTitle();
                  event.setCancelled(true);
                  if (this.title.equals(InventoryTitle.VEHICLE_MENU)) {
                     this.vehicleMenu();
                  } else if (this.title.equals(InventoryTitle.CHOOSE_VEHICLE_MENU)) {
                     this.chooseVehicleMenu();
                  } else if (this.title.equals(InventoryTitle.CHOOSE_LANGUAGE_MENU)) {
                     this.chooseLanguageMenu();
                  } else if (this.title.equals(InventoryTitle.CONFIRM_VEHICLE_MENU)) {
                     this.confirmVehicleMenu();
                  } else if (this.title.equals(InventoryTitle.VEHICLE_RESTORE_MENU)) {
                     this.vehicleRestoreMenu();
                  } else if (this.title.equals(InventoryTitle.VEHICLE_EDIT_MENU)) {
                     this.vehicleEditMenu();
                  } else if (this.title.equals(InventoryTitle.VEHICLE_SETTINGS_MENU)) {
                     this.vehicleSettingsMenu();
                  } else if (this.title.equals(InventoryTitle.VEHICLE_FUEL_MENU)) {
                     this.vehicleFuelMenu();
                  } else if (this.title.equals(InventoryTitle.VEHICLE_TRUNK_MENU)) {
                     this.vehicleTrunkMenu();
                  } else if (this.title.equals(InventoryTitle.VEHICLE_MEMBERS_MENU)) {
                     this.vehicleMembersMenu();
                  } else if (this.title.equals(InventoryTitle.VEHICLE_SPEED_MENU)) {
                     this.vehicleSpeedMenu();
                  } else if (this.title.equals(InventoryTitle.JERRYCAN_MENU)) {
                     this.jerryCanMenu();
                  } else if (this.title.equals(InventoryTitle.VOUCHER_REDEEM_MENU)) {
                     this.voucherRedeemMenu();
                  } else {
                     event.setCancelled(false);
                  }

               }
            }
         }
      }
   }

   private void vehicleMenu() {
      id.put(this.player.getUniqueId(), 1);
      raw.put(this.player.getUniqueId(), this.clickedSlot);
      MenuUtils.getvehicleCMD(this.player, (Integer)id.get(this.player.getUniqueId()), (Integer)raw.get(this.player.getUniqueId()));
   }

   private void chooseVehicleMenu() {
      if (this.clickedItem.equals(MenuUtils.getCloseItem())) {
         this.player.closeInventory();
      } else if (this.clickedItem.equals(MenuUtils.getBackItem())) {
         this.player.openInventory((Inventory)VehicleMenu.beginMenu.get(this.player.getUniqueId()));
      } else if (!this.clickedItem.equals(ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"))) {
         if (this.clickedSlot == 53) {
            MenuUtils.getvehicleCMD(this.player, (Integer)id.get(this.player.getUniqueId()) + 1, (Integer)raw.get(this.player.getUniqueId()));
            id.put(this.player.getUniqueId(), (Integer)id.get(this.player.getUniqueId()) + 1);
         } else if (this.clickedSlot == 45) {
            if ((Integer)id.get(this.player.getUniqueId()) > 1) {
               MenuUtils.getvehicleCMD(this.player, (Integer)id.get(this.player.getUniqueId()) - 1, (Integer)raw.get(this.player.getUniqueId()));
               id.put(this.player.getUniqueId(), (Integer)id.get(this.player.getUniqueId()) - 1);
            }

         } else {
            this.vehicleMenu.put(this.player.getUniqueId(), this.clickedItem);
            Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, InventoryTitle.CONFIRM_VEHICLE_MENU.getStringTitle());
            MessagesConfig msg = ConfigModule.messagesConfig;
            inv.setItem(11, ItemUtils.getMenuItem("RED_WOOL", "WOOL", (short)14, 1, "&c" + msg.getMessage(Message.CANCEL), "&7" + msg.getMessage(Message.CANCEL_ACTION)));
            inv.setItem(15, ItemUtils.getMenuItem("LIME_WOOL", "WOOL", (short)5, 1, "&a" + msg.getMessage(Message.CONFIRM), "&7" + msg.getMessage(Message.CONFIRM_ACTION), "&7" + msg.getMessage(Message.CONFIRM_VEHICLE_GIVE)));
            this.player.openInventory(inv);
         }
      }
   }

   private void chooseLanguageMenu() {
      LanguageUtils.changeLanguageMenu(this.player, this.clickedSlot);
      this.player.closeInventory();
   }

   private void confirmVehicleMenu() {
      if (this.clickedSlot == 11) {
         this.player.openInventory((Inventory)skinMenu.get(this.player.getUniqueId()));
      } else if (this.clickedSlot == 15) {
         if (!this.canGetVehicleFromMenu()) {
            this.player.closeInventory();
            return;
         }

         List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
         ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.COMPLETED_VEHICLE_GIVE);
         this.player.getInventory().addItem(new ItemStack[]{(ItemStack)this.vehicleMenu.get(this.player.getUniqueId())});
         NBTItem nbt = new NBTItem((ItemStack)this.vehicleMenu.get(this.player.getUniqueId()));
         String licensePlate = nbt.getString("mtvehicles.kenteken");
         List<Map<?, ?>> skins = (List)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("cars");
         double price = (double)0.0F;

         for(Map<?, ?> skin : skins) {
            if (skin.get("itemDamage").equals(((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("skinDamage")) && skin.get("SkinItem").equals(((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("skinItem"))) {
               price = (Double)skin.get("price");
            }
         }

         Vehicle vehicle = new Vehicle((Map)null, licensePlate, nbt.getString("mtvehicles.naam"), VehicleType.valueOf((String)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("vehicleType")), false, ((ItemStack)this.vehicleMenu.get(this.player.getUniqueId())).getDurability(), ((ItemStack)this.vehicleMenu.get(this.player.getUniqueId())).getType().toString(), false, (Boolean)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("hornEnabled"), (Double)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("maxHealth"), (Boolean)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("benzineEnabled"), (double)100.0F, 0.01, (Boolean)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("kofferbakEnabled"), 1, ConfigModule.vehicleDataConfig.getTrunkData(licensePlate), (Double)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("acceleratieSpeed"), (Double)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("maxSpeed"), (Double)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("maxSpeedBackwards"), (Double)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("brakingSpeed"), (Double)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("aftrekkenSpeed"), (Integer)((Map)vehicles.get((Integer)intSave.get(this.player.getUniqueId()))).get("rotateSpeed"), this.player.getUniqueId(), ConfigModule.vehicleDataConfig.getRiders(licensePlate), ConfigModule.vehicleDataConfig.getMembers(licensePlate), price, nbt.getString("mtcustom"));
         vehicle.save();
         this.player.closeInventory();
      }

   }

   private void vehicleRestoreMenu() {
      if (!this.clickedItem.equals(ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"))) {
         if (this.clickedSlot == 53) {
            MenuUtils.restoreCMD(this.player, (Integer)MenuUtils.restorePage.get(this.player) + 1, (UUID)MenuUtils.restoreUUID.get(this.player));
         } else if (this.clickedSlot == 45) {
            if ((Integer)MenuUtils.restorePage.get(this.player) - 1 >= 1) {
               MenuUtils.restoreCMD(this.player, (Integer)MenuUtils.restorePage.get(this.player) - 1, (UUID)MenuUtils.restoreUUID.get(this.player));
            }

         } else {
            this.player.getInventory().addItem(new ItemStack[]{this.clickedItem});
         }
      }
   }

   private void vehicleEditMenu() {
      switch (this.clickedSlot) {
         case 10:
            MenuUtils.menuEdit(this.player);
            break;
         case 11:
            MenuUtils.benzineEdit(this.player);
            break;
         case 12:
            MenuUtils.trunkEdit(this.player);
            break;
         case 13:
            MenuUtils.membersEdit(this.player);
            break;
         case 14:
            MenuUtils.speedEdit(this.player);
         case 15:
         default:
            break;
         case 16:
            this.deleteVehicle();
      }

   }

   private void deleteVehicle() {
      try {
         NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
         String licensePlate = nbt.getString("mtvehicles.kenteken");
         VehicleUtils.getVehicle(licensePlate).delete();
         this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_DELETED)));
      } catch (Exception var3) {
         this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ALREADY_DELETED)));
      }

      this.player.getInventory().getItemInMainHand().setAmount(0);
      this.player.closeInventory();
   }

   private void vehicleSettingsMenu() {
      if (this.clickedItem.equals(MenuUtils.getCloseItem())) {
         this.player.closeInventory();
      } else if (this.clickedItem.equals(MenuUtils.getBackItem())) {
         VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
      } else {
         NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
         String licensePlate = nbt.getString("mtvehicles.kenteken");
         boolean isGlowing = (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING);
         if (this.clickedSlot == 16) {
            VehicleEdit.editGlowing(this.player, licensePlate, isGlowing ? "false" : "true");
            ConfigModule.vehicleDataConfig.save();
            MenuUtils.menuEdit(this.player);
         }

         if (this.clickedSlot == 13) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_LICENSE_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".kenteken", true);
         }

         if (this.clickedSlot == 10) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_NAME_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".naam", true);
         }

      }
   }

   private void vehicleFuelMenu() {
      if (this.clickedItem.equals(MenuUtils.getCloseItem())) {
         this.player.closeInventory();
      } else if (this.clickedItem.equals(MenuUtils.getBackItem())) {
         VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
      } else {
         NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
         String licensePlate = nbt.getString("mtvehicles.kenteken");
         String menuItem = (new NBTItem(this.clickedItem)).getString("mtvehicles.item");
         if (menuItem.contains("1")) {
            boolean fuelEnabled = (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED);
            VehicleEdit.editFuelEnabled(this.player, licensePlate, fuelEnabled ? "false" : "true");
            MenuUtils.benzineEdit(this.player);
         }

         if (menuItem.contains("2")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".benzine", true);
         }

         if (menuItem.contains("3")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_NEW_BENZINE_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".benzineverbruik", true);
         }

      }
   }

   private void vehicleTrunkMenu() {
      if (this.clickedItem.equals(MenuUtils.getCloseItem())) {
         this.player.closeInventory();
      } else if (this.clickedItem.equals(MenuUtils.getBackItem())) {
         VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
      } else {
         NBTItem nbt = new NBTItem(this.player.getInventory().getItemInMainHand());
         String licensePlate = nbt.getString("mtvehicles.kenteken");
         String menuItem = (new NBTItem(this.clickedItem)).getString("mtvehicles.item");
         if (menuItem.contains("1")) {
            boolean trunkEnabled = (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED);
            VehicleEdit.editTrunkEnabled(this.player, licensePlate, trunkEnabled ? "false" : "true");
            MenuUtils.trunkEdit(this.player);
         }

         if (menuItem.contains("2")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_NEW_ROWS_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".kofferbakRows", true);
         }

         if (menuItem.contains("3")) {
            this.player.closeInventory();
            VehicleUtils.openTrunk(this.player, licensePlate);
         }

      }
   }

   private void vehicleMembersMenu() {
      if (this.clickedItem.equals(MenuUtils.getCloseItem())) {
         this.player.closeInventory();
      } else if (this.clickedItem.equals(MenuUtils.getBackItem())) {
         VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
      }

   }

   private void vehicleSpeedMenu() {
      if (this.clickedItem.equals(MenuUtils.getCloseItem())) {
         this.player.closeInventory();
      } else if (this.clickedItem.equals(MenuUtils.getBackItem())) {
         VehicleEdit.editMenu(this.player, this.player.getInventory().getItemInMainHand());
      } else {
         String menuItem = (new NBTItem(this.clickedItem)).getString("mtvehicles.item");
         if (menuItem.contains("1")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".acceleratieSpeed", true);
         }

         if (menuItem.contains("2")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeed", true);
         }

         if (menuItem.contains("3")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".brakingSpeed", true);
         }

         if (menuItem.contains("4")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".aftrekkenSpeed", true);
         }

         if (menuItem.contains("5")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".rotateSpeed", true);
         }

         if (menuItem.contains("6")) {
            this.player.closeInventory();
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TYPE_SPEED_IN_CHAT);
            ItemUtils.edit.put(this.player.getUniqueId() + ".maxSpeedBackwards", true);
         }

      }
   }

   private void jerryCanMenu() {
      this.player.getInventory().addItem(new ItemStack[]{this.clickedItem});
   }

   private void voucherRedeemMenu() {
      if (this.clickedSlot == 15) {
         String carUUID = (String)VehicleVoucherListener.voucher.get(this.player);
         if (VehicleUtils.createAndGetItemByUUID(this.player, carUUID) == null) {
            this.player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            this.player.closeInventory();
            return;
         }

         this.player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VOUCHER_REDEEM));
         this.player.getInventory().getItemInMainHand().setAmount(this.player.getInventory().getItemInMainHand().getAmount() - 1);
         this.player.getInventory().addItem(new ItemStack[]{VehicleUtils.createAndGetItemByUUID(this.player, carUUID)});
      }

      VehicleVoucherListener.voucher.remove(this.player);
      this.player.closeInventory();
   }

   private boolean canGetVehicleFromMenu() {
      int owned = ConfigModule.vehicleDataConfig.getNumberOfOwnedVehicles(this.player);
      if (this.player.hasPermission("mtvehicles.nolimit")) {
         return true;
      } else {
         int limit = (Integer)this.player.getEffectivePermissions().stream().filter((permission) -> permission.getPermission().startsWith("mtvehicles.limit.") && permission.getValue()).map((permission) -> {
            try {
               return Integer.parseInt(permission.getPermission().replace("mtvehicles.limit.", ""));
            } catch (NumberFormatException var2) {
               Main.logSevere("An error occurred whilst trying to retrieve player's 'mtvehicles.limit.X' permission.");
               return 0;
            }
         }).findFirst().orElse(-1);
         boolean canGetMore = limit == -1 || limit > owned;
         if (!canGetMore) {
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.TOO_MANY_VEHICLES);
         }

         return canGetMore;
      }
   }
}
