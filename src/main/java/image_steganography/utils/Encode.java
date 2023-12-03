package image_steganography.utils;

import java.awt.image.BufferedImage;
import java.io.File;

public class Encode {


    public static void encode(String imagePath, String textPath,int bytesForTextLengthData, int bitsInByte) {
        BufferedImage originalImage = ImageIOMethods.getImageFromPath(imagePath);
        BufferedImage imageInUserSpace = ImageIOMethods.getImageInUserSpace(originalImage);
        String text = ImageIOMethods.getTextFromTextFile(textPath);

        byte imageInBytes[] = ImageIOMethods.getBytesFromImage(imageInUserSpace);
        byte textInBytes[] = text.getBytes();
        byte textLengthInBytes[] = ImageIOMethods.getBytesFromInt(textInBytes.length,bytesForTextLengthData);
        try {
            encodeImage(imageInBytes, textLengthInBytes,  0,bitsInByte);
            encodeImage(imageInBytes, textInBytes, bytesForTextLengthData*bitsInByte,bitsInByte);
        }
        catch (Exception exception) {
            System.out.println("Couldn't hide tex.txt in image. Error: " + exception);
            return;
        }

        String fileName = imagePath;
        int position = fileName.lastIndexOf(".");
        if (position > 0) {
            fileName = fileName.substring(0, position);
        }

        String finalFileName = fileName + "_with_hidden_message.png";
        System.out.println("Was here");
        System.out.println(text);
        System.out.println("Successfully encoded tex.txt in: " + finalFileName);
        ImageIOMethods.saveImageToPath(imageInUserSpace, new File(finalFileName),"png");
        return;
    }

    /**
     *
     *
     * This inner loop iterates over each bit of the current byte in addition, starting from the most
     * significant bit (bitsInByte-1) and moving towards the least significant bit (0).
     * The offset variable is used to determine where in the image array the current bit should be stored.
     *
     *
     * The >>> operator performs a bitwise right shift on additionByte, moving the current bit to the
     * least significant position. The result is
     * then bitwise ANDed with 0x1 (binary 00000001), effectively extracting the least significant bit.
     *
     * The least significant bit of the current byte in image is cleared (set to 0 using & 0xFE),
     * and then the extracted bit (b) is ORed to set the bit to its desired value.
     *
     * After all bits have been embedded, the modified image array is returned.
     *
     * @param image
     * @param addition
     * @param offset
     * @param bitsInByte
     * @return
     */

    private static byte[] encodeImage(byte[] image, byte[] addition, int offset,int bitsInByte) {
        if (addition.length + offset > image.length) {
            throw new IllegalArgumentException("Image file is not long enough to store provided tex.txt");
        }
        for (int i=0; i<addition.length; i++) {
            int additionByte = addition[i];
            for (int bit=bitsInByte-1; bit>=0; --bit, offset++) {
                int b = (additionByte >>> bit) & 0x1;
                image[offset] = (byte)((image[offset] & 0xFE) | b);
            }
        }
        return image;
    }

}
