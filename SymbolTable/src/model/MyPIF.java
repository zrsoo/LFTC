package model;

import java.util.ArrayList;
import java.util.List;

public class MyPIF {
    private List<Pair<String, Integer>> entries;

    public MyPIF()
    {
        this.entries = new ArrayList<>();
    }

    public void genPIF(String token, Integer position)
    {
        this.entries.add(new Pair<>(token, position));
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("PIF:").append("\n");
        for(Pair<String, Integer> pair : entries)
        {
            stringBuilder
                    .append("(token: ")
                    .append(pair.getKey())
                    .append(")")
                    .append("  ->  ")
                    .append("(pos: ")
                    .append(pair.getValue())
                    .append(")")
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}
