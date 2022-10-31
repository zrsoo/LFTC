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
}
