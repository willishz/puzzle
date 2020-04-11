package com.willishz.puzzle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.houbb.word.checker.util.EnWordCheckers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public final class Cryptography {

    public static void main(String[] args) throws JsonProcessingException {
        String crpyt = null;
//        crpyt = "speling";
//        System.out.println(EnWordCheckers.isCorrect(crpyt));
//        crpyt = "4 16 15 8 19 2 21 22 13 2 21 10 16 15";
//        crpyt = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26";
//        System.out.println(objectMapper.writeValueAsString(Cryptography.alphabet(crpyt, true)));
//        crpyt = "2-3 3-3 1-1 4-3 3-5 2-4 4-2 4-4 3-4 3-2 4-5 4-5";
//        System.out.println(objectMapper.writeValueAsString(Cryptography.polybius(crpyt)));
        crpyt = "1627384950";
        System.out.println(objectMapper.writeValueAsString(Cryptography.railFence(crpyt)));
    }

    /**
     * numberic to alphabet with offset
     *
     * @param str
     * @return
     */
    public static List<String> alphabet(String str, boolean check) {
        List<String> result = new ArrayList();
        StringBuilder tmp = null;
        for (int offset = -25; offset <= 25; offset++) {
            tmp = new StringBuilder();
            for (String each : Cryptography.smartSplit(str)) {
                char c = (char) (Integer.parseInt(each) + offset + 96); // a-z 97-122
                if (c < 97) {
                    c += 26; // 26 alphabets loop
                } else if (c > 122) {
                    c -= 26; // 26 alphabets loop
                }
                tmp.append(c);
            }
            if (check) {
                tmp.append(" " + EnWordCheckers.isCorrect(tmp.toString()));
            }
            result.add(tmp.toString());
        }
        return result;
    }

    public static String[] polybius(String str) {
        StringBuilder result1 = new StringBuilder();
        StringBuilder result2 = new StringBuilder();
        String[] cipher = Cryptography.smartSplit(str.replaceAll("-", ""));
        for (String each : cipher) {
            result1.append(POLYBIUS[Integer.parseInt(each.substring(0, 1)) - 1][Integer.parseInt(each.substring(1, 2)) - 1]);
        }
        for (String each : cipher) {
            result2.append(POLYBIUS[Integer.parseInt(each.substring(1, 2)) - 1][Integer.parseInt(each.substring(0, 1)) - 1]);
        }
        return new String[]{result1.toString(), result2.toString()};
    }

    public static String[] smartSplit(String str) {
        List<String> delimiter = Arrays.asList("\n", " ", "\t", "-", "_");
        for (String each : delimiter) {
            if (str.contains(" ")) {
                return str.split(" ");
            }
        }
        return new String[]{str};
    }

    public static List<String> railFence(String str) {
        List<String> result = new ArrayList();
        StringBuilder tmp = null;
        for (int offset : new int[]{2, str.length() / 2}) {
            tmp = new StringBuilder();
            for (int loop = 0; loop < offset; loop++) {
                for (int i = loop; i < str.length(); i += offset) {
                    tmp.append(str.charAt(i));
                }
            }
            result.add(tmp.toString());
        }
        return result;
    }

    public static char[][] POLYBIUS = new char[][]{
            new char[]{'a', 'b', 'c', 'd', 'e'},
            new char[]{'f', 'g', 'h', 'i', 'k'},
            new char[]{'l', 'm', 'n', 'o', 'p'},
            new char[]{'q', 'r', 's', 't', 'u'},
            new char[]{'v', 'w', 'x', 'y', 'z'}
    };

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 转换为格式化的json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(new SimpleDateFormat(DATETIME_FORMAT))
                .writerWithDefaultPrettyPrinter();
    }

}
