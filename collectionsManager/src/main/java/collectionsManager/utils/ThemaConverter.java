package collectionsManager.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import collectionsManager.model.Thema;

@Converter(autoApply = true)
public class ThemaConverter implements AttributeConverter<Thema, String> {
   @Override
   public String convertToDatabaseColumn(Thema thema) {
       return thema.getThema();
   }

   @Override
   public Thema convertToEntityAttribute(String thema) {
       for (Thema th : Thema.values()) {
           if (th.getThema().equals(thema)) {
               return th;
           }
       }
       throw new IllegalArgumentException("Unknown thema " + thema);
   }
}