package gui.helper_classes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    private Image imageToReturn;

    public Image getImage(String imageToGet) {
        //Assign the image to normandy variable
        try {
            File pathToFile = new File("src/gui/images/" + imageToGet + ".png");
            imageToReturn = ImageIO.read(pathToFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return imageToReturn;
    }

    //Initializes the normandy image and rotates it 45 degrees to the left
    public Image getAndRotateImage(String imageToGet) {
        // Assign the images to the variables titan and normandy
        try {
            File pathToFile = new File("src/gui/images/" + imageToGet + ".png");
            BufferedImage originalImage = ImageIO.read(pathToFile);

            // Calculate the dimensions for the rotated image
            int maxWidth = (int) Math.ceil(originalImage.getWidth() * Math.abs(Math.cos(Math.PI / 4)))
                    + (int) Math.ceil(originalImage.getHeight() * Math.abs(Math.sin(Math.PI / 4)));
            int maxHeight = (int) Math.ceil(originalImage.getWidth() * Math.abs(Math.sin(Math.PI / 4)))
                    + (int) Math.ceil(originalImage.getHeight() * Math.abs(Math.cos(Math.PI / 4)));

            // Create a new BufferedImage with the appropriate dimensions
            BufferedImage rotatedImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);

            // Get the graphics object of the rotated image
            Graphics2D g2d = rotatedImage.createGraphics();
//
//            // Set the background color to transparent
//            rotatedImage = g2d.getDeviceConfiguration().createCompatibleImage(maxWidth, maxHeight, Transparency.TRANSLUCENT);
//            g2d.dispose();
//            g2d = rotatedImage.createGraphics();

            // Rotate the image around the center
            AffineTransform transform = new AffineTransform();
            transform.translate((maxWidth - originalImage.getWidth()) / 2.0, (maxHeight - originalImage.getHeight()) / 2.0);
            transform.rotate(-Math.PI / 4, originalImage.getWidth() / 2.0, originalImage.getHeight() / 2.0);

            // Apply the rotation to the new image
            g2d.setTransform(transform);
            g2d.drawImage(originalImage, 0, 0, null);
            g2d.dispose();

            // Assign the rotated image to the variable
            imageToReturn = rotatedImage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return imageToReturn;
    }
}
