package model;

// First line       -> nonterminals
// Second line      -> terminals
// Afterwards       -> productions

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {
    private List<String> terminals;
    private List<String> nonterminals;
    private List<Production> productions;

    public Grammar(String path) {
        this.terminals = new ArrayList<>();
        this.nonterminals = new ArrayList<>();
        this.productions = new ArrayList<>();

        readFromFile(path);
    }

    private void readFromFile(String path)
    {
        try{
            File input = new File(path);
            Scanner scanner = new Scanner(input);

            String line;

            // Read nonterminals.
            line = scanner.nextLine();

            // Split by space and add to list.
            this.nonterminals.addAll(Arrays.asList(line.split(" ")));

            // Read terminals and add to list
            line = scanner.nextLine();
            this.terminals.addAll(Arrays.asList(line.split(" ")));

            // Read productions and add to list
            ArrayList<String> elements = new ArrayList<>();
            while(scanner.hasNextLine())
            {
                line = scanner.nextLine();

                elements.addAll(Arrays.asList(line.split(" ")));

                Production production = new Production();

                // First character is initial
                production.setInitial(elements.get(0));

                // Everything after is result.
                elements.remove(0);
                production.setResults(elements);

                this.productions.add(production);

                elements.clear();
            }
        }
        catch (FileNotFoundException | NoSuchElementException exception)
        {
            System.out.println(exception);
        }
    }

    public void printNonterminals()
    {
        System.out.println("NONTERMINALS: " + this.nonterminals);
    }

    public void printTerminals()
    {
        System.out.println("TERMINALS: " + this.terminals);
    }

    public void printProductions()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("PRODUCTIONS:\n");

        this.productions.forEach(production -> {
                    sb.append(production.getInitial()).append(" -> ");

                    production.getResults().forEach(result -> sb.append(result).append(" | "));

                    sb.deleteCharAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);

                    sb.append("\n");
                });

        System.out.println(sb);
    }

    public boolean cfgCheck()
    {
        return this.productions.stream().noneMatch(production -> production.getInitial().length() > 1);
    }

    public List<Production> getProductions() {
        return productions;
    }
}
