package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public abstract class AccountBase implements Account {
    private static final int MAX_CAPACITY = 100;
    private static final double MIN_GRADE = 2.0;
    private static final double MAX_GRADE = 6.0;
    private final String username;
    private double balance;

    private final Course[] courses;
    private int numberOfAddedCourses;

    public AccountBase(String username, double balance) {
        this(username, balance, new Course[MAX_CAPACITY], 0);
    }

    AccountBase(String username, double balance, Course[] courses, int numberOfAddedCourses) {
        this.username = username;
        this.balance = balance;
        this.courses = courses;
        this.numberOfAddedCourses = numberOfAddedCourses;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addToBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                    "The amount that has to be added to the balance bust be a positive number");
        }
        balance += amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    protected abstract double applyDiscount(Course course);

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException,
            CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (balance < course.getPrice()) {
            throw new InsufficientBalanceException(
                    "Account %s does not have enough balance: %s $ to buy the course: %s $"
                            .formatted(username, balance, course.getPrice()));
        }
        if (courseIsAlreadyBought(course)) {
            throw new CourseAlreadyPurchasedException(
                    "Account %s has already purchased course \"%s\""
                            .formatted(username, course.getName()));
        }
        if (numberOfAddedCourses == MAX_CAPACITY) {
            throw new MaxCourseCapacityReachedException(
                    "The capacity of purchased courses for account %s has been reached! Unable to buy course %s"
                            .formatted(username, course.getName()));
        }

        double newPrice = applyDiscount(course);

        this.balance -= newPrice;

        Course courseToAdd = new Course(course);
        courseToAdd.purchase();
        this.courses[numberOfAddedCourses++] = courseToAdd;
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete)
            throws CourseNotPurchasedException, ResourceNotFoundException {
        if (course == null) {
            throw new IllegalArgumentException(
                    "Course for account %s is null"
                            .formatted(username));
        }
        if (resourcesToComplete == null) {
            throw new IllegalArgumentException(
                    "resourcesToComplete for account %s are null"
                            .formatted(username));
        }
        if (!courseIsAlreadyBought(course)) {
            throw new CourseNotPurchasedException(
                    "Course %s is not purchased for account %s"
                            .formatted(course.getName(), username));
        }

        for (Resource resourceForCompletion : resourcesToComplete) {
            course.completeResource(resourceForCompletion);
        }
    }

    @Override
    public void completeCourse(Course course, double grade)
            throws CourseNotPurchasedException, CourseNotCompletedException {
        Course completedCourse = getCourse(course);

        if (completedCourse == null) {
            throw new IllegalArgumentException(
                    "Course for account %s is null"
                            .formatted(username));
        }
        if (!(2.00 <= grade && grade <= 6.00)) {
            throw new IllegalArgumentException(
                    "To complete the course %s, the grade should be between 2.00 and 6.00 for account %s"
                            .formatted(course.getName(), username));
        }
        if (!courseIsAlreadyBought(completedCourse)) {
            throw new CourseNotPurchasedException(
                    "Course %s is not purchased for account %s thus cannot be completed"
                            .formatted(course.getName(), username));
        }
        if (!completedCourse.isCompleted()) {
            throw new CourseNotCompletedException(
                    "Course %s has some uncompleted resources and thus cannot be completed fo account %s"
                            .formatted(course.getName(), username));
        }
    }

    private boolean courseIsAlreadyBought(Course course) {
        return getCourse(course) != null;
    }

    private Course getCourse(Course course) {
        for (int i = 0; i < numberOfAddedCourses; i++) {
            if (courses[i].equals(course) && courses[i].isPurchased()) {
                return courses[i];
            }
        }
        return null;
    }

    @Override
    public Course getLeastCompletedCourse() {
        if (numberOfAddedCourses == 0) {
            return null;
        }

        Course leastCompletedCourse = courses[0];
        int leastCompletedPercentage = leastCompletedCourse.getCompletionPercentage();

        for (Course course : courses) {
            if (course == null) {
                break;
            }

            int currentCompletedPercentage = course.getCompletionPercentage();

            if (currentCompletedPercentage < leastCompletedPercentage) {
                leastCompletedCourse = course;
                leastCompletedPercentage = currentCompletedPercentage;
            }
        }

        return leastCompletedCourse;
    }


}
