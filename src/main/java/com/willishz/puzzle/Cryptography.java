package com.willishz.puzzle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.houbb.word.checker.util.EnWordCheckers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple tool try to decrypt simple numberic or alphabet cipher puzzles. List all possible solutions using Caesar, rail fence, Polybius, Vigenère, etc.
 */
public final class Cryptography {

    public static void main(String[] args) throws JsonProcessingException {
        String crpyt = null;
//        crpyt = "speling";
//        System.out.println(EnWordCheckers.isCorrect(crpyt));
//        crpyt = "4 16 15 8 19 2 21 22 13 2 21 10 16 15";
//        crpyt = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26";
//        crpyt = "12 52 22 11 25 25 21 53 12 15 21 34";
//        System.out.println(objectMapper.writeValueAsString(Cryptography.alphabet(crpyt, true)));
        crpyt = "2-5 3-1 5-5 1-1 1-5 2-2 5-1 4-2";
        System.out.println(objectMapper.writeValueAsString(Cryptography.polybius(crpyt)));
//        crpyt = "1627384950";
//        crpyt = "bwgakkfxbefo";
//        crpyt = "fkgawwbpf";
//        crpyt = "sucgwrosfubkbwkegffaxo";
//        System.out.println(objectMapper.writeValueAsString(Cryptography.railFence(crpyt)));
//        System.out.println(objectMapper.writeValueAsString(Cryptography.caesar(crpyt, true)));
//        crpyt = "tuilishe";
//        System.out.println(objectMapper.writeValueAsString(Cryptography.pinyin(crpyt)));
//        crpyt = "vxfjzurvixeneznhjiidar";
//        System.out.println(objectMapper.writeValueAsString(Cryptography.vigenere(crpyt, "key Word")));
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
                tmp.append(" " + wordCheck(tmp.toString()));
            }
            result.add(tmp.toString());
        }
        return result;
    }

    public static List<String> caesar(String str, boolean check) {
        str = str.replaceAll(" ", "").trim().toLowerCase();
        List<String> result = new ArrayList();
        StringBuilder tmp = null;
        for (int offset = -25; offset <= 25; offset++) {
            tmp = new StringBuilder();
            for (char each : str.toLowerCase().toCharArray()) {
                char c = (char) (each + offset); // a-z 97-122
                if (c < 97) {
                    c += 26; // 26 alphabets loop
                } else if (c > 122) {
                    c -= 26; // 26 alphabets loop
                }
                tmp.append(c);
            }
            if (check) {
                tmp.append(" " + wordCheck(tmp.toString()));
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

    public static List<String> railFence(String str) {
        str = str.replaceAll(" ", "").trim().toLowerCase();
        List<String> result = new ArrayList();
        char[] tmp = null;
        for (int offset = 2; offset <= str.length() / 2; offset++) {
            tmp = new char[str.length()];
            int group = (int) Math.ceil(str.length() / offset);
            for (int i = 0; i < offset; i++) {
                for (int loop = 0; loop < group; loop++) {
                    tmp[i + loop * offset] = str.charAt(i * group + loop);
                }
            }
            result.add(new String(tmp));
        }
        return result;
    }

    public static String vigenere(String str, String key) {
        str = str.replaceAll(" ", "").trim().toLowerCase();
        key = key.replaceAll(" ", "").trim().toLowerCase();
        char[] result = new char[str.length()];
        while (key.length() < str.length()) {
            key += key;
        }
        key = key.substring(0, str.length());
        for (int i = 0; i < str.length(); i++) {
            int tmp = str.charAt(i) - (key.charAt(i) - 'a');
            if (tmp > 122) {
                tmp -= 26;
            } else if (tmp < 97) {
                tmp += 26;
            }
            result[i] = (char) tmp;
        }
        return new String(result);
    }

    /**
     * check spelling or chinese pinyin
     *
     * @param str
     * @return
     */
    private static boolean wordCheck(String str) {
        return EnWordCheckers.isCorrect(str) || isPinyin(str);
    }

    /**
     * check is chinese pinyin
     *
     * @param str
     * @return
     */
    private static boolean isPinyin(String str) {
        return pinyin(str).size() > 1;
    }

    /**
     * split to chinese pinyin
     *
     * @param str
     * @return
     */
    private static List<String> pinyin(String str) {
        List<String> result = new ArrayList();
        int i = 0;
        int pointer = 0;
        while (i < str.length()) {
            for (int j = i; j < str.length(); j++) {
                if (!pinyin.contains(str.substring(i, j + 1))) {
                    continue;
                }
                pointer = j;
            }
            if (pointer == 0) {
                result.add(str);
                return result;
            }
            pointer += 1;
            result.add(str.substring(i, pointer));
            i = pointer;
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

    public static char[][] POLYBIUS = new char[][]{
            new char[]{'a', 'b', 'c', 'd', 'e'},
            new char[]{'f', 'g', 'h', 'i', 'k'},
            new char[]{'l', 'm', 'n', 'o', 'p'},
            new char[]{'q', 'r', 's', 't', 'u'},
            new char[]{'v', 'w', 'x', 'y', 'z'}
    };

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static ArrayList<String> pinyin = null;

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

    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/pinyin.txt")));
            pinyin = new ArrayList();
            String line = null;
            do {
                line = br.readLine();
                pinyin.add(line);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
