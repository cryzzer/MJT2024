package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;

    public CourseDuration {
        if (!(0 <= hours && hours <= HOURS_IN_DAY)) {
            throw new IllegalArgumentException("The hours should be between 0 and 24");
        }
        if (!(0 <= minutes && minutes <= MINUTES_IN_HOUR)) {
            throw new IllegalArgumentException("The hours should be between 0 and 60");
        }
    }

    public static CourseDuration of(Resource[] content) {
        int minutes = 0;

        for (Resource resource : content) {
            minutes += resource.getDuration().minutes();
        }

        int hours = minutes / MINUTES_IN_HOUR;
        minutes = minutes - hours * MINUTES_IN_HOUR;

        return new CourseDuration(hours, minutes);
    }

    public boolean isLongerThan(CourseDuration otherCourseDuration) {
        int totalMinutes = (this.hours * MINUTES_IN_HOUR) + this.minutes;
        int otherTotalMinutes = (otherCourseDuration.hours * MINUTES_IN_HOUR) + otherCourseDuration.minutes;

        return totalMinutes > otherTotalMinutes;
    }
}
