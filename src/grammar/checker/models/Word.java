/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.models;

/**
 *
 * @author Ru'fatiani
 */
public class Word {
    
    private int id_word;
    private String word_name;
    private String word_pos;
    private Affix affix;
    private Lemma lemma;

    public Word() {
    }

    public Word(int id_word) {
        this.id_word = id_word;
    }
    
    public Word(String word_name){
        this.word_name = word_name;
    }

    public Word(String word_name, String word_pos) {
        this.word_name = word_name;
        this.word_pos = word_pos;
    }

    public Word(int id_word, String word_name, String word_pos, Affix affix, Lemma lemma) {
        this.id_word = id_word;
        this.word_name = word_name;
        this.word_pos = word_pos;
        this.affix = affix;
        this.lemma = lemma;
    }

    public int getIdWord() {
        return id_word;
    }

    public void setIdWord(int id_word) {
        this.id_word = id_word;
    }

    public String getWordName() {
        return word_name;
    }

    public void setWordName(String word_name) {
        this.word_name = word_name;
    }

    public String getWordPOS() {
        return word_pos;
    }

    public void setWordPOS(String word_pos) {
        this.word_pos = word_pos;
    }

    public Affix getAffix() {
        return affix;
    }

    public void setAffix(Affix affix) {
        this.affix = affix;
    }

    public Lemma getLemma() {
        return lemma;
    }

    public void setLemma(Lemma lemma) {
        this.lemma = lemma;
    }

    
}
