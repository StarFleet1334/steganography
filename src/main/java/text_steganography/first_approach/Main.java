package text_steganography.first_approach;

import java.io.*;

class Decoder {

    public void decode() {
        String stegoKeyFilePath = "src/main/resources/text_steganography_first_approach/stegokey.txt";
        String stegoFilePath = "src/main/resources/text_steganography_first_approach/stego.txt";


        try (BufferedReader stegoKeyReader = new BufferedReader(new FileReader(stegoKeyFilePath));
             BufferedReader stegoReader = new BufferedReader(new FileReader(stegoFilePath))) {

            String stegoKeyLine;
            while ((stegoKeyLine = stegoKeyReader.readLine()) != null) {
                int k = Integer.parseInt(stegoKeyLine.trim());

                String stegoWord = stegoReader.readLine();
                if (stegoWord == null) {
                    System.out.println("Error: Stego file does not have enough words.");
                    break;
                }

                int l = stegoWord.length();
                if (l > 9) {
                    l -= 10;
                }

                char firstLetter = stegoWord.charAt(0);
                int s = decodeFirstLetter(firstLetter);

                int r = s - (l + k);
                int n = (k * 100) + (l * 10) + r;

                char characterEquivalent = (char) n;

                System.out.println("k: " + k + ", l: " + l + ", s: " + s + ", r: " + r +
                        ", n: " + n + ", Character equivalent: " + characterEquivalent);
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static int decodeFirstLetter(char firstLetter) {
        return Character.toUpperCase(firstLetter) - 'A' + 1;
    }

}


class Encoder {

    public void doEncode() throws IOException {
        // Replace "path/to/your/file.txt" with the actual path to your file
        String filePath = "src/main/resources/text_steganography_first_approach/input";
        String stegoKeyFilePath = "src/main/resources/text_steganography_first_approach/stegokey.txt";
        String stegoFilePath = "src/main/resources/text_steganography_first_approach/stego.txt";



        try (BufferedReader inputReader = new BufferedReader(new FileReader(filePath));
             BufferedWriter stegoKeyWriter = new BufferedWriter(new FileWriter(stegoKeyFilePath));
             BufferedWriter stegoWriter = new BufferedWriter(new FileWriter(stegoFilePath))) {

            // Step 1: Calculate length of the input file as ls
            int ls = 0;
            while (inputReader.read() != -1) {
                ls++;
            }
            inputReader.close();

            // Reopen the input file for reading

            try(
                    BufferedReader newInput = new BufferedReader(new FileReader(filePath))

                    ) {

                // Step 2 to 10: Process each character in the file
                int character;
                while ((character = newInput.read()) != -1) {
                    char c = (char) character;

                    // Step 3: If n < 100, make it a three-digit number by pre-appending it with zeroes
                    String nStr = String.format("%03d", (int) c);

                    // Step 4: k = most significant digit of n. Write k in the stego key file.
                    int k = Character.getNumericValue(nStr.charAt(0));
                    stegoKeyWriter.write(Integer.toString(k));
                    stegoKeyWriter.newLine();

                    // Step 5: L = middle digit of n.
                    int middleIndex = nStr.length() / 2;
                    int L = Character.getNumericValue(nStr.charAt(middleIndex));

                    // Step 6: If L < 6, then L = L+10.
                    if (L < 6) {
                        L += 10;
                    }

                    // Step 7: s = sum of the digits of n.
                    int s = Character.getNumericValue(nStr.charAt(0)) +
                            Character.getNumericValue(nStr.charAt(1)) +
                            Character.getNumericValue(nStr.charAt(2));

                    // Step 8: Get a L size word starting with the s-th letter of the English alphabet
                    String stegoWord = generateStegoWord(L, s);
                    stegoWriter.write(stegoWord);
                    stegoWriter.newLine();

                    // Steps 9-10: Repeat steps 2 to 8 till the end of the file.
                }
            }

            // Step 10: If ls < 10, insert 10-ls ten-letter words in the stego file
            if (ls < 10) {
                for (int i = 0; i < (10 - ls); i++) {
                    String tenLetterWord = generateStegoWord(10, 1); // Assume s=1 for simplicity
                    stegoWriter.write(tenLetterWord);
                }
            }

            System.out.println("Stego files created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateStegoWord(int size, int startingLetterIndex) {
        StringBuilder wordBuilder = new StringBuilder();

        for (int i = 0; i < size; i++) {
            char currentLetter = (char) ('A' + (startingLetterIndex - 1 + i) % 26);
            wordBuilder.append(currentLetter);
        }

        return wordBuilder.toString();
    }
}

public class Main {

    public static void main(String[] args) throws IOException {
        Encoder encoder = new Encoder();
        encoder.doEncode();


        Decoder decoder = new Decoder();
        decoder.decode();

    }
}