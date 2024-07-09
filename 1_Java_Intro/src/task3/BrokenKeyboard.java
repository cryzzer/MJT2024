package task3;

public class BrokenKeyboard {
    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        if (message.isBlank()) {
            return 0;
        }

        String[] words = message.replaceAll("\\s+", " ").trim().split(" ");
        int numWords = words.length;

        for (String word : words) {
            for (char c : brokenKeys.toCharArray()) {
                if (word.contains(Character.toString(c))) {
                    numWords--;
                    break;
                }
            }
        }
        return numWords;
    }

    public static void main(String[] args) {
        System.out.println(calculateFullyTypedWords("i love mjt", "qsf3o"));
        System.out.println(calculateFullyTypedWords("secret      message info      ", "sms"));
        System.out.println(calculateFullyTypedWords("dve po 2 4isto novi beli kecove", "o2sf"));
        System.out.println(calculateFullyTypedWords("     ", "asd"));
        System.out.println(calculateFullyTypedWords(" - 1 @ - 4", "s"));
    }
}
