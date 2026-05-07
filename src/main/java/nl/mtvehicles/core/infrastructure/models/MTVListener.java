package nl.mtvehicles.core.infrastructure.models;

import javax.annotation.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public abstract class MTVListener implements Listener {
   protected Event event;
   @Nullable
   protected Player player;
   private MTVEvent api;

   protected MTVListener() {
      this.api = null;
   }

   protected MTVListener(MTVEvent api) {
      this.api = api;
   }

   protected boolean isCancelled() {
      if (this.event == null) {
         throw new NullPointerException("Cannot check if event is cancelled if event is null.");
      } else {
         return this.getAPI().isCancelled();
      }
   }

   protected void setAPI(MTVEvent event) {
      this.api = event;
   }

   protected MTVEvent getAPI() {
      if (this.api == null) {
         throw new NullPointerException("Event API not specified for this listener.");
      } else {
         return this.api;
      }
   }

   protected void callAPI() {
      if (this.api == null) {
         throw new NullPointerException("Event API not specified for this listener.");
      } else {
         this.api.setPlayer(this.player);
         this.api.call();
      }
   }

   protected void callAPI(@Nullable Player player) throws NullPointerException {
      if (this.api == null) {
         throw new NullPointerException("Event API not specified for this listener.");
      } else {
         this.api.setPlayer(player);
         this.api.call();
      }
   }
}
