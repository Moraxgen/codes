import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.*;

public class des {
    public static void main(String[] args) throws Exception {
        Scanner sc= new Scanner(System.in);
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecretKey key = keyGen.generateKey();
        System.out.print("Enter text: ");
        String message = sc.nextLine();
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
        String encText = Base64.getEncoder().encodeToString(encrypted);

        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encText));
        String decText = new String(decrypted, "UTF-8");

        System.out.println("Original : " + message);
        System.out.println("Encrypted: " + encText);
        System.out.println("Decrypted: " + decText);
    }
}



