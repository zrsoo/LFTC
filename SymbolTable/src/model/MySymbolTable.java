package model;

import java.util.ArrayList;
import java.util.Optional;

public class MySymbolTable {
    static final int CONST_PRIME = 997;

    private ArrayList<ArrayList<Pair<Integer, String>>> hashTable;
    private Integer position;

    public MySymbolTable(int capacity) {
        this.hashTable = new ArrayList<>(capacity);
        this.position = 0;
    }

    /**
     * Hashes a word to a position in a table
     * @param word - the constant or identifier to be added
     * @return - the position in the table where word will be saved, hash computes position by summing the ascii codes
     *           of the letters of the constant/identifier involved
     */
    private int hash(String word)
    {
        int result = 0;

        for(int i = 0; i < word.length(); ++i)
            result += word.charAt(i);

        return result % CONST_PRIME + 1;
    }

    /**
     * Adds a word to the symbol table
     * @param word word to be added to the symbol table
     * @return the position of the word
     */
    public int add(String word)
    {
        int hashCode = this.hash(word);

        // Get list of computed hashCode
        var list = this.hashTable.get(hashCode);

        // If the word is already in the hashtable, return its position
        Optional<Integer> wordPosition = list
                .stream()
                .filter(pair -> pair.getValue().equals(word))
                .map(Pair::getKey)
                .findFirst();

        if(wordPosition.isPresent())
            return wordPosition.get();

        // If it is not, increment position, add, and return position
        position++;

        Pair<Integer, String> wordPair = new Pair<>(position, word);

        list.add(wordPair);

        return position;
    }
}


