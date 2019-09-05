/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.models;

import java.util.ArrayList;

/**
 *
 * @author Ru'fatiani
 */
public class Sentence {
    
    private ArrayList<Word> words;

    public Sentence() {
    }

    public Sentence(ArrayList<Word> words) {
        this.words = words;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
    
}
