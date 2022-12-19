package model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class Configuration {
    private String currentState;
    private int wordIndex;
    private Deque<String> workingStack;
    private Deque<String> inputStack;
    private String word;
    private List<Production> productions;

    private String currentNonTerminal;
    private int currentProduction;

    public Configuration(List<Production> productions, String word)
    {
        this.currentState = "q";
        this.wordIndex = 1;
        this.workingStack = new ArrayDeque<>();
        this.inputStack = new ArrayDeque<>();
        this.inputStack.addLast("S");
        this.word = word;
        this.productions = productions;
    }

    public Configuration(List<Production> productions)
    {
        this.currentState = "q";
        this.wordIndex = 1;
        this.workingStack = new ArrayDeque<>();
        this.inputStack = new ArrayDeque<>();
        this.inputStack.addLast("S");
        this.productions = productions;
    }

    public boolean parse()
    {
        // While end or error has not been reached;
        while(!this.currentState.equals("f") && !this.currentState.equals("e"))
        {
            // If current state is "normal state";
            if(this.currentState.equals("q"))
            {
                // If input stack is empty and whole word has been parsed, success;
                if(this.wordIndex == this.word.length() + 1 && this.inputStack.isEmpty())
                    return true;

                // TODO check if this peeks correctly
                // If head of input stack is a non-terminal;
                assert this.inputStack.peekLast() != null;
                if(Character.isUpperCase(this.inputStack.peekLast().charAt(0)))
                {
                    this.expand();
                }
                // If head of input stack is a terminal;
                else
                {
                    this.advance();
                }
            }
            // If current state is "back" state;
            else if(this.currentState.equals("b"))
            {

            }
        }

        return true;
    }

    /***
     * WHEN: head of input stack is a non-terminal
     * (q,i, 𝜶, A𝜷) ⊢ (q,i, 𝜶A1, 𝜸1𝜷)
     *
     * where:
     * A → 𝜸1 | 𝜸2 | … represents the productions corresponding to A
     * 1 = first prod of A
     */
    public void expand()
    {
        // Take non-terminal from input stack;
        // TODO check if this removes correctly
        String nonTerminal = this.inputStack.removeLast();

        // Find productions of non-terminal;
        Optional<Production> productionOptional = productions.stream()
                .filter(production1 -> production1.getInitial().equals(nonTerminal))
                .findFirst();
        if(productionOptional.isEmpty())
            throw new RuntimeException("Production cannot be found for NONTERMINAL: " + nonTerminal);
        Production production = productionOptional.get();

        this.currentNonTerminal = nonTerminal;
        this.currentProduction = 1;

        // Insert non-terminal into working stack;
        this.workingStack.addLast(nonTerminal);

        // Insert the first production result into input stack;
        this.inputStack.addLast(production.getResults().get(0));
    }

    /***
     * WHEN: head of input stack is a terminal = current symbol from input
     * (q,i, 𝜶, ai𝜷) ⊢ (q,i+1, 𝜶ai, 𝜷)
     * S
     *
     * OR
     *
     * WHEN: head of input stack is a terminal ≠ current symbol from input
     * (q,i, 𝜶, ai𝜷) ⊢ (b,i, 𝜶, ai𝜷)
     */
    public void advance()
    {
        // Head of input stack is terminal = current symbol from input
        assert this.inputStack.peekLast() != null;
        if(this.inputStack.peekLast().charAt(0) == this.word.charAt(wordIndex))
        {
            this.currentState = "q";

            // Pass to next character in word;
            this.wordIndex++;

            // Pop terminal from "input stack" and add it to "working stack";
            StringBuilder intermediary = new StringBuilder(this.inputStack.removeLast());

            // Add only first char of popped string, because something like "aSbS" might come from pop;
            this.workingStack.addLast(Character.valueOf(intermediary.charAt(0)).toString());

            // Delete popped char form intermediary.
            intermediary.deleteCharAt(0);
            // TODO check if these add correctly (everywhere);
            // Add rest of popped string back.
            this.inputStack.addLast(intermediary.toString());
        }
        // Head of input stack is terminal != current symbol from input - Momentary Insuccess;
        else
        {
            this.currentState = "b";
        }
    }

    /***
     * WHEN: head of working stack is a terminal
     * (b,i, 𝜶a, 𝜷) ⊢ (b,i-1, 𝜶, a𝜷)
     * S
     *
     * OR
     *
     * WHEN: head of working stack is a nonterminal
     * (b,i, 𝜶 Aj, 𝜸j 𝜷) ⊢ (q,i, 𝜶Aj+1, 𝜸j+1𝜷) , if ∃ A → 𝜸j+1
     * (b,i, 𝜶, A𝜷), otherwise with the exception
     * (e,i, 𝜶,𝜷), if i=1, A =S, ERROR
     */
    public void back()
    {
        // If head of working stack is a terminal;
        assert this.workingStack.peekLast() != null;
        if(Character.isUpperCase(this.workingStack.peekLast().charAt(0)))
        {
            // Return to previous character in word;
            this.wordIndex--;

            // Pop terminal from "working stack" and add it to "input stack";
            StringBuilder intermediary = new StringBuilder(this.workingStack.removeLast());

            // Add only first char of popped string, because something like "aSbS" might come from pop;
            this.inputStack.addLast(Character.valueOf(intermediary.charAt(0)).toString());

            // Delete popped char form intermediary.
            intermediary.deleteCharAt(0);
            // TODO check if these add correctly (everywhere);
            // Add rest of popped string back.
            this.workingStack.addLast(intermediary.toString());
        }
        // If head of working stack is a non-terminal;
        else
        {
            this.currentState = "q";

            // Get non-terminal;
            String nonTerminal = Character.valueOf(this.workingStack.removeLast().charAt(0)).toString();

            // TODO check if this works
            // If terminal has changed, reset currentProduction;
            if(!nonTerminal.equals(this.currentNonTerminal))
            {
                this.currentNonTerminal = nonTerminal;
                this.currentProduction = 0;
            }

            // Find productions of non-terminal;
            Optional<Production> productionOptional = productions.stream()
                    .filter(production1 -> production1.getInitial().equals(nonTerminal))
                    .findFirst();
            if(productionOptional.isEmpty())
                throw new RuntimeException("Production cannot be found for NONTERMINAL: " + nonTerminal);
            Production production = productionOptional.get();

            // Get next production;
            String result = production.getResults().get(this.currentProduction);
            this.currentProduction++;

            // TODO Append production to BETA
        }
    }

    public String getCurrentState() {
        return currentState;
    }

    public int getWordIndex() {
        return wordIndex;
    }

    public Deque<String> getWorkingStack() {
        return workingStack;
    }

    public Deque<String> getInputStack() {
        return inputStack;
    }

    public String getWord() {
        return word;
    }

    public List<Production> getProductions() {
        return productions;
    }
}
