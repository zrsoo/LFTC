package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Scanner {
    private final static String IDENTIFIER_REGEX = "[a-z]+[0-9]{0,1}";
    private final static String INTEGER_REGEX = "0|(?!0)[+-]{0,1}[1-9]+[0-9]*";
    private final static String CHARACTER_REGEX = "'[a-z]{1}'|'[0-9]{1}'";
    private final static String STRING_REGEX = "\"[a-z0-9A-Z]*\"";
    private final static String FLOAT_REGEX = "[+-]?([0-9]*[.])?[0-9]+";
    private final static String BUL_REGEX = "true|false";

    private final static String SYMBOL_TABLE_PATH = "src\\output\\ST.out";
    private final static String PIF_PATH = "src\\output\\PIF.out";

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    private MyPIF PIF;
    private MySymbolTable symbolTable;

    private List<String> resWords_ops_seps = new ArrayList<>();

    public Scanner()
    {
        PIF = new MyPIF();
        symbolTable = new MySymbolTable(1000);
        readSeparators("token.in");
    }

    private void readSeparators(String path)
    {
        File file = new File(path);

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
            return;
        }

        String line;

        try {
            while ((line = br.readLine()) != null)
            {
                resWords_ops_seps.add(line);
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        resWords_ops_seps.sort(Comparator.comparing(String::length).reversed());
    }

    public void scan(String path)
    {
        File file = new File(path);

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
            return;
        }

        String line;
        Integer lineNumber = 1;

        StringBuilder results = new StringBuilder();

        try {
            // Scanning algorithm
            while ((line = br.readLine()) != null)
            {
                String wrongToken = detect(line);

                if(!wrongToken.equals(""))
                {
                    results
                            .append("Lexical error at line ")
                            .append(lineNumber)
                            .append(": ")
                            .append(line)
                            .append("\nToken \"")
                            .append(wrongToken)
                            .append("\" is not a reserved word, operator, separator, identifier or constant.\n\n");
                }

                lineNumber++;
            }

            if(results.isEmpty())
                printToFile("Lexically correct");
            else
                printToFile(results.toString());
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * For a line, checks that all components of are either reserved words, operators, separators, constants or
     * identifiers.
     * @param line the line to be examined
     * @return "" if line is valid; the wrong token otherwise
     */
    private String detect(String line)
    {
        // Replace all reserved words, operators, separators by " " and split
        // Don't replace '' and "" because they are necessary in the String for Regex validation
        for(String string : resWords_ops_seps)
        {
            // Add reserved word, operator, separator to PIF if it is found
            if(line.contains(string))
                this.PIF.genPIF(string, 0);

            if(!string.equals("'") && !string.equals("\""))
                line  = line.replace(string, " ");
        }

        // Split by space
        List<String> words = new java.util.ArrayList<>(List.of(line.split("\\s+")));

        // Remove any empty words that might remain in the list after split
        words = words.stream().filter(word -> word.length() != 0).collect(Collectors.toList());

        // For the remaining tokens, check if they are either an identifier or a constant
        for(String word : words)
        {
            if(word.matches(IDENTIFIER_REGEX))
            {
                int index = this.symbolTable.add(word);
                this.PIF.genPIF("identifier", index);
                continue;
            }

            if(word.matches(INTEGER_REGEX) ||
            word.matches(CHARACTER_REGEX) ||
            word.matches(STRING_REGEX) ||
            word.matches(FLOAT_REGEX) ||
            word.matches(BUL_REGEX))
            {
                int index = this.symbolTable.add(word);
                this.PIF.genPIF("constant", index);
                continue;
            }

            return word;
        }

        return "";
    }

    private void printToFile(String result)
    {
        File symbolTableFile = new File(SYMBOL_TABLE_PATH);
        File PIFFile = new File(PIF_PATH);

        try{
            if(symbolTableFile.createNewFile())
            {
                System.out.println("ST.out created.");
            }
            else
            {
                System.out.println("ST.out exists and will be overwritten.");
            }

            if(PIFFile.createNewFile())
            {
                System.out.println("PIF.out created.");
            }
            else
            {
                System.out.println("PIF.out exists and will be overwritten.");
            }

            FileWriter symbolTableWriter = new FileWriter(SYMBOL_TABLE_PATH);
            FileWriter PIFWriter = new FileWriter(PIF_PATH);

            symbolTableWriter.write(result);
            symbolTableWriter.write(this.symbolTable.toString());

            PIFWriter.write(result);
            PIFWriter.write(this.PIF.toString());

            symbolTableWriter.close();
            PIFWriter.close();

            System.out.println("Successfully wrote to files.");
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
