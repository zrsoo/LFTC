package model;

import java.io.*;
import java.util.*;

public class MyScanner {

    private final Integer CAPACITY = 15;
    private LanguageSpecification languageSpecification = new LanguageSpecification();
    private ProgramInternalForm programInternalForm = new ProgramInternalForm();
    private SymbolTable symbolTable = new SymbolTable(CAPACITY);

    private String programFile;
    private String PIFFile;
    private String STFile;

    public MyScanner(String programFile, String PIFFile, String STFile) {
        this.programFile = programFile;
        this.PIFFile = PIFFile;
        this.STFile = STFile;
    }

    public void scan() {
        List<Map.Entry<String, Integer>> tokenPairs = new ArrayList<>();
        try {
            File file = new File(programFile);
            Scanner reader = new Scanner(file);

            int lineNr = 1;

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                List<String> tokens = tokenize(line);

                for (String token : tokens) {
                    tokenPairs.add(new AbstractMap.SimpleEntry<>(token, lineNr));
                }

                lineNr++;
            }

            reader.close();

            constructPifAndSt(tokenPairs);
            writeResults();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> tokenize(String line) {
        ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (languageSpecification.isSeparator(String.valueOf(line.charAt(i)))
                    && !(String.valueOf(line.charAt(i))).equals(" ")) {
                tokens.add(String.valueOf(line.charAt(i)));
            } else if (line.charAt(i) == '\"') {
                String constant = identifyStringConstant(line, i);
                tokens.add(constant);
                i += constant.length() - 1;
            } else if (line.charAt(i) == '\'') {
                String constant = identifyCharConstant(line, i);
                tokens.add(constant);
                i += constant.length() - 1;
            } else if (line.charAt(i) == '-') {
                String token = identifyMinusToken(line, i, tokens);
                tokens.add(token);
                i += token.length() - 1;
            } else if (line.charAt(i) == '+') {
                String token = identifyPlusToken(line, i, tokens);
                tokens.add(token);
                i += token.length() - 1;
            } else if (languageSpecification.isPartOfOperator(line.charAt(i))) {
                String operator = identifyOperator(line, i);
                tokens.add(operator);
                i += operator.length() - 1;
            } else if (line.charAt(i) != ' ') {
                String token = identifyToken(line, i);
                tokens.add(token);
                i += token.length() - 1;
            }
        }
        return tokens;
    }

    public String identifyStringConstant(String line, int position) {
        StringBuilder constant = new StringBuilder();

        for (int i = position; i < line.length(); i++) {
            if ((languageSpecification.isSeparator(String.valueOf(line.charAt(i)))
                    || languageSpecification.isOperator(String.valueOf(line.charAt(i))))
                    && ((i == line.length() - 2 && line.charAt(i + 1) != '\"') || (i == line.length() - 1)))
                break;
            constant.append(line.charAt(i));
            if (line.charAt(i) == '\"' && i != position)
                break;
        }

        return constant.toString();
    }

    public String identifyCharConstant(String line, int position) {
        StringBuilder constant = new StringBuilder();

        for (int i = position; i < line.length(); i++) {
            if ((languageSpecification.isSeparator(String.valueOf(line.charAt(i))) ||
                    languageSpecification.isOperator(String.valueOf(line.charAt(i))))
                && ((i == line.length() - 2 && line.charAt(i + 1) != '\'') ||
                    (i == line.length() - 1)))
                break;
            constant.append(line.charAt(i));
            if (line.charAt(i) == '\'' && i != position)
                break;
        }

        return constant.toString();
    }

    public String identifyMinusToken(String line, int position, ArrayList<String> tokens) {
        if (languageSpecification.isIdentifier(tokens.get(tokens.size() - 1)) ||
                languageSpecification.isConstant(tokens.get(tokens.size() - 1))) {
            return "-";
        }

        StringBuilder token = new StringBuilder();
        token.append('-');

        for (int i = position + 1; i < line.length() && (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.'); i++) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    public String identifyPlusToken(String line, int position, ArrayList<String> tokens) {
        if (languageSpecification.isIdentifier(tokens.get(tokens.size() - 1)) || languageSpecification.isConstant(tokens.get(tokens.size() - 1))) {
            return "+";
        }

        StringBuilder token = new StringBuilder();
        token.append('+');

        for (int i = position + 1; i < line.length() && (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.'); ++i) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    public String identifyOperator(String line, int position) {
        StringBuilder operator = new StringBuilder();
        operator.append(line.charAt(position));
        operator.append(line.charAt(position + 1));

        if (languageSpecification.isOperator(operator.toString()))
            return operator.toString();

        return String.valueOf(line.charAt(position));
    }

    public String identifyToken(String line, int position) {
        StringBuilder token = new StringBuilder();

        for (int i = position; i < line.length()
                && !languageSpecification.isSeparator(String.valueOf(line.charAt(i)))
                && !languageSpecification.isPartOfOperator(line.charAt(i))
                && line.charAt(i) != ' '; i++) {
            token.append(line.charAt(i));
        }

        return token.toString();
    }

    public void constructPifAndSt(List<Map.Entry<String, Integer>> tokens) {
        List<String> invalidTokens = new ArrayList<>();
        boolean isLexicallyCorrect = true;

        for (Map.Entry<String, Integer> tokenPair : tokens) {
            String token = tokenPair.getKey();

            if (languageSpecification.isOperator(token) || languageSpecification.isReservedWord(token)
                    || languageSpecification.isSeparator(token)) {
                programInternalForm.add(new AbstractMap.SimpleEntry<>(token, -1), new AbstractMap.SimpleEntry<>(-1, -1));
            } else if (languageSpecification.isIdentifier(token)) {
                symbolTable.add(new AbstractMap.SimpleEntry<>(token, 0));
                Map.Entry<Integer, Integer> position = symbolTable.getPosition(token);
                programInternalForm.add(new AbstractMap.SimpleEntry<>(token, 0), position);
            } else if (languageSpecification.isConstant(token)) {
                symbolTable.add(new AbstractMap.SimpleEntry<>(token, 1));
                Map.Entry<Integer, Integer> position = symbolTable.getPosition(token);
                programInternalForm.add(new AbstractMap.SimpleEntry<>(token, 1), position);
            } else if (!invalidTokens.contains(token)) {
                invalidTokens.add(token);
                isLexicallyCorrect = false;
                System.out.println("Error at line " + tokenPair.getValue() + ": invalid token " + token);
            }
        }

        if (isLexicallyCorrect) {
            System.out.println("Program is lexically correct");
        }
    }

    public void writeResults() {
        try {
            File pifFile = new File(PIFFile);
            FileWriter pifFileWriter = new FileWriter(pifFile, false);
            BufferedWriter pifWriter = new BufferedWriter(pifFileWriter);
            pifWriter.write(programInternalForm.toString());
            pifWriter.close();

            File symbolTableFile = new File(STFile);
            FileWriter symbolTableFileWriter = new FileWriter(symbolTableFile, false);
            BufferedWriter symbolTableWriter = new BufferedWriter(symbolTableFileWriter);
            symbolTableWriter.write(symbolTable.toString());
            symbolTableWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
