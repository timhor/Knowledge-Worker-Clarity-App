package helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class SharedComponents {

    // date picker formatting adapted from:
    // https://code.makery.ch/blog/javafx-8-date-picker/
    private static StringConverter<LocalDate> datePickerConverter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    public static StringConverter<LocalDate> getDatePickerConverter() {
        return datePickerConverter;
    }

}
