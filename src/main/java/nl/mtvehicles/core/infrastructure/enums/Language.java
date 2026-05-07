package nl.mtvehicles.core.infrastructure.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import nl.mtvehicles.core.infrastructure.annotations.LanguageSpecific;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

@LanguageSpecific
public enum Language {
   EN("English"),
   NL("Nederlands"),
   ES("Español"),
   CS("Čeština"),
   DE("Deutsch"),
   CN("中國人"),
   TR("Türk"),
   JA("日本語"),
   HE("עִברִית"),
   RU("Русский"),
   FR("Français"),
   TH("ภาษาไทย"),
   GR("Ελληνική"),
   CUSTOM("Custom language");

   private final String languageName;

   private Language(String languageName) {
      this.languageName = languageName;
   }

   public String getLanguageName() {
      return this.languageName;
   }

   public String getLanguageCode() {
      return this.equals(CUSTOM) ? ConfigModule.secretSettings.getMessagesLanguage() : this.toString().toLowerCase(Locale.ROOT);
   }

   /** @deprecated */
   @Deprecated
   public static String[] getAllLanguages() {
      return (String[])getAllLanguageCodes().toArray();
   }

   public static List<String> getAllLanguageCodes() {
      return (List)Arrays.stream(values()).map(Enum::toString).map(String::toLowerCase).filter((code) -> !code.equals("custom")).collect(Collectors.toList());
   }

   public static boolean isSupported(String languageCode) {
      List<String> languages = getAllLanguageCodes();
      return languages.contains(languageCode.toLowerCase(Locale.ROOT));
   }

   // $FF: synthetic method
   private static Language[] $values() {
      return new Language[]{EN, NL, ES, CS, DE, CN, TR, JA, HE, RU, FR, TH, GR, CUSTOM};
   }
}
