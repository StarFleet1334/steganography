package image_steganography.utils;

import java.awt.image.BufferedImage;
import java.io.File;

public class Decode {





    public static String decode(String imagePath,int bytesForTextLengthData,int bitsInByte) {
        byte[] decodedHiddenText;
        try {
            BufferedImage imageFromPath = ImageIOMethods.getImageFromPath(imagePath);
            BufferedImage imageInUserSpace = ImageIOMethods.getImageInUserSpace(imageFromPath);
            byte imageInBytes[] = ImageIOMethods.getBytesFromImage(imageInUserSpace);
            decodedHiddenText = decodeImage(imageInBytes,bytesForTextLengthData,bitsInByte);
            String hiddenText = new String(decodedHiddenText);
            String outputFileName = "hidden_text.txt";
            ImageIOMethods.saveTextToPath(hiddenText, new File(outputFileName));
            System.out.println("Successfully extracted tex.txt to: " + outputFileName);
            return hiddenText;
        } catch (Exception exception) {
            System.out.println("No hidden message. Error: " + exception);
            return "";
        }
    }

    /**
     *
     *
     * length: This variable is used to store the length of the hidden data, which is encoded at the beginning of the image array.
     * offset: This variable is initialized to the position in the image array where the hidden data starts.
     * It skips over the space used for encoding the text length.
     *
     * This loop iterates over the initial part of the image array (up to the offset) to extract
     * the hidden data's length. The length is accumulated by shifting the current value of length one
     * bit to the left (length << 1)
     * and then adding the least significant bit of the current image byte (image[i] & 0x1).
     *
     * A new byte array named result is created with a size equal to the extracted length.
     * This array will store the hidden data.
     *
     *
     * The outer loop iterates over each byte of the result array.
     * The inner loop iterates over each bit of the current byte, starting from the most significant bit.
     * For each bit, it shifts the current value of result[b] one bit to the left and then adds the least significant bit of the current image byte.
     *
     *
     * The resulting byte array (result) contains the decoded hidden data, and it is returned by the method.
     *
     *
     * @param image
     * @param bytesForTextLengthData
     * @param bitsInByte
     * @return
     */

    private static byte[] decodeImage(byte[] image,int bytesForTextLengthData,int bitsInByte) {
        int length = 0;
        int offset  = bytesForTextLengthData*bitsInByte;

        for (int i=0; i<offset; i++) {
            length = (length << 1) | (image[i] & 0x1);
        }

        byte[] result = new byte[length];

        for (int b=0; b<result.length; b++ ) {
            for (int i=0; i<bitsInByte; i++, offset++) {
                result[b] = (byte)((result[b] << 1) | (image[offset] & 0x1));
            }
        }
        return result;
    }



}
