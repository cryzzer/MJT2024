package task1;

public class Ipv4Task {

    public static boolean validateIPv4Address(String str) {
        int timesDot = 0;

        for (char c : str.toCharArray()) {
            if ((!Character.isDigit(c) && !(c == '.'))) {
                return false;
            } else if (c == '.') {
                if(timesDot < 3){
                    timesDot++;
                }
                else{
                    return false;
                }
            }

        }
        String[] splitArray = str.split("\\.");

        if (splitArray.length != 4) {
            return false;
        }

        for (String num : splitArray) {
            if(num.length() > 1 && num.charAt(0) == '0'){
                return false;
            }
            int number = Integer.parseInt(num);
            if (0 > number || number > 255) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(validateIPv4Address("192.168.1.1"));
        System.out.println(validateIPv4Address("192.168.1.0"));
        System.out.println(validateIPv4Address("192.168.1.00"));
        System.out.println(validateIPv4Address("192.168@1.1"));
        System.out.println(validateIPv4Address("192.168.1.0."));
        System.out.println(validateIPv4Address("1920.168.1.0"));
    }
}
