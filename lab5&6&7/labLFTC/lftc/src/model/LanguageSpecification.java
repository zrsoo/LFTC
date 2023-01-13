package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LanguageSpecification {
    private List<String> reservedWords = Arrays.asList("intreg", "flotant", "sir_caracter", "caracter", "pentru", "cat_timp", "daca", "altfel",
            "citeste", "afiseaza", "fractional", "continua", "iesi", "caz");
    private List<String> operators = Arrays.asList("+", "-", "*", "/", "%", "=", "==", "!=", "<", ">", "<=", ">=");
    private List<String> separators = Arrays.asList("(", ")", ";", "{", "}","[", "]", " ", "\t", "\n", ",");

    public LanguageSpecification() {
    }

    public boolean isReservedWord(String token) {
        return reservedWords.contains(token);
    }

    public boolean isOperator(String token) {
        return operators.contains(token);
    }

    public boolean isPartOfOperator(char op) {
        return op == '!' || isOperator(String.valueOf(op));
    }

    public boolean isSeparator(String token) {
        return separators.contains(token);
    }

    public boolean isIdentifier(String token) {
        String pattern = "^[a-zA-Z]([a-z|A-Z|0-9|_])*$";
        return token.matches(pattern);
    }

    public boolean isConstant(String token) {
        String numericPattern = "^0|[+|-][1-9]([0-9])*|[1-9]([0-9])*|[+|-][1-9]([0-9])*\\.([0-9])*|[1-9]([0-9])*\\.([0-9])*$";
        String charPattern = "^\'[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]\'";
        String stringPattern = "^\"[a-zA-Z0-9_?!#*./%+=<>;)(}{ ]+\"";
        return token.matches(numericPattern) ||
                token.matches(charPattern) ||
                token.matches(stringPattern);
    }

}
