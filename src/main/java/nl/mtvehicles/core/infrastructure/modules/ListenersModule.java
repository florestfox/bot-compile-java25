package nl.mtvehicles.core.infrastructure.modules;

import lombok.Generated;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.listeners.ChatListener;
import nl.mtvehicles.core.listeners.JerryCanClickListener;
import nl.mtvehicles.core.listeners.JoinListener;
import nl.mtvehicles.core.listeners.LeaveListener;
import nl.mtvehicles.core.listeners.TNTSpawnListener;
import nl.mtvehicles.core.listeners.VehicleClickListener;
import nl.mtvehicles.core.listeners.VehicleEntityListener;
import nl.mtvehicles.core.listeners.VehicleLeaveListener;
import nl.mtvehicles.core.listeners.VehicleOtherDamageListener;
import nl.mtvehicles.core.listeners.VehiclePlaceListener;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import nl.mtvehicles.core.listeners.inventory.InventoryClickListener;
import nl.mtvehicles.core.listeners.inventory.InventoryCloseListener;

public class ListenersModule {
   private static ListenersModule instance;

   public ListenersModule() {
      Main.instance.registerListener(new InventoryClickListener());
      Main.instance.registerListener(new VehiclePlaceListener());
      Main.instance.registerListener(new VehicleClickListener());
      Main.instance.registerListener(new VehicleLeaveListener());
      Main.instance.registerListener(new ChatListener());
      Main.instance.registerListener(new VehicleEntityListener());
      Main.instance.registerListener(new VehicleOtherDamageListener());
      Main.instance.registerListener(new JoinListener());
      Main.instance.registerListener(new VehicleVoucherListener());
      Main.instance.registerListener(new InventoryCloseListener());
      Main.instance.registerListener(new JerryCanClickListener());
      Main.instance.registerListener(new TNTSpawnListener());
      Main.instance.registerListener(new LeaveListener());
   }

   @Generated
   public static ListenersModule getInstance() {
      return instance;
   }

   @Generated
   public static void setInstance(ListenersModule instance) {
      ListenersModule.instance = instance;
   }
}
