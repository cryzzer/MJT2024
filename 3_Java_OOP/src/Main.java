import bg.sofia.uni.fmi.mjt.udemy.LearningPlatform;
import bg.sofia.uni.fmi.mjt.udemy.Udemy;
import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.account.BusinessAccount;
import bg.sofia.uni.fmi.mjt.udemy.account.EducationalAccount;
import bg.sofia.uni.fmi.mjt.udemy.account.StandardAccount;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class Main {
    public static void main(String[] args) throws AccountNotFoundException, InsufficientBalanceException, MaxCourseCapacityReachedException, CourseAlreadyPurchasedException {
        Account acc1 = new StandardAccount("ivan.ivanov", 200.00);
        Account acc2 = new EducationalAccount("georgi.georgiev", 300.00);

        Category[] categories = new Category[3];
        categories[0] = Category.BUSINESS;
        categories[1] = Category.FINANCE;
        categories[2] = Category.SOFTWARE_ENGINEERING;
        Account acc3 = new BusinessAccount("petar.petrov", 400.00, categories);

        Account[] accounts = new Account[3];
        accounts[0] = acc1;
        accounts[1] = acc2;
        accounts[2] = acc3;

        Resource video1 = new Resource("First video", new ResourceDuration(60));
        Resource video2 = new Resource("Second video", new ResourceDuration(59));
        Resource video3 = new Resource("Third video", new ResourceDuration(58));
        Resource[] resources = {video1, video2, video3};

        Course programming = new Course("Programming with C++",
                "Here we program into C++ language",
                49.99, resources, Category.SOFTWARE_ENGINEERING);
        Course[] courses = {programming};

        LearningPlatform learningPlatform = new Udemy(accounts, courses);

        learningPlatform.getAccount("petar.petrov").buyCourse(programming);

        System.out.printf("Balance after buying course: %s%n", learningPlatform.getAccount("petar.petrov").getBalance());

        System.out.printf("Courses: %s%n", learningPlatform.getAccount("petar.petrov").getLeastCompletedCourse().getName());

    }
}