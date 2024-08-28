
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Random;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DestinationHashGenerator {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java DestinationHashGenerator <PRN_Number> <Path_to_JSON_File>");
            return;
        }
        
        String prn = args[0].toLowerCase().replace(" ", "");
        String jsonFilePath = args[1];
        
        JSONTokener tokener = new JSONTokener(new FileReader(jsonFilePath));
        JSONObject jsonObject = new JSONObject(tokener);
        
        String destinationValue = findDestination(jsonObject);
        String randomString = generateRandomString(8);
        
        String concatenatedString = prn + destinationValue + randomString;
        String md5Hash = generateMD5Hash(concatenatedString);
        
        System.out.println(md5Hash + ";" + randomString);
    }
    
    private static String findDestination(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (key.equals("destination")) {
                return value.toString();
            } else if (value instanceof JSONObject) {
                String result = findDestination((JSONObject) value);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}

