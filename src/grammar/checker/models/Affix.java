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
public class Affix {
    
    private int id_affix;
    private String prefix_1;
    private String prefix_2;
    private String prefix_3;
    private String suffix_1;
    private String suffix_2;

    public Affix() {
    }

    public Affix(int id_affix) {
        this.id_affix = id_affix;
    }
    
    

    public Affix(int id_affix, String prefix_1, String prefix_2, String prefix_3, String suffix_1, String suffix_2) {
        this.id_affix = id_affix;
        this.prefix_1 = prefix_1;
        this.prefix_2 = prefix_2;
        this.prefix_3 = prefix_3;
        this.suffix_1 = suffix_1;
        this.suffix_2 = suffix_2;
    }

    public int getIdAffix() {
        return id_affix;
    }

    public void setIdAffix(int id_affix) {
        this.id_affix = id_affix;
    }

    public String getPrefix1() {
        return prefix_1;
    }

    public void setPrefix1(String prefix_1) {
        this.prefix_1 = prefix_1;
    }

    public String getPrefix2() {
        return prefix_2;
    }

    public void setPrefix2(String prefix_2) {
        this.prefix_2 = prefix_2;
    }

    public String getPrefix3() {
        return prefix_3;
    }

    public void setPrefix3(String prefix_3) {
        this.prefix_3 = prefix_3;
    }

    public String getSuffix1() {
        return suffix_1;
    }

    public void setSuffix1(String suffix_1) {
        this.suffix_1 = suffix_1;
    }

    public String getSuffix2() {
        return suffix_2;
    }

    public void setSuffix2(String suffix_2) {
        this.suffix_2 = suffix_2;
    }
}
