/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker;

import grammar.checker.models.Affix;
import grammar.checker.models.Sentence;
import grammar.checker.models.Word;
import grammar.checker.service.AffixService;
import grammar.checker.views.HomePanel;
import grammar.checker.views.JFrame;
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
import jsastrawi.morphology.defaultimpl.Removal;
import jsastrawi.morphology.defaultimpl.RemovalImpl;
import jsastrawi.morphology.defaultimpl.visitor.VisitorProvider;

/**
 *
 * @author Ru'fatiani
 */
public class GrammarChecker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        // TODO code application logic here
        
        Set<String> dictionary = new HashSet<String>();
        ConnectionManager conn = new ConnectionManager();;
    
        InputStream in = Lemmatizer.class.getResourceAsStream("/root-words.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String line;
        while((line = br.readLine()) != null){
            dictionary.add(line);
         }
        
        Lemmatizer lemmatizer = new DefaultLemmatizer(dictionary);
    
        System.out.println(lemmatizer.lemmatize("mengetahui"));
        System.out.println(lemmatizer.lemmatize("masukan"));
        System.out.println(lemmatizer.lemmatize("pesta olahraga"));
        String s = "ka'ban";
        System.out.println(s.replace("'", "\\'"));
        
        Context context = new Context("menangis", dictionary, new VisitorProvider());
        context.execute();
        
        for(int i =0; i<context.getRemovals().size(); i++){
            System.out.println(i+ " " +context.getRemovals().get(i).getRemovedPart());
        }
        
        AffixService affix = new AffixService();
        Affix a = new Affix();
        a = affix.getAffix("masukan", affix.getLemma("masukan"));
        
        System.out.println(a.getPrefix1() + " " + a.getPrefix2() + " " + a.getPrefix3() + " " + a.getSuffix1()+ " " + a.getSuffix2());
        
    }
}
