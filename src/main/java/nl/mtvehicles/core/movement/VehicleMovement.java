package nl.mtvehicles.core.movement;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.events.HornUseEvent;
import nl.mtvehicles.core.events.TankShootEvent;
import nl.mtvehicles.core.events.VehicleRegionEnterEvent;
import nl.mtvehicles.core.events.VehicleRegionLeaveEvent;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class VehicleMovement {
   protected Object packet;
   protected Player player;
   protected VehicleType vehicleType;
   protected String license;
   protected ArmorStand standMain;
   protected ArmorStand standSkin;
   protected ArmorStand standMainSeat;
   protected @Nullable ArmorStand standRotors;
   protected boolean isFalling = false;
   protected boolean extremeFalling = false;
   protected boolean headlightsEnabled = false;

   public void vehicleMovement(Player player, Object packet) {
      if (PacketHandler.isObjectPacket(packet)) {
         this.packet = packet;
         this.player = player;
         AtomicLong lastUsed = new AtomicLong(0L);
         if (player.getVehicle() != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof ArmorStand) {
               if (vehicle.getCustomName() != null) {
                  if (!vehicle.getCustomName().replace("MTVEHICLES_MAINSEAT_", "").isEmpty()) {
                     this.license = vehicle.getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
                     if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + this.license) != null) {
                        if (VehicleData.speed.get(this.license) == null) {
                           VehicleData.speed.put(this.license, (double)0.0F);
                        } else {
                           this.vehicleType = (VehicleType)VehicleData.type.get(this.license);
                           if (this.vehicleType != null) {
                              if ((Double)VehicleData.fuel.get(this.license) < (double)1.0F) {
                                 BossBarUtils.setBossBarValue((double)0.0F, this.license);
                                 if (!this.vehicleType.canFly()) {
                                    return;
                                 }

                                 this.isFalling = true;
                                 this.extremeFalling = this.vehicleType.isHelicopter() && (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.EXTREME_HELICOPTER_FALL);
                              }

                              BossBarUtils.setBossBarValue((Double)VehicleData.fuel.get(this.license) / (double)100.0F, this.license);
                              this.standMain = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_MAIN_" + this.license);
                              this.standSkin = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_SKIN_" + this.license);
                              this.standMainSeat = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_MAINSEAT_" + this.license);
                              this.standRotors = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + this.license);
                              if (ConfigModule.vehicleDataConfig.getHealth(this.license) == (double)0.0F) {
                                 this.standMain.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.standMain.getLocation(), 2);
                                 if (!VehicleData.isVehicleDestroyed(this.license)) {
                                    if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.EXPLODING_VEHICLE)) {
                                       Bukkit.getScheduler().runTask(Main.instance, () -> {
                                          this.standMain.getLocation().add((double)0.0F, (double)0.0F, (double)0.0F).createExplosion(2.0F);
                                          VehicleData.markVehicleAsDestroyed(this.license);
                                       });
                                    }

                                    if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DESTRUCTIBLE_VEHICLE)) {
                                       Bukkit.getScheduler().runTask(Main.instance, () -> {
                                          String l = this.license;
                                          VehicleUtils.despawnVehicle(l);
                                          VehicleUtils.getVehicle(l).delete();
                                       });
                                    }
                                 }

                              } else {
                                 if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.HEADLIGHTS_ENABLED) && VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_17)) {
                                    this.headlightsEnabled = true;
                                 }

                                 Main.schedulerRun(() -> {
                                    this.standSkin.teleport(new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY(), this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch()));
                                    if (DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) {
                                       Set<String> newRegions = DependencyModule.worldGuard.getRegionNames(this.standMain.getLocation());
                                       if (VehicleData.lastRegions.containsKey(this.license)) {
                                          Set<String> lastRegions = (Set)VehicleData.lastRegions.get(this.license);
                                          if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.RIDE, this.vehicleType, this.standMain.getLocation(), player)) {
                                             player.getVehicle().eject();
                                             VehicleData.speed.put(this.license, (double)0.0F);
                                             ConfigModule.messagesConfig.sendMessage(player, (Message)Message.CANNOT_DO_THAT_HERE);
                                             return;
                                          }

                                          UnmodifiableIterator var6 = Sets.difference(lastRegions, newRegions).iterator();

                                          while(var6.hasNext()) {
                                             String leftRegion = (String)var6.next();
                                             VehicleRegionLeaveEvent event = new VehicleRegionLeaveEvent(this.license, leftRegion);
                                             event.setPlayer(player);
                                             event.call();
                                             if (event.isCancelled()) {
                                                player.getVehicle().eject();
                                                VehicleData.speed.put(this.license, (double)0.0F);
                                                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                                                   this.standMain.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standMain.getLocation().getY(), player.getLocation().getZ()));
                                                   this.standSkin.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standSkin.getLocation().getY(), player.getLocation().getZ()));
                                                }, 5L);
                                                return;
                                             }
                                          }

                                          var6 = Sets.difference(newRegions, lastRegions).iterator();

                                          while(var6.hasNext()) {
                                             String enteredRegion = (String)var6.next();
                                             VehicleRegionEnterEvent event = new VehicleRegionEnterEvent(this.license, enteredRegion);
                                             event.setPlayer(player);
                                             event.call();
                                             if (event.isCancelled()) {
                                                player.getVehicle().eject();
                                                VehicleData.speed.put(this.license, (double)0.0F);
                                                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                                                   this.standMain.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standMain.getLocation().getY(), player.getLocation().getZ()));
                                                   this.standSkin.teleport(new Location(player.getWorld(), player.getLocation().getX(), this.standSkin.getLocation().getY(), player.getLocation().getZ()));
                                                }, 5L);
                                                return;
                                             }
                                          }
                                       }

                                       VehicleData.lastRegions.put(this.license, newRegions);
                                    }

                                    this.updateStand();
                                    if (!this.vehicleType.isHelicopter()) {
                                       this.blockCheck();
                                    }

                                    this.mainSeat();
                                    if (VehicleData.seatsize.get(this.license + "addon") != null) {
                                       for(int i = 1; i <= (Integer)VehicleData.seatsize.get(this.license + "addon"); ++i) {
                                          ArmorStand standAddon = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_ADDON" + i + "_" + this.license);
                                          standAddon.teleport(this.standMain.getLocation());
                                       }
                                    }

                                    if (this.vehicleType.isHelicopter()) {
                                       this.rotors();
                                    }

                                    if (ConfigModule.vehicleDataConfig.isHornEnabled(this.license) && this.steerIsJumping() && !this.isFalling) {
                                       if (VehicleData.lastUsage.containsKey(player.getName())) {
                                          lastUsed.set((Long)VehicleData.lastUsage.get(player.getName()));
                                       }

                                       if (System.currentTimeMillis() - lastUsed.get() >= Long.parseLong(ConfigModule.defaultConfig.get(DefaultConfig.Option.HORN_COOLDOWN).toString()) * 1000L) {
                                          HornUseEvent api = new HornUseEvent(this.license);
                                          api.setPlayer(player);
                                          Objects.requireNonNull(api);
                                          Main.schedulerRun(api::call);
                                          if (!api.isCancelled()) {
                                             this.standMain.getWorld().playSound(this.standMain.getLocation(), (String)Objects.requireNonNull(ConfigModule.defaultConfig.get(DefaultConfig.Option.HORN_TYPE).toString()), 0.9F, 1.0F);
                                             VehicleData.lastUsage.put(player.getName(), System.currentTimeMillis());
                                          }
                                       }
                                    }

                                    if (this.vehicleType.isTank() && this.steerIsJumping()) {
                                       if (VehicleData.lastUsage.containsKey(player.getName())) {
                                          lastUsed.set((Long)VehicleData.lastUsage.get(player.getName()));
                                       }

                                       if (System.currentTimeMillis() - lastUsed.get() >= Long.parseLong(ConfigModule.defaultConfig.get(DefaultConfig.Option.TANK_COOLDOWN).toString()) * 1000L) {
                                          this.standMain.getWorld().playEffect(this.standMain.getLocation(), Effect.BLAZE_SHOOT, 1, 1);
                                          this.standMain.getWorld().playEffect(this.standMain.getLocation(), Effect.GHAST_SHOOT, 1, 1);
                                          this.standMain.getWorld().playEffect(this.standMain.getLocation(), Effect.WITHER_BREAK_BLOCK, 1, 1);
                                          double xOffset = (double)4.0F;
                                          double yOffset = 1.6;
                                          double zOffset = (double)0.0F;
                                          Location locvp = this.standMain.getLocation().clone();
                                          Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
                                          float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians((double)fbvp.getYaw())));
                                          float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians((double)fbvp.getYaw())));
                                          Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
                                          this.spawnParticles(this.standMain, loc);
                                          this.tankShoot(this.standMain, loc);
                                          VehicleData.lastUsage.put(player.getName(), System.currentTimeMillis());
                                       }
                                    }

                                    if (this.headlightsEnabled && vehicle.getWorld().getTime() >= 13000L && this.vehicleType.isCar() && !vehicle.isEmpty()) {
                                       this.addAndRemoveLight();
                                    }

                                    this.rotation();
                                    this.move();
                                 });
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected void rotation() {
      int rotationSpeed = VehicleData.getRotationSpeed(this.license);
      Location locBelow = new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() - 0.2, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch());
      Material blockTypeBelow = locBelow.getBlock().getType();
      if (!this.isFalling || !this.vehicleType.isHelicopter()) {
         if (!this.vehicleType.isHelicopter() || blockTypeBelow.equals(Material.AIR)) {
            if (ConfigModule.defaultConfig.isIceSlippery() && blockTypeBelow.toString().contains("ICE")) {
               rotationSpeed *= 2;
            }

            if (ConfigModule.defaultConfig.usePlayerFacingDriving()) {
               this.rotateVehicle(this.player.getLocation().getYaw());
            } else {
               int rotation = (Double)VehicleData.speed.get(this.license) < 0.1 ? rotationSpeed / 3 : rotationSpeed;
               if (this.steerGetXxa() > 0.0F) {
                  this.rotateVehicle(this.standMain.getLocation().getYaw() - (float)rotation);
               } else if (this.steerGetXxa() < 0.0F) {
                  this.rotateVehicle(this.standMain.getLocation().getYaw() + (float)rotation);
               }
            }

         }
      }
   }

   protected void rotateVehicle(float yaw) {
      Main.schedulerRun(() -> {
         this.standMain.setRotation(yaw, this.standMain.getLocation().getPitch());
         this.standMainSeat.setRotation(yaw, this.standMain.getLocation().getPitch());
         this.standSkin.setRotation(yaw, this.standMain.getLocation().getPitch());
      });
   }

   protected void move() {
      double maxSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEED, this.license);
      double accelerationSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.ACCELERATION, this.license);
      double brakingSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.BRAKING, this.license);
      double maxSpeedBackwards = VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEEDBACKWARDS, this.license);
      Location locBelow = new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() - 0.2, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch());
      if ((double)this.steerGetZza() == (double)0.0F && !locBelow.getBlock().getType().equals(Material.AIR)) {
         this.putFrictionSpeed();
      }

      if ((double)this.steerGetZza() > (double)0.0F) {
         VehicleData.frictionBlocked.remove(this.license);
         if ((Double)VehicleData.speed.get(this.license) < (double)0.0F) {
            VehicleData.speed.put(this.license, (Double)VehicleData.speed.get(this.license) + brakingSpeed);
            return;
         }

         this.putFuelUsage();
         if ((Double)VehicleData.speed.get(this.license) > maxSpeed - accelerationSpeed) {
            return;
         }

         VehicleData.speed.put(this.license, (Double)VehicleData.speed.get(this.license) + accelerationSpeed);
      }

      if ((double)this.steerGetZza() < (double)0.0F) {
         VehicleData.frictionBlocked.remove(this.license);
         if ((Double)VehicleData.speed.get(this.license) > (double)0.0F) {
            VehicleData.speed.put(this.license, (Double)VehicleData.speed.get(this.license) - brakingSpeed);
            return;
         }

         this.putFuelUsage();
         if ((Double)VehicleData.speed.get(this.license) < -maxSpeedBackwards) {
            return;
         }

         VehicleData.speed.put(this.license, (Double)VehicleData.speed.get(this.license) - accelerationSpeed);
      }

   }

   protected void putFrictionSpeed() {
      double frictionSpeed = VehicleData.getSpeed(VehicleData.DataSpeed.FRICTION, this.license);
      String blockBelowName = (new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() - 0.2, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch())).getBlock().getType().toString();
      if (ConfigModule.defaultConfig.isIceSlippery() && blockBelowName.contains("ICE")) {
         frictionSpeed *= (double)0.5F;
      }

      BigDecimal round = BigDecimal.valueOf((Double)VehicleData.speed.get(this.license)).setScale(1, 1);
      if (Double.parseDouble(String.valueOf(round)) == (double)0.0F) {
         VehicleData.speed.put(this.license, (double)0.0F);
      } else if (Double.parseDouble(String.valueOf(round)) > 0.01) {
         VehicleData.speed.put(this.license, (Double)VehicleData.speed.get(this.license) - frictionSpeed);
      } else {
         if (Double.parseDouble(String.valueOf(round)) < 0.01) {
            VehicleData.speed.put(this.license, (Double)VehicleData.speed.get(this.license) + frictionSpeed);
         }

      }
   }

   @ToDo("Trapdoors")
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
      BlockData blockData = loc.getBlock().getBlockData();
      BlockData blockDataBelow = locBlockBelow.getBlock().getBlockData();
      if (!this.standMain.getLocation().getBlock().getType().toString().contains("PATH") && !this.standMain.getLocation().getBlock().getType().toString().contains("FARMLAND")) {
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
         } else if (blockData instanceof Snow) {
            int layers = ((Snow)blockData).getLayers();
            double layerHeight = this.getLayerHeight(layers);
            if ((Double)VehicleData.speed.get(this.license) > 0.1) {
               VehicleData.speed.put(this.license, 0.1);
            }

            if (layerHeight == difference) {
               return false;
            } else {
               double snowDifference = layerHeight - difference;
               this.pushVehicleUp(snowDifference);
               return true;
            }
         } else if (!(blockData instanceof Fence) && !loc.getBlock().getType().toString().contains("WALL") && !(blockData instanceof TrapDoor)) {
            if (ConfigModule.defaultConfig.isHoneySlowdownEnabled() && locBlockBelow.getBlock().getType() == Material.HONEY_BLOCK) {
               if ((Double)VehicleData.speed.get(this.license) > 0.05) {
                  VehicleData.speed.put(this.license, Math.max((Double)VehicleData.speed.get(this.license) * 0.2, 0.05));
               }

               return false;
            } else {
               if (ConfigModule.defaultConfig.isIceSlippery() && locBlockBelow.getBlock().getType().toString().contains("ICE") && (Double)VehicleData.speed.get(this.license) > 0.05) {
                  VehicleData.speed.put(this.license, Math.max((Double)VehicleData.speed.get(this.license) * 1.1, VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEED, this.license) * 1.2));
               }

               if (ConfigModule.defaultConfig.driveUpSlabs().isSlabs()) {
                  if (isOnSlab) {
                     if (isPassable) {
                        this.pushVehicleDown((double)0.5F);
                        return false;
                     }

                     if (blockData instanceof Slab) {
                        Slab slab = (Slab)blockData;
                        if (slab.getType().equals(Type.BOTTOM)) {
                           return false;
                        }
                     }

                     if (!isAbovePassable) {
                        VehicleData.speed.put(this.license, (double)0.0F);
                        return false;
                     }

                     this.pushVehicleUp((double)0.5F);
                     return true;
                  }

                  if (!isPassable) {
                     if (blockData instanceof Slab) {
                        Slab slab = (Slab)blockData;
                        if (slab.getType().equals(Type.BOTTOM)) {
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
                     }

                     VehicleData.speed.put(this.license, (double)0.0F);
                     return false;
                  }
               } else {
                  if (ConfigModule.defaultConfig.driveUpSlabs().isBlocks()) {
                     if (!isOnSlab && !isPassable) {
                        if (blockData instanceof Slab) {
                           Slab slab = (Slab)blockData;
                           if (slab.getType().equals(Type.BOTTOM)) {
                              VehicleData.speed.put(this.license, (double)0.0F);
                              return false;
                           }
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

                     if (blockData instanceof Slab) {
                        Slab slab = (Slab)blockData;
                        if (slab.getType().equals(Type.BOTTOM)) {
                           return false;
                        }
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

                        if (blockData instanceof Slab) {
                           Slab slab = (Slab)blockData;
                           if (slab.getType().equals(Type.BOTTOM)) {
                              return false;
                           }
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

                        if (blockData instanceof Slab) {
                           Slab slab = (Slab)blockData;
                           if (slab.getType().equals(Type.BOTTOM)) {
                              if (isOnGround) {
                                 this.pushVehicleUp((double)0.5F);
                              } else if ((double)0.5F - difference > (double)0.0F) {
                                 this.pushVehicleUp((double)0.5F - difference);
                              }

                              return true;
                           }
                        }

                        if (isOnGround) {
                           this.pushVehicleUp((double)1.0F);
                        } else if ((double)1.0F - difference > (double)0.0F) {
                           this.pushVehicleUp((double)1.0F - difference);
                        }

                        return true;
                     }

                     if (blockDataBelow instanceof Slab) {
                        Slab slab = (Slab)blockDataBelow;
                        if (slab.getType().equals(Type.BOTTOM)) {
                           this.pushVehicleDown((double)0.5F);
                           return false;
                        }
                     }
                  }
               }

               return false;
            }
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

   protected double getLayerHeight(int layers) {
      switch (layers) {
         case 1:
            return (double)0.125F;
         case 2:
            return (double)0.25F;
         case 3:
            return (double)0.375F;
         case 4:
            return (double)0.5F;
         case 5:
            return (double)0.625F;
         case 6:
            return (double)0.75F;
         case 7:
            return (double)0.875F;
         default:
            return (double)1.0F;
      }
   }

   protected void mainSeat() {
      if (VehicleData.seatsize.get(this.license) != null) {
         for(int i = 2; i <= (Integer)VehicleData.seatsize.get(this.license); ++i) {
            ArmorStand seatas = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + this.license);
            double xOffset = (Double)VehicleData.seatx.get("MTVEHICLES_SEAT" + i + "_" + this.license);
            double yOffset = (Double)VehicleData.seaty.get("MTVEHICLES_SEAT" + i + "_" + this.license);
            double zOffset = (Double)VehicleData.seatz.get("MTVEHICLES_SEAT" + i + "_" + this.license);
            Location locvp = this.standMain.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
            float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians((double)fbvp.getYaw())));
            float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians((double)fbvp.getYaw())));
            Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
            this.teleportSeat(seatas, loc);
         }
      }

      double xOffset = (Double)VehicleData.mainx.get("MTVEHICLES_MAINSEAT_" + this.license);
      double yOffset = (Double)VehicleData.mainy.get("MTVEHICLES_MAINSEAT_" + this.license);
      double zOffset = (Double)VehicleData.mainz.get("MTVEHICLES_MAINSEAT_" + this.license);
      Location locvp = this.standMain.getLocation().clone();
      Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
      float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians((double)fbvp.getYaw())));
      float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians((double)fbvp.getYaw())));
      Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
      this.teleportSeat(this.standMainSeat, loc);
   }

   @VersionSpecific
   protected void teleportSeat(ArmorStand seat, Location loc) {
      if (VersionModule.getServerVersion().is1_12()) {
         this.teleportSeat(((CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_13()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_15()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_16()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_17()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_18_R1()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_18_R2()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_19()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_19_R2()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_19_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_19_R3()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_20_R1()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_20_R2()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_20_R3()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_20_R4()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_21_R1()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_21_R2()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_21_R3()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_21_R4()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_21_R5()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      } else if (VersionModule.getServerVersion().is1_21_R6()) {
         this.teleportSeat(((org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntity)seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
      }

   }

   @VersionSpecific
   protected static String getTeleportMethod() {
      return VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_18_R1) ? "a" : "setLocation";
   }

   protected void teleportSeat(Object seat, double x, double y, double z, float yaw, float pitch) {
      Main.schedulerRun(() -> {
         try {
            Method method = seat.getClass().getSuperclass().getSuperclass().getDeclaredMethod(getTeleportMethod(), Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
            method.invoke(seat, x, y, z, yaw, pitch);
         } catch (Exception e) {
            e.printStackTrace();
         }

      });
   }

   protected void updateStand() {
      Location loc = this.standMain.getLocation();
      Location locBlockAhead = this.getLocationOfBlockAhead();
      Location locBlockAheadAndBelow = new Location(locBlockAhead.getWorld(), locBlockAhead.getX(), locBlockAhead.getY() - (double)1.0F, locBlockAhead.getZ(), locBlockAhead.getPitch(), locBlockAhead.getYaw());
      Location locBelow = new Location(loc.getWorld(), loc.getX(), loc.getY() - 0.2, loc.getZ(), loc.getYaw(), loc.getPitch());
      Material block = locBelow.getBlock().getType();
      String blockName = block.toString();
      boolean space = !this.isFalling && this.steerIsJumping();
      if (this.vehicleType.canFly()) {
         if (this.vehicleType.isAirplane() && this.isFalling && !block.equals(Material.AIR)) {
            this.putFrictionSpeed();
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), (double)0.0F, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
         } else {
            if (this.vehicleType.isHelicopter() && !this.isPassable(locBelow.getBlock()) || this.vehicleType.isAirplane() && (Double)VehicleData.fuel.get(this.license) < (double)1.0F && !block.equals(Material.AIR)) {
               VehicleData.speed.put(this.license, (double)0.0F);
            }

            if (space) {
               double takeOffSpeed = (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.TAKE_OFF_SPEED) > (double)0.0F ? (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.TAKE_OFF_SPEED) : 0.4;
               if (this.vehicleType.isAirplane() && (Double)VehicleData.speed.get(this.license) < takeOffSpeed) {
                  double y = this.isPassable(locBelow.getBlock()) ? -0.2 : (double)0.0F;
                  this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), y, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
               } else {
                  this.putFuelUsage();
                  if (!(loc.getY() > (double)(Integer)ConfigModule.defaultConfig.get(DefaultConfig.Option.MAX_FLYING_HEIGHT))) {
                     this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), 0.2, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
                  }
               }
            } else if (this.extremeFalling) {
               if (this.standMain.isOnGround() && VehicleData.fallDamage.get(this.license) == null) {
                  double damageAmount = (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.HELICOPTER_FALL_DAMAGE) <= (double)0.0F ? (Double)DefaultConfig.Option.HELICOPTER_FALL_DAMAGE.getDefaultValue() : (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.HELICOPTER_FALL_DAMAGE);
                  Main.schedulerRun(() -> {
                     this.player.damage(damageAmount);
                     if (VehicleData.seatsize.get(this.license) != null) {
                        for(int i = 2; i <= (Integer)VehicleData.seatsize.get(this.license); ++i) {
                           ArmorStand seat = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + this.license);

                           for(Entity p : seat.getPassengers()) {
                              if (p instanceof LivingEntity) {
                                 ((LivingEntity)p).damage(damageAmount);
                              }
                           }
                        }
                     }

                  });
                  VehicleData.fallDamage.put(this.license, true);
               }

               this.standMain.setGravity(true);
            } else {
               this.putFuelUsage();
               this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), -0.2, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
            }
         }
      } else if (this.vehicleType.isHover()) {
         if (block.equals(Material.AIR)) {
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), -0.8, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
         } else {
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), 1.0E-5, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
         }
      } else if (this.vehicleType.isBoat()) {
         if (!this.boatPassable(blockName) && !this.isPassable(locBelow.getBlock())) {
            VehicleData.speed.put(this.license, (double)0.0F);
         }

         if (this.isPassable(locBelow.getBlock()) && !this.boatPassable(blockName)) {
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), -0.8, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
         } else {
            this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), 0.01, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
         }
      } else if (blockName.contains("WATER")) {
         this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), -0.8, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
      } else {
         if (this.isPassable(locBlockAhead.getBlock()) && this.isPassable(locBlockAheadAndBelow.getBlock())) {
            if (this.isPassable(locBelow.getBlock())) {
               this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), -0.8, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
               return;
            }

            if (blockName.contains("CARPET")) {
               this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), -0.7375, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
               return;
            }
         }

         this.standMain.setVelocity(new Vector(loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getX(), (double)0.0F, loc.getDirection().multiply((Double)VehicleData.speed.get(this.license)).getZ()));
      }
   }

   protected void putFuelUsage() {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (Boolean)ConfigModule.vehicleDataConfig.get(this.license, VehicleDataConfig.Option.FUEL_ENABLED)) {
         double fuelMultiplier = Double.parseDouble(ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_MULTIPLIER).toString());
         if (fuelMultiplier < 0.1 || fuelMultiplier > (double)10.0F) {
            fuelMultiplier = (double)1.0F;
         }

         double newFuel = (Double)VehicleData.fuel.get(this.license) - fuelMultiplier * (Double)VehicleData.fuelUsage.get(this.license);
         if (newFuel < (double)0.0F) {
            VehicleData.fuel.put(this.license, (double)0.0F);
         } else {
            VehicleData.fuel.put(this.license, newFuel);
         }

      }
   }

   protected boolean isPassable(Block block) {
      return block.isPassable();
   }

   protected void rotors() {
      double xOffset = (Double)VehicleData.wiekenx.get("MTVEHICLES_WIEKENS_" + this.license);
      double yOffset = (Double)VehicleData.wiekeny.get("MTVEHICLES_WIEKENS_" + this.license);
      double zOffset = (Double)VehicleData.wiekenz.get("MTVEHICLES_WIEKENS_" + this.license);
      Location locvp = this.standMain.getLocation().clone();
      Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
      float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians((double)this.standRotors.getLocation().getYaw())));
      float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians((double)this.standRotors.getLocation().getYaw())));
      float yawAdd = this.isFalling ? 5.0F : 15.0F;
      if (this.extremeFalling) {
         yawAdd = 0.0F;
      }

      Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, this.standRotors.getLocation().getYaw() + yawAdd, this.standRotors.getLocation().getPitch());
      Main.schedulerRun(() -> this.standRotors.teleport(loc));
   }

   protected void pushVehicleUp(double plus) {
      Location newLoc = new Location(this.standMain.getLocation().getWorld(), this.standMain.getLocation().getX(), this.standMain.getLocation().getY() + plus, this.standMain.getLocation().getZ(), this.standMain.getLocation().getYaw(), this.standMain.getLocation().getPitch());
      Main.schedulerRun(() -> this.standMain.teleport(newLoc));
   }

   protected void pushVehicleDown(double minus) {
      this.pushVehicleUp(-minus);
   }

   protected Location getLocationOfBlockAhead() {
      double xOffset = 0.7;
      double yOffset = 0.4;
      double zOffset = (double)0.0F;
      Location locvp = this.standMain.getLocation().clone();
      Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
      float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians((double)fbvp.getYaw())));
      float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians((double)fbvp.getYaw())));
      return new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
   }

   protected boolean steerIsJumping() {
      boolean isJumping = false;

      try {
         if (VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_21_R1)) {
            String declaredMethod = "d";
            if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R2) && VersionModule.getServerVersion().isOlderThan(ServerVersion.v1_20_R4)) {
               declaredMethod = "e";
            } else if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R4)) {
               declaredMethod = "f";
            }

            Method method = this.packet.getClass().getDeclaredMethod(declaredMethod);
            isJumping = (Boolean)method.invoke(this.packet);
         } else {
            Object input = this.packet.getClass().getDeclaredMethod("b").invoke(this.packet);
            isJumping = (Boolean)input.getClass().getDeclaredMethod("e").invoke(input);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return isJumping;
   }

   protected float steerGetXxa() {
      float Xxa = 0.0F;

      try {
         if (VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_21_R1)) {
            String declaredMethod = "b";
            if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_19_R3) && VersionModule.getServerVersion().isOlderThan(ServerVersion.v1_20_R4)) {
               declaredMethod = "a";
            }

            Method method = this.packet.getClass().getDeclaredMethod(declaredMethod);
            Xxa = (Float)method.invoke(this.packet);
         } else {
            Object input = this.packet.getClass().getDeclaredMethod("b").invoke(this.packet);
            if ((Boolean)input.getClass().getDeclaredMethod("c").invoke(input)) {
               Xxa = 1.0F;
            } else if ((Boolean)input.getClass().getDeclaredMethod("d").invoke(input)) {
               Xxa = -1.0F;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return Xxa;
   }

   protected float steerGetZza() {
      float Zza = 0.0F;

      try {
         if (VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_21_R1)) {
            String declaredMethod = "c";
            if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R2) && VersionModule.getServerVersion().isOlderThan(ServerVersion.v1_20_R4)) {
               declaredMethod = "d";
            } else if (VersionModule.getServerVersion().isNewerOrEqualTo(ServerVersion.v1_20_R4)) {
               declaredMethod = "e";
            }

            Method method = this.packet.getClass().getDeclaredMethod(declaredMethod);
            Zza = (Float)method.invoke(this.packet);
         } else {
            Object input = this.packet.getClass().getDeclaredMethod("b").invoke(this.packet);
            if ((Boolean)input.getClass().getDeclaredMethod("a").invoke(input)) {
               Zza = 1.0F;
            } else if ((Boolean)input.getClass().getDeclaredMethod("b").invoke(input)) {
               Zza = -1.0F;
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return Zza;
   }

   @VersionSpecific
   protected void spawnParticles(ArmorStand stand, Location loc) {
      stand.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 2);
      stand.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 2);
      stand.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 5);
      if (!VersionModule.getServerVersion().isOlderOrEqualTo(ServerVersion.v1_13)) {
         stand.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 5);
      }

   }

   public void tankShoot(ArmorStand stand, Location loc) {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.TANK_TNT)) {
         TankShootEvent api = new TankShootEvent(this.license);
         api.setPlayer(this.player);
         Objects.requireNonNull(api);
         Main.schedulerRun(api::call);
         if (!api.isCancelled()) {
            Main.schedulerRun(() -> {
               TNTPrimed tnt = (TNTPrimed)loc.getWorld().spawn(loc, TNTPrimed.class);
               tnt.setFuseTicks(20);
               tnt.setVelocity(stand.getLocation().getDirection().multiply((double)3.0F));
            });
         }
      }
   }

   public boolean boatPassable(String blockName) {
      return blockName.contains("WATER") || blockName.contains("SEAGRASS") || blockName.contains("KELP") || blockName.contains("CORAL") || blockName.contains("PICKLE");
   }

   private void addAndRemoveLight() {
      double xOffset = 0.7;
      double yOffset = 0.4;
      double zOffset = (double)0.0F;
      Location locvp = this.standMain.getLocation().clone();
      Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset).multiply(2));
      float zvp = (float)(fbvp.getZ() + zOffset * Math.sin(Math.toRadians((double)fbvp.getYaw())));
      float xvp = (float)(fbvp.getX() + zOffset * Math.cos(Math.toRadians((double)fbvp.getYaw())));
      Location loc = new Location(this.standMain.getWorld(), (double)xvp, this.standMain.getLocation().getY() + yOffset, (double)zvp, fbvp.getYaw(), fbvp.getPitch());
      if (loc.getBlock().getType().equals(Material.AIR)) {
         loc.getBlock().setType(Material.LIGHT);
         Bukkit.getScheduler().runTaskLater(Main.instance, () -> loc.getBlock().setType(Material.AIR), 10L);
      }

   }
}
