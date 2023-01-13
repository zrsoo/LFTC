import model.Grammar;
import model.MyScanner;
import model.Parser;

import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar("lftc/src/assets/testparser/g3.txt");
        Parser parser = new Parser(grammar, new Stack<>());
        parser.parsingStrategy("a c b c");
    }
}