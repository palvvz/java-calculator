package calculator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Calculator {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Pattern PATTERN_ROMAN = Pattern.compile("^(X|IX|IV|V?I{0,3})(\\+|\\-|\\*|\\/)(X|IX|IV|V?I{0,3})$");
    private static final Pattern PATTERN_DECIMAL = Pattern.compile("^(10|[1-9])(\\+|\\-|\\*|\\/)(10|[1-9])$");

    public static void main(String[] args) throws Exception {
        String input = "";
        while (true) {
            input = getInput("Enter your expression, (E for exit): ");
            if (input.equals("E")) break;
            String res = calc(input);
            System.out.println("Result: " + res);
        }
    }

    public static String getInput(String question) {
        System.out.println(question);
        return SCANNER.nextLine();
    }

    public static String calc(String input) throws Exception {
        input = input.replaceAll("\\s+", "");
        Parsed p = getParsed(PATTERN_DECIMAL, input);
        if (p.isValid) {
            int res = eval(Integer.parseInt(p.n1), p.op, Integer.parseInt(p.n2));
            return String.valueOf(res);
        }
        p = getParsed(PATTERN_ROMAN, input);
        if (p.isValid) {
            int res = eval(romanToArabic(p.n1), p.op, romanToArabic(p.n2));
            if (res < 0) {
                throw new Exception("Result is negative");
            }
            return arabicToRoman(res);
        }
        throw new Exception("Invalid input string");
    }

    private static Parsed getParsed(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return new Parsed(matcher.group(1), matcher.group(2), matcher.group(3), true);
        }
        return new Parsed("", "", "", false);
    }

    static int eval(int num1, String op, int num2) throws Exception {
        int res = 0;
        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new Exception("Invalid operation, " + op);
        }
    }

    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        int i = 0;
        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }
        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }
        return result;
    }

    public static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }
        return sb.toString();
    }

    record Parsed(String n1, String op, String n2, boolean isValid) {
    }

    enum RomanNumeral {
        I(1), IV(4), V(5), IX(9), X(10),
        XL(40), L(50), XC(90), C(100),
        CD(400), D(500), CM(900), M(1000);
        private int value;

        RomanNumeral(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static List<RomanNumeral> getReverseSortedValues() {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                    .collect(Collectors.toList());
        }
    }
}