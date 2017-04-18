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
public class Corpus {
    
    private int corpus_id;
    private ArrayList<Word> words;

    public Corpus() {
    }

    public Corpus(int corpus_id, ArrayList<Word> words) {
        this.corpus_id = corpus_id;
        this.words = words;
    }

    public int getCorpus_id() {
        return corpus_id;
    }

    public void setCorpus_id(int corpus_id) {
        this.corpus_id = corpus_id;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
    
    
    
}
