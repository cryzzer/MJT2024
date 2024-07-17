package bg.sofia.uni.fmi.mjt.photoalbum;

import bg.sofia.uni.fmi.mjt.photoalbum.image.Image;
import bg.sofia.uni.fmi.mjt.photoalbum.image.ImageWorker;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {
    private final int imageProcessorsCount;
    private final Queue<Path> imageQueue = new LinkedList<>();
    private final Object lock = new Object();
    private final AtomicInteger remainingPicturesToBeProduced = new AtomicInteger(0);

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        File sourceDir = new File(sourceDirectory);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Source directory does not exist or is not a directory");
        }

        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        ImageWorker imageWorker = new ImageWorker(outputDirectory);

        // Start producer threads
        File[] files = sourceDir.listFiles((dir, name) -> name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png"));
        if (files != null) {
            remainingPicturesToBeProduced.set(files.length); // Set the number of producers

            // Start consumer threads
            for (int i = 0; i < imageProcessorsCount; i++) {
                new Thread(new ImageProcessor(imageQueue, imageWorker, lock, remainingPicturesToBeProduced)).start();
            }

            for (File file : files) {
                new Thread(new ImageLoader(file.toPath(), imageQueue, lock, remainingPicturesToBeProduced)).start();
            }
        }
    }

    private static class ImageLoader implements Runnable {
        private final Path imagePath;
        private final Queue<Path> imageQueue;
        private final Object lock;
        private final AtomicInteger remainingPicturesToBeProduced;

        public ImageLoader(Path imagePath, Queue<Path> imageQueue, Object lock, AtomicInteger remainingPicturesToBeProduced) {
            this.imagePath = imagePath;
            this.imageQueue = imageQueue;
            this.lock = lock;
            this.remainingPicturesToBeProduced = remainingPicturesToBeProduced;
        }

        @Override
        public void run() {
            synchronized (lock) {
                imageQueue.add(imagePath);
                lock.notifyAll(); // Notify consumers that a new image is available
            }
            if (remainingPicturesToBeProduced.decrementAndGet() == 0) {
                synchronized (lock) {
                    lock.notifyAll(); // Notify consumers that all producers are done
                }
            }
        }
    }

    private static class ImageProcessor implements Runnable {
        private final Queue<Path> imageQueue;
        private final ImageWorker imageWorker;
        private final Object lock;
        private final AtomicInteger remainingPicturesToBeProduced;

        public ImageProcessor(Queue<Path> imageQueue, ImageWorker imageWorker, Object lock, AtomicInteger remainingPicturesToBeProduced) {
            this.imageQueue = imageQueue;
            this.imageWorker = imageWorker;
            this.lock = lock;
            this.remainingPicturesToBeProduced = remainingPicturesToBeProduced;
        }

        @Override
        public void run() {
            while (true) {
                Path imagePath;
                synchronized (lock) {
                    while (imageQueue.isEmpty() && remainingPicturesToBeProduced.get() > 0) {
                        try {
                            lock.wait(); // Wait for new images to be added
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (imageQueue.isEmpty() && remainingPicturesToBeProduced.get() == 0) {
                        return; // Exit when no more images to process and all producers are done
                    }
                    imagePath = imageQueue.poll();
                }
                if (imagePath != null) {
                    Image image = imageWorker.loadImage(imagePath);
                    Image bwImage = imageWorker.convertToBlackAndWhite(image);
                    imageWorker.saveImage(bwImage);
                }
            }
        }
    }
}
