/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker;

import grammar.checker.views.HomePanel;
import grammar.checker.views.JFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        Set<String> dictionary = new HashSet<String>();
    
        InputStream in = Lemmatizer.class.getResourceAsStream("/root-words.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String line;
        while((line = br.readLine()) != null){
            dictionary.add(line);
         }
        
        Lemmatizer lemmatizer = new DefaultLemmatizer(dictionary);
    
        System.out.println(lemmatizer.lemmatize("menulis"));
        System.out.println(lemmatizer.lemmatize("saran"));
        System.out.println(lemmatizer.lemmatize("menyusui"));
        
        Context context = new Context("berasa", dictionary, new VisitorProvider());
        context.execute();
        
        System.out.println(context.getRemovals().size()+ " " +context.getRemovals().get(0).getRemovedPart());
    }
    
}
