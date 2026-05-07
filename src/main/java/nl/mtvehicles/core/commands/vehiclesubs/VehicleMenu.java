package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import nl.mtvehicles.core.events.inventory.VehicleMenuOpenEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemFactory;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class VehicleMenu extends MTVSubCommand {
   public static HashMap<UUID, Inventory> beginMenu = new HashMap();

   public VehicleMenu() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.menu")) {
         return true;
      } else {
         this.sendMessage(Message.MENU_OPEN);
         int menuRows = (Integer)ConfigModule.defaultConfig.get(DefaultConfig.Option.VEHICLE_MENU_SIZE);
         int menuSize = menuRows >= 3 && menuRows <= 6 ? menuRows * 9 : 27;
         Inventory inv = Bukkit.createInventory((InventoryHolder)null, menuSize, InventoryTitle.VEHICLE_MENU.getStringTitle());

         for(Map<?, ?> vehicle : ConfigModule.vehiclesConfig.getVehicles()) {
            int itemDamage = (Integer)vehicle.get("itemDamage");
            String name = (String)vehicle.get("name");
            String skinItem = (String)vehicle.get("skinItem");
            ItemStack itemStack = ItemUtils.getMenuVehicle(ItemUtils.getMaterial(skinItem), itemDamage, name);
            if (vehicle.get("nbtValue") == null) {
               inv.addItem(new ItemStack[]{itemStack});
            } else {
               inv.addItem(new ItemStack[]{(new ItemFactory(itemStack)).setNBT((String)vehicle.get("nbtKey"), (String)vehicle.get("nbtValue")).toItemStack()});
            }
         }

         VehicleMenuOpenEvent api = new VehicleMenuOpenEvent(this.player);
         api.call();
         if (api.isCancelled()) {
            return true;
         } else {
            beginMenu.put(this.player.getUniqueId(), inv);
            this.player.openInventory(inv);
            return true;
         }
      }
   }
}
