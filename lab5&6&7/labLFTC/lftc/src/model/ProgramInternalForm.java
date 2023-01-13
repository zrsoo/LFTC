package model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgramInternalForm {
    private List<Map.Entry<Map.Entry<String, Integer>, Map.Entry<Integer, Integer>>> pif = new ArrayList<>();

    public void add(Map.Entry<String, Integer> code, Map.Entry<Integer, Integer> value) {
        Map.Entry<Map.Entry<String, Integer>, Map.Entry<Integer, Integer>> pair = new AbstractMap.SimpleEntry<>(code, value);
        pif.add(pair);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Map.Entry<String, Integer>, Map.Entry<Integer, Integer>> pair : pif) {
            String identifierOrConstant = "";
            if (pair.getKey().getValue() == 0 || pair.getKey().getValue() == 1)
                identifierOrConstant = " - " + pair.getKey().getValue();
            result.append(pair.getKey().getKey()).append(identifierOrConstant).append(" -> (").append(pair.getValue().getKey()).append(", ").append(pair.getValue().getValue()).append(")\n");
        }
        return result.toString();
    }
}
