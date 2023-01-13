package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private Grammar grammar;
    private Stack<String> workingStack; // terminal or production rule "S 1"
    private List<String> inputStack = new ArrayList<>();
    private StateEnum state;
    private int index;

    public Parser(Grammar grammar, Stack<String> workingStack) {
        this.grammar = grammar;
        this.workingStack = workingStack;
        this.inputStack.add(this.grammar.source);
        this.state = StateEnum.NORMAL;
        this.index = 1;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    public Stack<String> getWorkingStack() {
        return workingStack;
    }

    public void setWorkingStack(Stack<String> workingStack) {
        this.workingStack = workingStack;
    }

    public List<String> getInputStack() {
        return inputStack;
    }

    public void setInputStack(List<String> inputStack) {
        this.inputStack = inputStack;
    }

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void expand() {
        String nonTerminal = this.inputStack.remove(0);
        Production production = this.grammar.getProductionsForNonterminal(nonTerminal).get(0);
        this.workingStack.push(nonTerminal + " 1");
        this.inputStack.addAll(0, production.getRules().get(1));
    }

    public void advance() {
        String nonterminal = this.inputStack.remove(0);
        this.workingStack.add(nonterminal);
        this.index += 1;
    }

    public void momentaryInsuccess() {
        this.state = StateEnum.BACK;
    }

    public void back() {
        String last = this.workingStack.pop();
        this.inputStack.add(0, last);
        this.index -= 1;
    }

    public void anotherTry() {
        //pops from the working stack the head, and checks if there is any next production
        // -> puts in the stack the next production, removes from the input stack the elements beloging to the previous production
        // -> if not and the index is 1 and the poped head is the source than the parser will enter error state
        // -> else remove from the input stack the elements beloging to the rhs of the poped nonterminal and add the poped nonterminal
        String last = this.workingStack.pop();
        String[] lastParts = last.split(" ");
        int productionNumber = Integer.parseInt(lastParts[1]);
        Production terminalProduction = this.grammar.getProductionsForNonterminal(lastParts[0]).get(0);
        if (terminalProduction.hasNextRule(productionNumber)) {
            this.state = StateEnum.NORMAL;
            this.workingStack.push(lastParts[0] + " " + (productionNumber + 1));
            Integer lastLength = terminalProduction.getRules().get(productionNumber).size();
            this.inputStack = this.inputStack.subList(lastLength, inputStack.size());
            this.inputStack.addAll(0, terminalProduction.getRules().get(productionNumber + 1));
        } else if (this.index == 1 && lastParts[0].equals(this.grammar.source)) {
            this.state = StateEnum.ERROR;
        } else {
            Integer lastLength = terminalProduction.getRules().get(productionNumber).size();
            this.inputStack = this.inputStack.subList(lastLength, inputStack.size());
            this.inputStack.add(lastParts[0]);
        }


    }

    public void success() {
        this.state = StateEnum.FINAL;
    }

    @Override
    public String toString() {
        return "RecursiveDescendent{" +
                "workingStack=" + workingStack +
                ", inputStack=" + inputStack +
                ", state='" + state + '\'' +
                ", index=" + index +
                '}';
    }

    public void parsingStrategy(String seq) {
        // Parse a sequence using descendent recursive parsing
        String[] seqSplit = seq.split(" ");
        while (this.state != StateEnum.FINAL && this.state != StateEnum.ERROR) {
            System.out.println(this);
            if (this.state == StateEnum.NORMAL) {
                if (this.index == seqSplit.length + 1 && this.inputStack.isEmpty()) {
                    System.out.println("Success");
                    this.success();
                }
                // empty input stack and end of the sequence not reached => momentary insuccess
                else if (this.inputStack.isEmpty()) {
                    System.out.println("Momentary insuccess");
                    this.momentaryInsuccess();
                }
                // else if head(input stack) is a non-terminal => expand
                else if (this.grammar.nonTerminals.contains(this.inputStack.get(0))) {
                    System.out.println("Expand");
                    this.expand();
                }
                // else if head is a terminal and the current element in the sequence => advance
                else if (this.index < seq.length() && this.inputStack.get(0).equals(seqSplit[this.index - 1])) {
                    System.out.println("Advance");
                    this.advance();
                } else {
                    System.out.println("Momentary insuccess");
                    this.momentaryInsuccess();
                }
                // if index = n+1 and input stack is empty => success
            } else if (this.state == StateEnum.BACK) {
                if (this.grammar.terminals.contains(this.workingStack.get(this.workingStack.size() - 1))) {
                    System.out.println("Back");
                    this.back();
                } else {
                    System.out.println("Another try");
                    this.anotherTry();
                }
            }
        }
        if (this.state == StateEnum.ERROR) {
            System.out.println("Error at index " + this.index + "!");
        } else {
            System.out.println("Sequence " + seq + " is accepted!");
            System.out.println(this.workingStack);
        }
    }
}
