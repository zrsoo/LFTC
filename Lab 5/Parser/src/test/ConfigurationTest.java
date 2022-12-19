package test;

import model.Configuration;
import model.Grammar;
import model.Production;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {
    Grammar grammar;

    private ConfigurationTest()
    {
        this.grammar = new Grammar("grammar.txt");
        this.runTests();
    }

    public static void runAllTests()
    {
        new ConfigurationTest();
    }

    @Test
    public void testExpand()
    {
        Configuration configuration = new Configuration(grammar.getProductions());

        // Assert head of input stack is non-terminal.
        assertNotNull(configuration.getInputStack().peekLast());
        assertTrue(Character.isUpperCase(configuration.getInputStack().peekLast().charAt(0)));

        // Get non-terminal.
        Character nonTerminal = configuration.getInputStack().peekLast().charAt(0);

        configuration.expand();

        // Assert head of working stack is the acquired non-terminal.
        assertNotNull(configuration.getWorkingStack().peekLast());
        assertEquals(nonTerminal, configuration.getWorkingStack().peekLast().charAt(0));

        // Assert head of input stack is the first production of the non-terminal;
        Optional<Production> production = grammar.getProductions().stream()
                .filter(production1 -> production1.getInitial().equals(nonTerminal.toString()))
                .findFirst();
        assertTrue(production.isPresent());
        assertEquals(configuration.getInputStack().peekLast(), production.get().getResults().get(0));
    }

    void runTests()
    {
        testExpand();
    }
}
