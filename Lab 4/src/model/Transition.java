package model;

public class Transition {
    private String initialState;
    private Character alphabetLetter;
    private String finalState;

    public Transition(String initialState, Character alphabetLetter, String finalState) {
        this.initialState = initialState;
        this.alphabetLetter = alphabetLetter;
        this.finalState = finalState;
    }

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public Character getAlphabetLetter() {
        return alphabetLetter;
    }

    public void setAlphabetLetter(Character alphabetLetter) {
        this.alphabetLetter = alphabetLetter;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setFinalState(String finalState) {
        this.finalState = finalState;
    }
}
