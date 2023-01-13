package model;

import java.util.List;
import java.util.Map;

public class Production {
    private final String start;
    private final Map<Integer, List<String>> rules;

    Production(String start, Map<Integer, List<String>> rules) {
        this.start = start;
        this.rules = rules;
    }

    Map<Integer, List<String>> getRules() {
        return this.rules;
    }

    String getStart() {
        return this.start;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.start + " -> ");

        for (List<String> strings : this.rules.values()) {

            for (String o : strings) {
                sb.append(o).append(" ");
            }

            sb.append("| ");
        }

        sb.replace(sb.length() - 3, sb.length() - 1, "");
        return sb.toString();
    }

    public boolean hasNextRule(int n) {
        return n < rules.size();
    }
}

