package ru.plastikam.mail.misc;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static String rget(String text, String... regexp) {

        for (int i = 0; i < regexp.length; i++) {
            String r = regexp[i];

            Pattern pattern = Pattern.compile(r);

            String[] separated = text.split("\\n");

            for (int j = 0; j < separated.length; j++) {
                String s = StringUtils.normalizeSpace(separated[j]);
                if (s.isEmpty())
                    continue;
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches()) {
                    return StringUtils.normalizeSpace(matcher.group(1));
                }
            }

        }

        return "";
    }

}
