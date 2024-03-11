import java.util.Arrays;
import java.util.Objects;

public class Task {
    public static int[] addArrays(int[] a, int[] b) {
        // 1. Validation
        if (a == null || b == null) {
            return validationArray(a, b);
        }

        // 2. Check which array is larger
        int maxLength = Math.max(a.length, b.length);

        // 3. Create the new Array
        int[] sumArray = new int[maxLength + 1];


        int carry = 0;
        int aIndex = a.length - 1;
        int bIndex = b.length - 1;
        int sumIndex = sumArray.length - 1;

        // 4. Iterate through all elements and add them,
        // starting from the end of the arrays
        while (aIndex >= 0 || bIndex >= 0){
            sumArray[sumIndex] += carry;

            // 4.1 Check if the index is within bounds of the array
            if(aIndex >= 0){
                sumArray[sumIndex] += a[aIndex];
                aIndex--;
            }
            if (bIndex >= 0) {
                sumArray[sumIndex] += b[bIndex];
                bIndex--;
            }

            // 5. Check if the new number is <= 9, if not add the excess
            // to a carry number and make the index number a singe digit
            if (sumArray[sumIndex] > 9) {
                carry = sumArray[sumIndex] / 10;
                sumArray[sumIndex] = sumArray[sumIndex] % 10;
            } else {
                carry = 0;
            }
            sumIndex--;
        }

        // 6. Return the array with the leading carry number in front,
        // otherwise return the array from the 1st index, since index 0 is 0
        if (carry != 0) {
            sumArray[0] = carry;
            return sumArray;
        } else {
            return Arrays.copyOfRange(sumArray, 1, sumArray.length);
        }
    }

    private static int[] validationArray(int[] a, int[] b) {
        if (a == null && b == null) {
            return new int[]{};
        } else return Objects.requireNonNullElse(a, b);
    }


    public static void main(String[] args) {
        int[] a = {1,2,3};
        int[] b = {1,2,3};
        System.out.println("Equal size: " + Arrays.toString(addArrays(a, b)));

        int[] c = null;
        System.out.println("\"c\" is null: " + Arrays.toString(addArrays(a, c)));

        int[] d = {9,9,9};
        int[] e = {4,3};
        System.out.println("Different size and additional digit: " + Arrays.toString(addArrays(d, e)));
    }
}

