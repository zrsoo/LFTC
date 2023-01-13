package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    List<String> nonTerminals = new ArrayList<>();
    List<String> terminals = new ArrayList<>();
    String source;
    List<Production> productions = new ArrayList<>();
    String file;

    public Grammar(String file) {
        this.file = file;
        this.readFromFile();
    }

    public void readFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            this.nonTerminals = Arrays.asList(line.split(" "));
            line = reader.readLine();
            this.terminals = Arrays.asList(line.split(" "));
            line = reader.readLine();
            this.source = line;
            line = reader.readLine();

            while (line != null) {
                Map<Integer, List<String>> rules = new HashMap<>();

                List<String> splited = Arrays.asList(line.split(" -> "));
                String key = splited.get(0);

                List<String> options = Arrays.asList(splited.get(1).split(" \\| "));

                int prodNo = 1;
                for (String option : options) {
                    rules.put(prodNo, Arrays.asList(option.split(" ")));
                    prodNo++;
                }

                productions.add(new Production(key, rules));
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void runMenu() {
        Scanner in = new Scanner(System.in);
        System.out.println("1.Print NonTerminals");
        System.out.println("2.Print Terminals");
        System.out.println("3.Print Source");
        System.out.println("4.Print Productions");
        System.out.println("5.Check CFG");
        System.out.println("6.print productions for nonterminal");
        System.out.println("7.Exit");
        System.out.println("Welcome to grammar menu enter command: ");
        String input = in.nextLine();
        boolean runIt = true;
        while (runIt) {
            switch (input) {
                case "1":
                    System.out.println(getNonTerminalsToString());
                    break;
                case "2":
                    System.out.println(getTerminalsToString());
                    break;
                case "3":
                    System.out.println(source);
                    break;
                case "4":
                    System.out.println(getProductionToString());
                    break;
                case "5":
                    System.out.println(checkIfCFG());
                    break;
                case "6":
                    System.out.println("Enter non terminal:");
                    String nonTerminal = in.nextLine();
                    System.out.println(getProductionForNonTerminalToString(nonTerminal));
                    break;
                case "7":
                    runIt = false;
                    break;
                default:
                    System.out.println("INVALID!");
                    break;
            }
            if (!runIt)
                break;
            System.out.println("Enter command");
            input = in.nextLine();
        }


    }

    public String getNonTerminalsToString() {
        String result = "";
        for (String nonTerminal : nonTerminals) {
            result += nonTerminal + " ";
        }
        return result;
    }

    public String getTerminalsToString() {
        String result = "";
        for (String terminal : terminals) {
            result += terminal + " ";
        }
        return result;
    }

    public String getProductionToString() {
        String result = "";
        for (Production production : productions) {
            result += production.toString() + "\n";
        }
        return result;
    }

    public String getProductionForNonTerminalToString(String key) {
        if (!nonTerminals.contains(key)) {
            return "Non terminal doesn't exist";
        }

        String result = "";
        for (Production production : productions) {
            if (Objects.equals(production.getStart(), key)) {
                return production.toString();
            }
        }

        return result;
    }

    public List<Production> getProductionsForNonterminal(String nonterminal) {
        List<Production> productionsForNonterminal = new LinkedList<>();
        for (Production production : productions) {
            if (production.getStart().equals(nonterminal)) {
                productionsForNonterminal.add(production);
            }
        }
        return productionsForNonterminal;
    }

    public boolean checkIfCFG() {
        if (!nonTerminals.contains(source) || productions.stream().noneMatch(production -> Objects.equals(production.getStart(), source)))
            return false;

        for (String key : productions.stream().map(Production::getStart).toList()) {
            if (!nonTerminals.contains(key))
                return false;

            for (Production production : productions) {
                for (List<String> rule : production.getRules().values()) {
                    for (String symbol : rule) {
                        if (!(nonTerminals.contains(symbol) || terminals.contains(symbol) || Objects.equals(symbol, "epsilon"))) {
                            return false;
                        }
                    }
                }

            }
        }

        return true;
    }

}
