package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class MTVCommand implements CommandExecutor {
   public CommandSender commandSender;

   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
      this.commandSender = commandSender;
      return this.execute(commandSender, command, label, strings);
   }

   public abstract boolean execute(CommandSender var1, Command var2, String var3, String[] var4);

   public void sendMessage(String message) {
      this.commandSender.sendMessage(TextUtils.colorize(message));
   }
}
