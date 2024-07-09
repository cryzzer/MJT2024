package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable {
    private static final int MAX_COMPLETION_PERCENTAGE = 100;
    private final String name;
    private final ResourceDuration duration;

    private boolean isCompleted;

    public Resource(String name, ResourceDuration duration) {
        this(name, false, duration);
    }

    public Resource(Resource otherResource) {
        this(otherResource.name, otherResource.isCompleted, otherResource.duration);
    }

    Resource(String name, boolean isCompleted, ResourceDuration duration) {
        this.name = name;
        this.isCompleted = isCompleted;
        this.duration = duration;
    }

    /**
     * Returns the resource name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the total duration of the resource.
     */
    public ResourceDuration getDuration() {
        return duration;
    }

    /**
     * Marks the resource as completed.
     */
    public void complete() {
        this.isCompleted = true;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public int getCompletionPercentage() {
        return isCompleted ? MAX_COMPLETION_PERCENTAGE : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Resource other) {
            return this.name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
