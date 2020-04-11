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
        System.out.println(EnWordCheckers.isCorrect("speling"));
        String crpyt = "4 16 15 8 19 2 21 22 13 2 21 10 16 15";
//        crpyt = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26";
        System.out.println(objectMapper.writeValueAsString(Cryptography.alphabet(crpyt, true)));
    }

    /**
     * numberic to alphabet with offset
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

    public static String[] smartSplit(String str) {
        List<String> delimiter = Arrays.asList("\n", " ", "\t", "-", "_");
        for (String each : delimiter) {
            if (str.contains(" ")) {
                return str.split(" ");
            }
        }
        return new String[]{str};
    }

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
