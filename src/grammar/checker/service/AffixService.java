/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.service;

import grammar.checker.dao.AffixDAO;
import grammar.checker.dao.LemmaDAO;
import grammar.checker.models.Affix;
import grammar.checker.models.Lemma;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import jsastrawi.morphology.DefaultLemmatizer;
import jsastrawi.morphology.Lemmatizer;
import jsastrawi.morphology.defaultimpl.Context;
import jsastrawi.morphology.defaultimpl.visitor.VisitorProvider;

/**
 *
 * @author Ru'fatiani
 */
public class AffixService {
    
    private Set<String> dictionary = new HashSet<String>();

    public AffixService() throws IOException {
        InputStream in = Lemmatizer.class.getResourceAsStream("/root-words.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String line;
        while((line = br.readLine()) != null){
            dictionary.add(line);
         }
    }
    
    public String getLemma(String word){
        Lemmatizer lemmatizer = new DefaultLemmatizer(dictionary);
        return lemmatizer.lemmatize(word);
    }
    
    public Affix getAffix(String word, String lemma) throws IOException, SQLException{
        LemmaDAO lemmaDao = new LemmaDAO();
        
        Context context = new Context(word, dictionary, new VisitorProvider());
        context.execute();
       
        Affix affix = new Affix();
        for(int i=0;i<context.getRemovals().size();i++){
            String s = context.getRemovals().get(i).getRemovedPart();
            
            if(s.length()>=3){
                s = s.substring(0, 3);
                if(s.substring(0, 3).equals("ber") || s.substring(0, 3).equals("per") || s.substring(0, 3).equals("ter")){
                }else{
                    if(s.equals("pel")){
                        s = "per";
                    }else{
                        String temp = s.substring(0, 2);
                        if(temp.equals("be"))
                            s = "ber";
                        else if(temp.equals("me") || temp.equals("pe"))
                            s = temp;
                    }
                }
            }
            
            if(s.equals("me") || s.equals("ber") || s.equals("per") || s.equals("se") || s.equals("ke") || s.equals("di") || s.equals("ter") || s.equals("pe") || s.equals("ku") ){
                boolean status = true;
                
                if(s.equals("ku")){
                    String temp = word.substring(0, 2);
                    if(!temp.equals(s)) status = false;
                }
                
                if(status == true){
                    if(affix.getPrefix1() == null || affix.getPrefix1().equals(""))
                    affix.setPrefix1(s);
                else if(affix.getPrefix2() == null || affix.getPrefix2().equals(""))
                    affix.setPrefix2(s);
                else
                    affix.setPrefix3(s);
                }else{
                    if(affix.getSuffix1() == null || affix.getSuffix1().equals(""))
                        affix.setSuffix1(s);
                    else
                        affix.setSuffix2(s);
                }
                
            }else if(s.equals("kah")|| s.equals("pun") || s.equals("kan") || s.equals("an") || s.equals("mu") || s.equals("lah") || s.equals("i") || s.equals("tor") || s.equals("sasi") || s.equals("asi") || s.equals("wati") || s.equals("wan") || s.equals("nya") || s.equals("iah") || s.equals("wiah") || s.equals("if") || s.equals("er") || s.equals("al") || s.equals("is")){
                if(affix.getSuffix1() == null || affix.getSuffix1().equals(""))
                    affix.setSuffix1(s);
                    affix.setSuffix2(s);
            }
        }
        
        return affix;
    }
    
    private String getPrefix(String s){
        if(s.length()>=3){
            if(s.substring(0, 3).equals("ber") || s.substring(0, 3).equals("per") || s.substring(0, 3).equals("ter")){
            }else{
                String temp = s.substring(0, 2);
                if(temp.equals("be"))
                    s = "ber";
                else if(temp.equals("pe"))
                    s = "per";
                else
                    s = temp;
            }
        }
        
        return s;
    }
    
    public boolean isEquals(Affix a, Affix b){  
        if(a.getPrefix1() != null){
            if(b.getPrefix1() != null){
                if(!a.getPrefix1().equals(b.getPrefix1()))
                return false;
            }else
                return false;
        }
        
        if(a.getPrefix2() != null){
            if(b.getPrefix2() != null){
                if(!a.getPrefix2().equals(b.getPrefix2()))
                return false;
            }else
                return false;
        }
        
        if(a.getPrefix3() != null){
            if(b.getPrefix3() != null){
                if(!a.getPrefix3().equals(b.getPrefix3()))
                return false;
            }else
                return false;
        }
        
        if(a.getSuffix1()!= null){
            if(b.getSuffix1() != null){
                if(!a.getSuffix1().equals(b.getSuffix1()))
                return false;
            }else
                return false;
        }
        
        if(a.getSuffix2() != null){
            if(b.getSuffix2() != null){
                if(!a.getSuffix2().equals(b.getSuffix2()))
                return false;
            }else
                return false;
        }
        
        return true;
    }
    
    public String getMessageAffix(Affix affix){
        String message=", mungkin lebih baik diberi imbuhan ";
        
        if(affix.getPrefix1() != null){
            message = message + affix.getPrefix1() + "- ";
        }
        
        if(affix.getPrefix2()!= null){
            message = message + affix.getPrefix2()+ "- ";
        }
        
        if(affix.getPrefix3()!= null){
            message = message + affix.getPrefix3()+ "- ";
        }
        
        if(affix.getSuffix1()!= null){
            message = message + "-" + affix.getSuffix1()+ " ";
        }
        
        if(affix.getSuffix2()!= null){
            message = message + "-" + affix.getSuffix2()+ " ";
        }
        
        return message;
    }
}
