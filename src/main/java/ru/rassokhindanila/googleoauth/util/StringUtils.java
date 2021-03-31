package ru.rassokhindanila.googleoauth.util;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utils for some operations on String
 */
public class StringUtils{

    /**
     * @param text Text with variables
     * @param replacements Map of variables and their values to replace
     * @return Text with replaced variables
     */
    public static String replaceVariables(String text, HashMap<String, String> replacements)
    {
        AtomicReference<String> newText = new AtomicReference<>();
        newText.set(text);
        replacements.forEach((var, val) -> {
            String replace = newText.get().replaceAll("\\$"+var+"\\$", val);
            newText.set(replace);
        });
        return newText.get();
    }

}
