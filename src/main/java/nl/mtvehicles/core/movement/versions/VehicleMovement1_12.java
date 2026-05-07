package nl.mtvehicles.core.movement.versions;

import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.movement.VehicleMovement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class VehicleMovement1_12 extends VehicleMovement {
   protected boolean blockCheck() {
      Location loc = this.getLocationOfBlockAhead();
      String locY = String.valueOf(this.standMain.getLocation().getY());
      Location locBlockAbove = new Location(loc.getWorld(), loc.getX(), loc.getY() + (double)1.0F, loc.getZ(), loc.getYaw(), loc.getPitch());
      Location locBlockBelow = new Location(loc.getWorld(), loc.getX(), loc.getY() - (double)1.0F, loc.getZ(), loc.getYaw(), loc.getPitch());
      String drivingOnY = locY.substring(locY.length() - 2);
      boolean isOnGround = drivingOnY.contains(".0");
      boolean isOnSlab = drivingOnY.contains(".5");
      boolean isPassable = this.isPassable(loc.getBlock());
      boolean isAbovePassable = this.isPassable(locBlockAbove.getBlock());
      double difference = Double.parseDouble("0." + locY.split("\\.")[1]);
      int data = loc.getBlock().getData();
      int dataBelow = locBlockBelow.getBlock().getData();
      if (this.vehicleType.isBoat()) {
         if (!locBlockBelow.getBlock().getType().toString().contains("WATER")) {
            VehicleData.speed.put(this.license, (double)0.0F);
            return false;
         } else {
            return false;
         }
      } else if (!this.standMain.getLocation().getBlock().getType().toString().contains("PATH") && !this.standMain.getLocation().getBlock().getType().toString().contains("FARMLAND")) {
         if (loc.getBlock().getType().toString().contains("CARPET")) {
            if (!(Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DRIVE_ON_CARPETS)) {
               VehicleData.speed.put(this.license, (double)0.0F);
               return false;
            } else if (!isAbovePassable) {
               VehicleData.speed.put(this.license, (double)0.0F);
               return false;
            } else {
               if (isOnGround) {
                  this.pushVehicleUp((double)0.0625F);
               }

               return true;
            }
         } else if (loc.getBlock().getType().toString().contains("SNOW") && !loc.getBlock().getType().toString().contains("SNOW_BLOCK")) {
            int layers = data + 1;
            double layerHeight = this.getLayerHeight(layers);
            if ((Double)VehicleData.speed.get(this.license) > 0.1) {
               VehicleData.speed.put(this.license, 0.1);
            }

            if (layerHeight == difference) {
               return false;
            } else {
               double snowDifference = layerHeight - difference;
               this.pushVehicleUp(snowDifference);
               return false;
            }
         } else if (!loc.getBlock().getType().toString().contains("FENCE") && !loc.getBlock().getType().toString().contains("WALL") && !loc.getBlock().getType().toString().contains("TRAPDOOR") && !loc.getBlock().getType().toString().contains("TRAP_DOOR")) {
            if (ConfigModule.defaultConfig.driveUpSlabs().isSlabs()) {
               if (isOnSlab) {
                  if (isPassable) {
                     this.pushVehicleDown((double)0.5F);
                     return false;
                  }

                  if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
                     return false;
                  }

                  if (!isAbovePassable) {
                     VehicleData.speed.put(this.license, (double)0.0F);
                     return false;
                  }

                  this.pushVehicleUp((double)0.5F);
                  return true;
               }

               if (!isPassable) {
                  if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
                     if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, (double)0.0F);
                        return false;
                     }

                     if (isOnGround) {
                        this.pushVehicleUp((double)0.5F);
                     } else if ((double)0.5F - difference > (double)0.0F) {
                        this.pushVehicleUp((double)0.5F - difference);
                     }
                  }

                  VehicleData.speed.put(this.license, (double)0.0F);
                  return false;
               }
            } else {
               if (ConfigModule.defaultConfig.driveUpSlabs().isBlocks()) {
                  if (!isOnSlab && !isPassable) {
                     if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
                        VehicleData.speed.put(this.license, (double)0.0F);
                        return false;
                     }

                     if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, (double)0.0F);
                        return false;
                     }

                     if (isOnGround) {
                        this.pushVehicleUp((double)1.0F);
                     } else if ((double)1.0F - difference > (double)0.0F) {
                        this.pushVehicleUp((double)1.0F - difference);
                     }

                     return true;
                  }

                  if (isPassable) {
                     this.pushVehicleDown((double)0.5F);
                     return false;
                  }

                  if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
                     return false;
                  }

                  if (!isAbovePassable) {
                     VehicleData.speed.put(this.license, (double)0.0F);
                     return false;
                  }

                  this.pushVehicleUp((double)0.5F);
                  return true;
               }

               if (ConfigModule.defaultConfig.driveUpSlabs().isBoth()) {
                  if (isOnSlab) {
                     if (isPassable) {
                        this.pushVehicleDown((double)0.5F);
                        return false;
                     }

                     if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
                        return false;
                     }

                     if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, (double)0.0F);
                        return false;
                     }

                     this.pushVehicleUp((double)0.5F);
                     return true;
                  }

                  if (!isPassable) {
                     if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, (double)0.0F);
                        return false;
                     }

                     if ((loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) && !loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) {
                        if (isOnGround) {
                           this.pushVehicleUp((double)0.5F);
                        } else if ((double)0.5F - difference > (double)0.0F) {
                           this.pushVehicleUp((double)0.5F - difference);
                        }

                        return true;
                     }

                     if (isOnGround) {
                        this.pushVehicleUp((double)1.0F);
                     } else if ((double)1.0F - difference > (double)0.0F) {
                        this.pushVehicleUp((double)1.0F - difference);
                     }

                     return true;
                  }

                  if ((locBlockBelow.getBlock().getType().toString().contains("STEP") || locBlockBelow.getBlock().getType().toString().contains("SLAB")) && !locBlockBelow.getBlock().getType().toString().contains("DOUBLE") && dataBelow < 9) {
                     this.pushVehicleDown((double)0.5F);
                     return false;
                  }
               }
            }

            return false;
         } else {
            VehicleData.speed.put(this.license, (double)0.0F);
            return false;
         }
      } else if (!isAbovePassable) {
         VehicleData.speed.put(this.license, (double)0.0F);
         return false;
      } else if (!loc.getBlock().getType().toString().contains("PATH") && !loc.getBlock().getType().toString().contains("FARMLAND")) {
         this.pushVehicleUp((double)0.0625F);
         return true;
      } else {
         return false;
      }
   }

   protected boolean isPassable(Block block) {
      String blockName = block.getType().toString();
      return blockName.contains("AIR") || blockName.contains("FLOWER") || blockName.contains("ROSE") || blockName.contains("PLANT") || block.getType().equals(Material.BROWN_MUSHROOM) || block.getType().equals(Material.RED_MUSHROOM) || blockName.contains("LONG_GRASS") || blockName.contains("SAPLING") || blockName.contains("DEAD_BUSH") || blockName.contains("TORCH") || blockName.contains("BANNER");
   }

   protected void rotateVehicle(float yaw) {
      Location loc = this.standMain.getLocation();
      this.standMain.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
      this.standMainSeat.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
      this.standSkin.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
   }

   protected boolean steerIsJumping() {
      PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)this.packet;
      return ppisv.c();
   }

   protected float steerGetXxa() {
      PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)this.packet;
      return ppisv.a();
   }

   protected float steerGetZza() {
      PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle)this.packet;
      return ppisv.b();
   }
}
