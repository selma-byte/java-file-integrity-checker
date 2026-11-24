
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class FileIntegrityChecker {

    // Calculate the SHA-256 hash of a file
    public static String calculateHash(String filePath)
            throws IOException, NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (FileInputStream input = new FileInputStream(filePath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hashBytes = digest.digest();

        StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b & 0xff));
        }

        return hexString.toString();
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String savedHash = "";

        while (true) {

            System.out.println("\n================================");
            System.out.println("     FILE INTEGRITY CHECKER");
            System.out.println("================================");
            System.out.println("1. Calculate and save file hash");
            System.out.println("2. Verify file integrity");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {

                if (choice.equals("1")) {

                    System.out.print("Enter file path: ");
                    String path = scanner.nextLine();

                    savedHash = calculateHash(path);

                    System.out.println("\nOriginal SHA-256 hash:");
                    System.out.println(savedHash);

                    System.out.println("\nBaseline hash saved successfully.");

                } else if (choice.equals("2")) {

                    if (savedHash.isEmpty()) {
                        System.out.println("\nNo baseline hash found.");
                        System.out.println("Calculate and save a hash first.");
                        continue;
                    }

                    System.out.print("Enter file path: ");
                    String path = scanner.nextLine();

                    String currentHash = calculateHash(path);

                    System.out.println("\nChecking file integrity...");

                    if (savedHash.equals(currentHash)) {
                        System.out.println("RESULT: FILE UNCHANGED");
                    } else {
                        System.out.println("RESULT: FILE MODIFIED!");
                    }

                } else if (choice.equals("3")) {

                    System.out.println("Exiting program...");
                    break;

                } else {

                    System.out.println("Invalid option. Try again.");
                }

            } catch (IOException e) {

                System.out.println("File error: " + e.getMessage());

            } catch (NoSuchAlgorithmException e) {

                System.out.println("Hash algorithm unavailable.");
            }
        }

        scanner.close();
    }
}
