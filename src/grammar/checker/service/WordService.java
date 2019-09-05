/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.service;

import grammar.checker.models.Word;
import java.util.ArrayList;

/**
 *
 * @author Ru'fatiani
 */
public class WordService {

    public WordService() {
    }
    
    public int wordPositionByName(ArrayList<Word> words, Word word){
        int position=-1;
        
        for(int i=0;i<words.size();i++){
            if(words.get(i).getWordName().toLowerCase().equals(word.getWordName().toLowerCase())){
                position = i;
                break;
            }
        }
        
        return position;
    }
    
    public ArrayList<Integer> wordPositionsByName(ArrayList<Word> words, Word word){
        ArrayList<Integer> position = new ArrayList<>();
        
        for(int i=0;i<words.size();i++){
            if(words.get(i).getWordName().toLowerCase().equals(word.getWordName().toLowerCase())){
                position.add(i);
            }
        }
        
        return position;
    }
    
    public int wordPositionByPOS(ArrayList<Word> words, Word word){
        int position =  -1;

        for(int i=0;i<words.size();i++){
            if(words.get(i).getWordPOS().toLowerCase().equals(word.getWordPOS().toLowerCase())){
                position = i;
                break;
            }
        }
        
        return position;
    }
    
    public ArrayList<Integer> wordPositionsByPOS(ArrayList<Word> words, Word word){
        ArrayList<Integer> position = new ArrayList<>();
        
        for(int i=0;i<words.size();i++){
            if(words.get(i).getWordPOS().toLowerCase().equals(word.getWordPOS().toLowerCase())){
                position.add(i);
            }
        }
        
        return position;
    }
    
    public String printWords(ArrayList<Word> words){
        String sentence="\"";
        for(int i=0;i<words.size();i++){
            sentence = sentence + words.get(i).getWordName() + " ";
        }
        sentence = sentence + "\"";
        
        return sentence;
    }
    
    
}
