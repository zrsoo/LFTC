package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FiniteAutomata {
    private List<String> stateSet;
    private List<Integer> alphabet;
    private List<Transition> transitionFunction;
    private String initialState;
    private List<String> finalState;

    private String currentState;
    private boolean canStop;

    public FiniteAutomata(String path) {
        this.stateSet = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.transitionFunction = new ArrayList<>();

        this.finalState = new ArrayList<>();

        initializeFromFile(path);

        this.currentState = this.initialState;
        this.canStop = false;
    }

    public FiniteAutomata(List<String> stateSet, List<Integer> alphabet, List<Transition> transitionFunction,
                          String initialState, List<String> finalState) {
        this.stateSet = stateSet;
        this.alphabet = alphabet;
        this.transitionFunction = transitionFunction;
        this.initialState = initialState;
        this.finalState = finalState;
    }

    public List<String> getStateSet() {
        return stateSet;
    }

    public List<Integer> getAlphabet() {
        return alphabet;
    }

    public List<Transition> getTransitionFunction() {
        return transitionFunction;
    }

    public String getInitialState() {
        return initialState;
    }

    public List<String> getFinalState() {
        return finalState;
    }

    public void setStateSet(List<String> stateSet) {
        this.stateSet = stateSet;
    }

    public void setAlphabet(List<Integer> alphabet) {
        this.alphabet = alphabet;
    }

    public void setTransitionFunction(List<Transition> transitionFunction) {
        this.transitionFunction = transitionFunction;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public void setFinalState(List<String> finalState) {
        this.finalState = finalState;
    }

    private void initializeFromFile(String path)
    {
        try{
            File input = new File(path);
            Scanner scanner = new Scanner(input);

            String line;

            // Read state set
            line = scanner.nextLine();

            // Remove first and last bracket
            line = line.replace("(", "");
            line = line.replace(")", "");

            // Split by "," and add results to state set
            this.stateSet = List.of(line.split(","));

            // Read alphabet
            line = scanner.nextLine();

            // Remove first and last bracket
            line = line.replace("(", "");
            line = line.replace(")", "");

            // Split by "," and add results to alphabet
            this.alphabet = Stream.of(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());

            // Read transition list
            line = scanner.nextLine();

            // Remove first and last bracket
            line = line.replace("[", "");
            line = line.replace("]", "");

            // Split by "," and process each transition, adding it to the transitions list
            Stream.of(line.split(";"))
                    .forEach(rawTransition -> {
                        // Split again by ",", and assign each element to a new Transition object,
                        // that is then added to the list
                        List<String> components = List.of(rawTransition.split(","));
                        Transition transition = new Transition(components.get(0).replace("(", ""),
                                components.get(1).charAt(0), components.get(2).replace(")", ""));
                        this.transitionFunction.add(transition);
                    });

            // Read initial state
            line = scanner.nextLine();
            line = line.replace("{", "");
            line = line.replace("}", "");
            this.initialState = line;

            // Read final state
            line = scanner.nextLine();
            line = line.replace("{", "");
            line = line.replace("}", "");
            this.finalState.add(line);
        }
        catch (FileNotFoundException | NoSuchElementException exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    public void printStates()
    {
        System.out.println(this.stateSet);
    }

    public void printAlphabet()
    {
        System.out.println(alphabet);
    }

    public void printTransitions()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");
        this.transitionFunction.forEach(transition -> stringBuilder
                .append(transition.getInitialState())
                .append(", ")
                .append(transition.getAlphabetLetter())
                .append(", ")
                .append(transition.getFinalState())
                .append("\n"));

        System.out.println(stringBuilder);
    }

    public void printInitialState()
    {
        System.out.println(this.initialState);
    }

    public void printFinalState()
    {
        System.out.println(this.finalState);
    }

    /***
     * Performs one transit operation from a state to the next one, based on a letter, and keeps track of nature of
     * current state.
     * @param letter the letter that decides the branch
     */
    public void transit(Character letter)
    {
        this.currentState = this.getNextState(this.currentState, letter);
        this.canStop = this.finalState.contains(this.currentState);
    }

    /***
     * Based on a given state and a given alphabet letter, returns the next state.
     * @param state the current state
     * @param letter the alphabet letter
     * @return the next state
     */
    private String getNextState(String state, Character letter)
    {
        Optional<Transition> transitionOptional = this.transitionFunction.stream()
                .filter(transition -> transition.getInitialState().equals(state) && transition.getAlphabetLetter().equals(letter))
                .findFirst();

        if(transitionOptional.isEmpty())
            throw new RuntimeException("Incorrect sequence");

        return transitionOptional.get().getFinalState();
    }

    /***
     * Based on a sequence, performs a transit for each character and returns true if the FA is in a terminal state.
     * @param sequence the input sequence
     * @return true or false, depending on the FA state
     */
    public boolean checkSequence(String sequence)
    {
        for(int i = 0; i < sequence.length(); ++i)
            this.transit(sequence.charAt(i));

        return this.canStop;
    }
}
