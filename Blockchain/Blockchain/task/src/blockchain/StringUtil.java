package blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;

class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> divideString(String string, int subStringLength) {
        ArrayList<String> result = new ArrayList<>();
        int beginIndex = 0;
        while (beginIndex < string.length()) {
            int endIndex = Math.min(beginIndex + subStringLength, string.length());
            result.add(string.substring(beginIndex,endIndex));
            beginIndex += subStringLength;
        }
        return result;
    }
}