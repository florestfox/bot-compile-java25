package nl.mtvehicles.core.infrastructure.dataconfig;

import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;

public class SecretSettingsConfig extends MTVConfig {
   public SecretSettingsConfig() {
      super(ConfigType.SUPERSECRETSETTINGS);
   }

   public String getConfigVersion() {
      return this.getConfiguration().getString("configVersion");
   }

   public String getMessagesVersion() {
      return this.getConfiguration().getString("messagesVersion");
   }

   public String getMessagesLanguage() {
      return this.getConfiguration().getString("messagesLanguage");
   }

   public void setMessagesLanguage(Language language) throws IllegalArgumentException {
      if (language == Language.CUSTOM) {
         throw new IllegalArgumentException("CUSTOM language can't be used in this method.");
      } else {
         String languageCode = language.getLanguageCode();
         this.getConfiguration().set("messagesLanguage", languageCode);
         this.save();
      }
   }
}
