package model;

import java.util.*;
import java.util.stream.Collectors;

public class SymbolTable {
    private ArrayList<ArrayList<Map.Entry<String, Integer>>> items;
    private int size;

    public SymbolTable(int size) {
        this.size = size;
        this.items = new ArrayList<>();
        for (int i = 0; i < size; i++)
            this.items.add(new ArrayList<>());
    }

    public int getSize() {
        return size;
    }

    private int hash(String key) {
        int sum = 0;

        for (int i = 0; i < key.length(); i++)
            sum += key.charAt(i);

        return sum % size;
    }

    public boolean add(Map.Entry<String, Integer> pair){
        int hashValue = hash(pair.getKey());

        if(!items.get(hashValue).contains(pair)){
            items.get(hashValue).add(pair);
            return true;
        }
        return false;
    }

    public boolean contains(String key){
        int hashValue = hash(key);

        return items.get(hashValue).stream().map(Map.Entry::getKey).toList().contains(key);
    }

    public Map.Entry<Integer, Integer> getPosition(String key){
        if (this.contains(key)){
            int listPosition = this.hash(key);
            int listIndex = 0;
            for (Map.Entry pair : this.items.get(listPosition)) {
                if (!pair.getKey().equals(key))
                    listIndex++;
                else
                    break;
            }

            return new AbstractMap.SimpleEntry<>(listPosition, listIndex);
        }
        return new AbstractMap.SimpleEntry<>(-1, -1);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(i).append(": [");
            String separator = "";
            for(Map.Entry pair : items.get(i)){
                String pairToString = "[" + pair.getKey() + "->" + pair.getValue() + "]";
                result.append(separator);
                separator = ", ";
                result.append(pairToString);
            }
            result.append("]\n");
        }
        return result.toString();
    }
}

