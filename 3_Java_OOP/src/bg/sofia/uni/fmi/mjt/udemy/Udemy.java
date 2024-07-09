package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

import java.util.Arrays;

public class Udemy implements LearningPlatform {
    private final Account[] accounts;
    private final Course[] courses;

    public Udemy(Account[] accounts, Course[] courses) {
        this.accounts = accounts;
        this.courses = courses;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is either blank or a null, enter a valid name for result");
        }

        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }

        throw new CourseNotFoundException(
                "No such course with name %s was found in the courses array"
                        .formatted(name));
    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword is either null or blank, it must be a valid word for result");
        }

        Course[] containingKeywordCourses = new Course[courses.length];
        int numberOfMatchedCourses = 0;

        for (Course course : courses) {
            if (course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                containingKeywordCourses[numberOfMatchedCourses++] = course;
            }
        }

        return Arrays.copyOf(containingKeywordCourses, numberOfMatchedCourses);
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is null, enter valid category for results");
        }

        Course[] matchedCategoryCourses = new Course[courses.length];
        int numberOfMatchedCourses = 0;

        for (Course course : courses) {
            if (course.getCategory().equals(category)) {
                matchedCategoryCourses[numberOfMatchedCourses++] = course;
            }
        }
        return Arrays.copyOf(matchedCategoryCourses, numberOfMatchedCourses);
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is either blank or a null, enter a valid name for finding account");
        }

        for (Account account : accounts) {
            if (account.getUsername().equals(name)) {
                return account;
            }
        }

        throw new AccountNotFoundException(
                "No such account with username %s exists"
                        .formatted(name));
    }

    @Override
    public Course getLongestCourse() {
        Course longestCourse = null;
        CourseDuration longestCourseDuration = new CourseDuration(0, 0);

        for (Course course : courses) {
            if (course.getTotalTime().isLongerThan(longestCourseDuration)) {
                longestCourse = course;
                longestCourseDuration = longestCourse.getTotalTime();
            }
        }

        return longestCourse;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is null, enter valid category to get the cheapest course by category");
        }

        if (courses.length == 0) {
            return null;
        }

        Course cheapestCourse = null;
        double lowestPrice = Double.MAX_VALUE;

        for (Course course : courses) {
            if (course.getCategory().equals(category) && course.getPrice() < lowestPrice) {
                cheapestCourse = course;
                lowestPrice = cheapestCourse.getPrice();
            }
        }

        return cheapestCourse;
    }
}
