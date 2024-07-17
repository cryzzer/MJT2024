package bg.sofia.uni.fmi.mjt.photoalbum.image;

import java.io.File;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
public class ImageWorker {
    private final String destinationDirectory;

    public ImageWorker(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public Image loadImage(Path imagePath) {
        try {
            BufferedImage imageData = ImageIO.read(imagePath.toFile());
            return new Image(imagePath.getFileName().toString(), imageData);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to load image %s", imagePath.toString()), e);
        }
    }


    public Image convertToBlackAndWhite(Image image) {
        BufferedImage processedData = new BufferedImage(image.data.getWidth(), image.data.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        processedData.getGraphics().drawImage(image.data, 0, 0, null);

        return new Image(image.name, processedData);
    }

    public void saveImage(Image image) {
        try {
            ImageIO.write(image.data, "png", new File(destinationDirectory, image.name));
            System.out.println("Saved " + image.name + " to " + destinationDirectory);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("While saving image %s", image.name), e);
        }
    }
}
