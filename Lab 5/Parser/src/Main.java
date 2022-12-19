import model.Grammar;
import test.ConfigurationTest;

public class Main {
    public static void main(String[] args)
    {
        Grammar grammar = new Grammar("grammar.txt");
        ConfigurationTest.runAllTests();

        grammar.printNonterminals();
        grammar.printTerminals();
        grammar.printProductions();
    }
}
