package model;

import java.util.ArrayList;
import java.util.List;

public class Production {
    private String initial;
    private List<String> results;

    public Production() {
        this.results = new ArrayList<>();
    }

    public Production(String initial, List<String> results) {
        this.initial = initial;
        this.results = results;
    }

    public Production(String initial) {
        this.initial = initial;
    }

    public void addResult(String result)
    {
        this.results.add(result);
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results.addAll(results);
    }
}
