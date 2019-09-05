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
public class Lemma {
    
    private int id_lemma;
    private String lemma_name;
    private String lemma_pos;

    public Lemma() {
    }

    public Lemma(int id_lemma) {
        this.id_lemma = id_lemma;
    }

    public Lemma(String lemma_name) {
        this.lemma_name = lemma_name;
    }

    public Lemma(int id_lemma, String lemma_name, String lemma_pos) {
        this.id_lemma = id_lemma;
        this.lemma_name = lemma_name;
        this.lemma_pos = lemma_pos;
    }

    public int getIdLemma() {
        return id_lemma;
    }

    public void setIdLemma(int id_lemma) {
        this.id_lemma = id_lemma;
    }

    public String getLemmaName() {
        return lemma_name;
    }

    public void setLemmaName(String lemma_name) {
        this.lemma_name = lemma_name;
    }

    public String getLemmaPOS() {
        return lemma_pos;
    }

    public void setLemmaPOS(String lemma_pos) {
        this.lemma_pos = lemma_pos;
    }
}
