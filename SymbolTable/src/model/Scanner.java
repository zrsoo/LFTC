package model;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Scanner {
    private final static String IDENTIFIER_REGEX = "[a-z]+[0-9]{0,1}";
    private final static String INTEGER_REGEX = "0$|(?!0)[+-]{0,1}[1-9]+[0]*";

    private Set<String> separators = new HashSet<>();

    public Scanner()
    {
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
                separators.add(line);
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
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

        try {
            while ((line = br.readLine()) != null)
            {
                System.out.println(lineNumber + ".)" + line);
                lineNumber++;
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
