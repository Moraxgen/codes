import java.util.*;

public class addcipher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== CIPHER MENU ===");
            System.out.println("1. Playfair Cipher");
            System.out.println("2. Rail Fence Cipher");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    playfairCipher(sc);
                    break;
                case 2:
                    railFenceCipher(sc);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);

        sc.close();
    }

    // Playfair Cipher
    public static void playfairCipher(Scanner sc) {
        System.out.print("Enter key: ");
        String key = sc.nextLine().toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        System.out.print("Enter text: ");
        String text = sc.nextLine().toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        System.out.print("Encrypt (E) or Decrypt (D): ");
        char action = sc.nextLine().toUpperCase().charAt(0);

        if (action == 'E') {
            System.out.println("Encrypted: " + playfairEncrypt(text, key));
        } else if (action == 'D') {
            System.out.println("Decrypted: " + playfairDecrypt(text, key));
        } else {
            System.out.println("Invalid action.");
        }
    }

    public static String playfairEncrypt(String text, String key) {
        char[][] matrix = createPlayfairMatrix(key);
        text = preparePlayfairText(text, true);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(matrix, a);
            int[] posB = findPosition(matrix, b);

            if (posA[0] == posB[0]) { // Same row
                result.append(matrix[posA[0]][(posA[1] + 1) % 5]);
                result.append(matrix[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) { // Same column
                result.append(matrix[(posA[0] + 1) % 5][posA[1]]);
                result.append(matrix[(posB[0] + 1) % 5][posB[1]]);
            } else { // Rectangle
                result.append(matrix[posA[0]][posB[1]]);
                result.append(matrix[posB[0]][posA[1]]);
            }
        }

        return result.toString();
    }

    public static String playfairDecrypt(String text, String key) {
        char[][] matrix = createPlayfairMatrix(key);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(matrix, a);
            int[] posB = findPosition(matrix, b);

            if (posA[0] == posB[0]) { // Same row
                result.append(matrix[posA[0]][(posA[1] + 4) % 5]);
                result.append(matrix[posB[0]][(posB[1] + 4) % 5]);
            } else if (posA[1] == posB[1]) { // Same column
                result.append(matrix[(posA[0] + 4) % 5][posA[1]]);
                result.append(matrix[(posB[0] + 4) % 5][posB[1]]);
            } else { // Rectangle
                result.append(matrix[posA[0]][posB[1]]);
                result.append(matrix[posB[0]][posA[1]]);
            }
        }

        return result.toString();
    }

    public static char[][] createPlayfairMatrix(String key) {
        char[][] matrix = new char[5][5];
        Set<Character> used = new HashSet<>();
        int index = 0;

        for (char c : key.toCharArray()) {
            if (!used.contains(c)) {
                matrix[index / 5][index % 5] = c;
                used.add(c);
                index++;
            }
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            if (c != 'J' && !used.contains(c)) {
                matrix[index / 5][index % 5] = c;
                used.add(c);
                index++;
            }
        }

        return matrix;
    }

    public static String preparePlayfairText(String text, boolean encrypt) {
        StringBuilder prepared = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i + 1 < text.length() && text.charAt(i) == text.charAt(i + 1)) {
                prepared.append(text.charAt(i)).append('X');
            } else {
                prepared.append(text.charAt(i));
            }
        }
        if (prepared.length() % 2 != 0) {
            prepared.append('X');
        }
        return prepared.toString();
    }

    public static int[] findPosition(char[][] matrix, char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Rail Fence Cipher
    public static void railFenceCipher(Scanner sc) {
        System.out.print("Enter text: ");
        String text = sc.nextLine();
        System.out.print("Enter number of rails: ");
        int rails = sc.nextInt();
        sc.nextLine();
        System.out.print("Encrypt (E) or Decrypt (D): ");
        char action = sc.nextLine().toUpperCase().charAt(0);

        if (action == 'E') {
            System.out.println("Encrypted: " + railFenceEncrypt(text, rails));
        } else if (action == 'D') {
            System.out.println("Decrypted: " + railFenceDecrypt(text, rails));
        } else {
            System.out.println("Invalid action.");
        }
    }

    public static String railFenceEncrypt(String text, int rails) {
        char[][] rail = new char[rails][text.length()];
        boolean down = false;
        int row = 0, col = 0;

        for (char c : text.toCharArray()) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            rail[row][col++] = c;
            row += down ? 1 : -1;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rails; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] != 0) {
                    result.append(rail[i][j]);
                }
            }
        }
        return result.toString();
    }

    public static String railFenceDecrypt(String text, int rails) {
        char[][] rail = new char[rails][text.length()];
        boolean down = false;
        int row = 0, col = 0;

        // Step 1: Mark the zigzag pattern with '*'
        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            rail[row][col++] = '*';
            row += down ? 1 : -1;
        }

        // Step 2: Fill the zigzag pattern with the ciphertext
        int index = 0;
        for (int i = 0; i < rails; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] == '*' && index < text.length()) {
                    rail[i][j] = text.charAt(index++);
                }
            }
        }

        // Step 3: Read the plaintext by following the zigzag pattern
        StringBuilder result = new StringBuilder();
        row = 0;
        col = 0;
        down = false;
        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            result.append(rail[row][col++]);
            row += down ? 1 : -1;
        }

        return result.toString();
    }
}
