import java.util.*;
public class cipher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=== CIPHER MENU ===");
            System.out.println("1. Caesar Cipher");
            System.out.println("2. Substitution Cipher");
            System.out.println("3. Hill Cipher");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    caesarCipher(sc);
                    break;
                case 2:
                    substitutionCipher(sc);
                    break;
                case 3:
                    hillCipher(sc);
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);

        sc.close();
    }
    public static void caesarCipher(Scanner sc) {
        System.out.println("\n--- Caesar Cipher ---");
        System.out.print("Do you want to (E)ncrypt or (D)ecrypt? ");
        char action = sc.nextLine().toUpperCase().charAt(0);
        System.out.print("Enter text: ");
        String text = sc.nextLine();
        System.out.print("Enter shift: ");
        int shift = sc.nextInt();
        sc.nextLine();

        if (action == 'E' || action == 'e') {
            System.out.println("Encrypted: " + caesarEncrypt(text, shift));
        } else if (action == 'D' || action == 'd') {
            System.out.println("Decrypted: " + caesarEncrypt(text, -shift));
        } else {
            System.out.println("Invalid action.");
        }
    }
    public static String caesarEncrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = ((shift % 26) + 26) % 26;
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c))
                result.append((char) ((c - 'A' + shift) % 26 + 'A'));
            else if (Character.isLowerCase(c))
                result.append((char) ((c - 'a' + shift) % 26 + 'a'));
            else
                result.append(c);
        }
        return result.toString();
    }
    public static void substitutionCipher(Scanner sc) {
        System.out.println("\n--- Substitution Cipher ---");
        System.out.print("Do you want to (E)ncrypt or (D)ecrypt? ");
        char action = sc.nextLine().toUpperCase().charAt(0);
        System.out.print("Enter 26-letter key: ");
        String key = sc.nextLine().toUpperCase();
        System.out.print("Enter text: ");
        String text = sc.nextLine();
        if (action == 'E' || action == 'e') {
            System.out.println("Encrypted: " + substitutionEncrypt(text, key));
        } else if (action == 'D' || action == 'd') {
            System.out.println("Decrypted: " + substitutionDecrypt(text, key));
        } else {
            System.out.println("Invalid action.");
        }
    }

    public static String substitutionEncrypt(String text, String key) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            map.put(alphabet.charAt(i), key.charAt(i));
        }
        StringBuilder result = new StringBuilder();
        for (char c : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(c))
                result.append(map.get(c));
            else
                result.append(c);
        }
        return result.toString();
    }

    public static String substitutionDecrypt(String text, String key) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Map<Character, Character> reverseMap = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            reverseMap.put(key.charAt(i), alphabet.charAt(i));
        }
        StringBuilder result = new StringBuilder();
        for (char c : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(c))
                result.append(reverseMap.get(c));
            else
                result.append(c);
        }
        return result.toString();
    }

    // Hill Cipher (3x3)
    public static void hillCipher(Scanner sc) {
        System.out.println("\n--- Hill Cipher ---");
        System.out.print("Do you want to (E)ncrypt or (D)ecrypt? ");
        char action = sc.nextLine().toUpperCase().charAt(0);
        System.out.print("Enter text: ");
        String text = sc.nextLine().toUpperCase();
        System.out.print("Enter 9-letter key (3x3 matrix): ");
        String key = sc.nextLine().toUpperCase();

        if (action == 'E' || action == 'e') {
            System.out.println("Encrypted: " + hillEncrypt(text, key));
        } else if (action == 'D' || action == 'd') {
            System.out.println("Decrypted: " + hillDecrypt(text, key));
        } else {
            System.out.println("Invalid action.");
        }
    }

    public static String hillEncrypt(String text, String key) {
        while (text.length() % 3 != 0) {
            text += "X";
        }

        int[][] keyMatrix = createKeyMatrix(key);
        StringBuilder cipher = new StringBuilder();

        for (int k = 0; k < text.length(); k += 3) {
            int[] textVector = createVector(text.substring(k, k + 3));
            int[] cipherVector = new int[3];
            for (int i = 0; i < 3; i++) {
                cipherVector[i] = 0;
                for (int j = 0; j < 3; j++) {
                    cipherVector[i] += keyMatrix[i][j] * textVector[j];
                }
                cipherVector[i] = ((cipherVector[i] % 26) + 26) % 26;
            }
            for (int val : cipherVector) {
                cipher.append((char) (val + 'A'));
            }
        }
        return cipher.toString();
    }

    public static String hillDecrypt(String text, String key) {
        int[][] keyMatrix = createKeyMatrix(key);
        int[][] invMatrix = inverseMatrix(keyMatrix);
        if (invMatrix == null) {
            return "DECRYPTION FAILED - INVALID KEY";
        }

        while (text.length() % 3 != 0) {
            text += "X";
        }

        StringBuilder plain = new StringBuilder();
        for (int k = 0; k < text.length(); k += 3) {
            int[] textVector = createVector(text.substring(k, k + 3));
            int[] plainVector = new int[3];
            for (int i = 0; i < 3; i++) {
                plainVector[i] = 0;
                for (int j = 0; j < 3; j++) {
                    plainVector[i] += invMatrix[i][j] * textVector[j];
                }
                plainVector[i] = ((plainVector[i] % 26) + 26) % 26;
            }
            for (int val : plainVector) {
                plain.append((char) (val + 'A'));
            }
        }
        return plain.toString();
    }

    public static int[][] createKeyMatrix(String key) {
        int[][] matrix = new int[3][3];
        for (int i = 0; i < 9; i++) {
            matrix[i / 3][i % 3] = key.charAt(i) - 'A';
        }
        return matrix;
    }

    public static int[] createVector(String text) {
        int[] vector = new int[3];
        for (int i = 0; i < 3; i++) {
            vector[i] = text.charAt(i) - 'A';
        }
        return vector;
    }

    public static int[][] inverseMatrix(int[][] matrix) {
        int det = determinant(matrix);
        if (det == 0) {
            System.out.println("Matrix is not invertible!");
            return null;
        }
        int invDet = modInverse(det, 26);
        if (invDet == -1) {
            System.out.println("Determinant has no modular inverse!");
            return null;
        }
        int[][] adj = adjugate(matrix);
        int[][] inverse = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                inverse[i][j] = ((adj[i][j] * invDet) % 26 + 26) % 26;
            }
        }
        return inverse;
    }

    public static int determinant(int[][] matrix) {
        int det = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        return ((det % 26) + 26) % 26;
    }

    public static int[][] adjugate(int[][] matrix) {
        int[][] adj = new int[3][3];
        adj[0][0] = ((matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) % 26 + 26) % 26;
        adj[0][1] = ((-(matrix[0][1] * matrix[2][2] - matrix[0][2] * matrix[2][1])) % 26 + 26) % 26;
        adj[0][2] = ((matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1]) % 26 + 26) % 26;
        adj[1][0] = ((-(matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])) % 26 + 26) % 26;
        adj[1][1] = ((matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0]) % 26 + 26) % 26;
        adj[1][2] = ((-(matrix[0][0] * matrix[1][2] - matrix[0][2] * matrix[1][0])) % 26 + 26) % 26;
        adj[2][0] = ((matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]) % 26 + 26) % 26;
        adj[2][1] = ((-(matrix[0][0] * matrix[2][1] - matrix[0][1] * matrix[2][0])) % 26 + 26) % 26;
        adj[2][2] = ((matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % 26 + 26) % 26;
        return adj;
    }

    public static int modInverse(int a, int m) {
        a = ((a % m) + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }
}