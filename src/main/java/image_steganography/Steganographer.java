package image_steganography;


import image_steganography.utils.Decode;
import image_steganography.utils.Encode;

public class Steganographer {

    private static int bytesForTextLengthData = 4;
    private static int bitsInByte = 8;

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length == 1) {
                Decode.decode(args[0],bytesForTextLengthData,bitsInByte);
                return;
            } else if (args.length == 2) {
                Encode.encode(args[0], args[1],bytesForTextLengthData,bitsInByte);
                return;
            }
        }
        System.out.println("Wrong input. Use '--help' option for more information.");
    }



}
