package image_steganography.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

public class ImageIOMethods {

    static BufferedImage getImageFromPath(String path) {
        BufferedImage image	= null;
        File file = new File(path);
        try {
            image = ImageIO.read(file);
        } catch (Exception exception) {
            System.out.println("Input image cannot be read. Error: " + exception);
        }
        return image;
    }

    static String getTextFromTextFile(String textFile) {
        String text = "";
        try {
            Scanner scanner = new Scanner( new File(textFile) );
            text = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (Exception exception) {
            System.out.println("Couldn't read tex.txt from file. Error: " + exception);
        }
        return text;
    }


    /**
     * Here, a new BufferedImage named imageInUserSpace is created with the same width and
     * height as the original image (image). The color model used is TYPE_3BYTE_BGR.
     * This color model represents RGB values using three bytes
     * (8 bits for each color channel: blue, green, and red) and does not have an alpha channel.*
     *
     * The createGraphics() method is called on the new imageInUserSpace to obtain a Graphics2D
     * object named graphics.
     * The Graphics2D class provides a set of advanced two-dimensional drawing methods.
     *
     * The drawRenderedImage() method is used to draw the contents of the original image (image)
     * onto the new image (imageInUserSpace). The second parameter (null in this case) is an
     * AffineTransform that can be used for transformations during the drawing.
     * Passing null means no additional transformation is applied.
     * @param image
     * @return
     */

    static BufferedImage getImageInUserSpace(BufferedImage image) {
        BufferedImage imageInUserSpace  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = imageInUserSpace.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return imageInUserSpace;
    }


    /**
     *
     *
     *
     * The getRaster() method is called on the BufferedImage to obtain its WritableRaster.
     * The raster represents the raw pixel data of
     * the image and provides methods for accessing and modifying pixel values.
     *
     * The getDataBuffer() method is called on the raster to obtain its data buffer.
     * In this case, it's assumed that the data buffer is of type DataBufferByte,
     * so a cast is used to convert it to that type.
     * The DataBuffer contains the actual data storage for the raster.
     *
     *
     * The getData() method is called on the DataBufferByte to obtain the raw byte
     * data of the image. This byte array represents the pixel values of the image in
     * a linear fashion. Each pixel's color information
     * is represented by a sequence of bytes, and this method returns those bytes.
     * @param image
     * @return
     */
    static byte[] getBytesFromImage(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        return buffer.getData();
    }

    static void saveImageToPath(BufferedImage image, File file, String extension) {
        try {
            file.delete();
            ImageIO.write(image, extension, file);
        } catch (Exception exception) {
            System.out.println("Image file could not be saved. Error: " + exception);
        }
    }

    static void saveTextToPath(String text, File file) {
        try {
            if (file.exists() == false) {
                file.createNewFile( );
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (Exception exception) {
            System.out.println("Couldn't write tex.txt to file: " + exception);
        }
    }

    /**
     *
     *
     * This line creates a new ByteBuffer with a specified capacity,
     * which is determined by the variable bytesForTextLengthData.
     * The allocate method allocates a new byte buffer of the specified size.
     *
     * The putInt method is called on the ByteBuffer to insert the integer value into the buffer.
     * The integer is converted to its binary representation
     * and stored in the buffer as four consecutive bytes, using the platform's native byte order.
     *
     * The array method is called on the ByteBuffer to obtain a byte array containing the data
     * stored in the buffer.
     * This byte array now represents the binary representation of the original integer.
     * @param integer
     * @param bytesForTextLengthData
     * @return
     */

    static byte[] getBytesFromInt(int integer,int bytesForTextLengthData) {
        return ByteBuffer.allocate(bytesForTextLengthData).putInt(integer).array();
    }
}
