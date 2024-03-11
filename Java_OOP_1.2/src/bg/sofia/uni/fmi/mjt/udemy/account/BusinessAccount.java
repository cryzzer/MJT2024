package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;

public class BusinessAccount extends AccountBase {
    private final Category[] allowedCategories;

    public BusinessAccount(String username, double balance, Category[] allowedCategories) {
        super(username, balance);
        this.allowedCategories = allowedCategories;
    }

    @Override
    protected double applyDiscount(Course course) {
        if (!isValidCategory(course.getCategory())) {
            throw new IllegalArgumentException(
                    "Category is not in the allowed categories array thus cannot apply discount for account %s and course %s"
                            .formatted(getUsername(), course.getName()));
        }
        return course.getPrice() - (course.getPrice() * AccountType.BUSINESS.getDiscount());
    }

    private boolean isValidCategory(Category category) {
        for (Category availableCategory : allowedCategories) {
            if (availableCategory.equals(category)) {
                return true;
            }
        }
        return false;
    }
}
