package utils;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Utils {

    public static String toString(InputStream is) {
        try {
            return IOUtils.toString(is, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object fromJsonWithDate(String bodyString, Class classe) {
        GsonBuilder gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        new LocalDateSerializer())
                .registerTypeAdapter(LocalDateTime.class,
                        new LocalDateTimeSerializer());
        return gson.create().fromJson(bodyString, classe);
    }

    public static <T> T checkNotNull(T reference, String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        } else {
            return reference;
        }
    }

    public static <T> T checkParameterNotNull(T reference, String paramName) {
        if (reference == null) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' not found");
        } else {
            return reference;
        }
    }
}
