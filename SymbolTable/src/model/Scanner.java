package model;

import java.io.*;

public class Scanner {

    public Scanner()
    {

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
