package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.Objects;

public class Course implements Completable, Purchasable {
    private final String name;
    private final String description;
    private final double price;
    private final Resource[] content;
    private final Category category;
    private final CourseDuration totalTime;
    private boolean isPurchased;

    public Course(String name, String description, double price, Resource[] content, Category category) {
        this(name, description, price, content, category, CourseDuration.of(content), false);
    }

    public Course(Course other) {
        this.name = other.name;
        this.description = other.description;
        this.price = other.price;
        this.content = Arrays.copyOf(other.content, other.content.length);
        this.category = other.category;
        this.totalTime = other.totalTime;
        this.isPurchased = other.isPurchased;
    }

    Course(String name, String description, double price, Resource[] content,
           Category category, CourseDuration totalTime, boolean isPurchased) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.content = content;
        this.category = category;
        this.totalTime = totalTime;
        this.isPurchased = isPurchased;
    }

    /**
     * Returns the name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the price of the course.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the category of the course.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the content of the course.
     */
    public Resource[] getContent() {
        return content;
    }

    /**
     * Returns the total duration of the course.
     */
    public CourseDuration getTotalTime() {
        return totalTime;
    }

    /**
     * Completes a resource from the course.
     *
     * @param resourceToComplete the resource which will be completed.
     * @throws IllegalArgumentException  if resourceToComplete is null.
     * @throws ResourceNotFoundException if the resource could not be found in the course.
     */
    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        for (Resource resource : content) {
            if (resource.equals(resourceToComplete)) {
                resource.complete();
            }
        }
        throw new ResourceNotFoundException(
                "Resource with name %s could not be found in course %s"
                        .formatted(resourceToComplete.getName(), name));

    }


    @Override
    public boolean isCompleted() {
        for (Resource resource : content) {
            if (!resource.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getCompletionPercentage() {
        int completionPercentage = 0;

        for (Resource resource : content) {
            if (resource.isCompleted()) {
                completionPercentage += resource.getCompletionPercentage();
            }
        }

        return completionPercentage == 0 ?
                0 : (int) Math.round((double) completionPercentage / content.length);
    }

    @Override
    public void purchase() {
        isPurchased = true;
    }

    @Override
    public boolean isPurchased() {
        return isPurchased;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Course other) {
            return this.name.equals(other.name) && this.description.equals(other.description) &&
                    this.price == other.price && Arrays.equals(this.content, other.content) &&
                    this.category.equals(other.category) && this.totalTime.equals(other.totalTime);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, Arrays.hashCode(content), category, totalTime);
    }
}
