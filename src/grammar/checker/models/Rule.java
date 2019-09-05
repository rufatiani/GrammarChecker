/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.models;

import grammar.checker.service.AffixService;
import grammar.checker.service.WordService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Ru'fatiani
 */
public class Rule {
    private WordService wordService = new WordService();

    public Rule() {
    }
    
    public String ruleDiKe(ArrayList<Word> words , int position){
        String message ="";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NND")){
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda.";
        }else{
            if(words.get(position - 1).getWordPOS().equals("VB") || words.get(position - 1).getWordPOS().equals("NN")){
                if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NND")){
                }else{
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda.";
                }
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja atau kata benda.";
        }
        return message;
    }
    
    public String ruleDiKe2(ArrayList<Word> words , int position){
        String message = "";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NND")){
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda.";
        }else{
            int i=position-1;
            boolean status = false;
            while(i>0 && !words.get(i).getWordPOS().equals("Z") && !words.get(i).getWordPOS().equals("CC") && !words.get(i).getWordPOS().equals("SC")){
                if(words.get(i).getWordPOS().equals("VB")){
                    status=true;
                    break;
                }
                i--;
            }
            
            if(status==false)
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja.";
            else{
                if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NND")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda.";
            } 
        }
        
        return message;
    }
    
    public String ruleDari(ArrayList<Word> words , int position){
        String message = "";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("CD") || words.get(position + 1).getWordPOS().equals("PR") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("PRP")){
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda, kata bilangan, pronomina, pronomina penunjuk atau proper noun.";
        }else{
            String pos = words.get(position - 1).getWordPOS();
            if(words.get(position - 1).getWordPOS().equals("VB")){
                if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("CD")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda atau kata bilangan.";
            }else if(words.get(position - 1).getWordPOS().equals("PRP") || words.get(position - 1).getWordPOS().equals("NNP")){
                if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("PR") || words.get(position + 1).getWordPOS().equals("NNP")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda atau pronomina penunjuk.";
            }else if(words.get(position - 1).getWordPOS().equals("NN") || words.get(position - 1).getWordName().toLowerCase().equals("itu")){
                if(words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina atau proper noun.";   
            }else if(words.get(position - 1).getWordPOS().equals("CD")){
                if(words.get(position + 1).getWordPOS().equals("CD")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh numeralia.";   
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja, pronomina atau kata benda.";
        }
        
        return message;
    }
    
    public String ruleDengan(ArrayList<Word> words , int position){
        String message="";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("JJ") || words.get(position + 1).getWordPOS().equals("VB")){  
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda, kata sifat atau kata kerja.";
        }else{
            if(words.get(position - 1).getWordPOS().equals("VB") || words.get(position - 1).getWordPOS().equals("NN") || words.get(position - 1).getWordName().toLowerCase().equals("itu")){
                if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("JJ")){  
                }else{
                    if(words.get(position - 1).getWordPOS().equals("NN") || words.get(position - 1).getWordName().toLowerCase().equals("itu")){
                        if(!words.get(position + 1).getWordPOS().equals("VB"))
                            message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda, kata sifat atau kata kerja.";
                    }else
                        message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda atau kata sifat.";
                }
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja atau kata benda.";
        }
        
        return message;
    }
    
    public String ruleDengan2(ArrayList<Word> words , int position){
        String message = "";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("JJ") || words.get(position + 1).getWordPOS().equals("VB")){  
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda, kata sifat atau kata kerja.";
        }else{
            int i=position-1;
            boolean status = false;
            while(i>0 && !words.get(i).getWordPOS().equals("Z") && !words.get(i).getWordPOS().equals("CC") && !words.get(i).getWordPOS().equals("SC")){
                if(words.get(i).getWordPOS().equals("VB")){
                    status=true;
                    break;
                }
                i--;
            }
            
            if(status==false)
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja.";
            else{
                if(words.get(position + 1).getWordPOS().equals("NN") || words.get(position + 1).getWordPOS().equals("JJ") || words.get(position + 1).getWordPOS().equals("VB")){  
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh kata benda, kata sifat atau kata kerja.";
            }
        }
        
        return message;
    }
    
    public String ruleOleh(ArrayList<Word> words , int position, AffixService affixService) throws IOException, SQLException{
        String message="";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP")){ 
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina atau proper noun.";
        }else{
            words.get(position - 1).setAffix(affixService.getAffix(words.get(position - 1).getWordName(), affixService.getLemma(words.get(position - 1).getWordName())));
            
            if(words.get(position - 1).getAffix().getPrefix1() != null){
                if(words.get(position - 1).getWordPOS().equals("VB") && words.get(position - 1).getAffix().getPrefix1().toLowerCase().equals("di")){
                    if(words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP")){ 
                    }else
                        message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina atau proper noun.";
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja pasif.";
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja pasif.";  
        }
            
        return message;
    }
    
    public String rulePada(ArrayList<Word> words , int position){
        String message="";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NN")){
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina atau proper noun.";
        }else{
            if(words.get(position - 1).getWordPOS().equals("VB") || words.get(position - 1).getWordPOS().equals("NN")){
                if(words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina atau proper noun.";
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja atau kata benda.";
        }
        
        return message;
    }
    
    public String rulePada2(ArrayList<Word> words , int position){
        String message = "";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NN")){
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina, proper noun atau nomina.";
        }else{
            int i=position-1;
            boolean status = false;
            while(i>0 && !words.get(i).getWordPOS().equals("Z") && !words.get(i).getWordPOS().equals("CC") && !words.get(i).getWordPOS().equals("SC")){
                if(words.get(i).getWordPOS().equals("VB")){
                    status=true;
                    break;
                }
                i--;
            }
            
            if(status==false)
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja.";
            else{
                if(words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina atau proper noun.";
            }
        }
        
        return message;
    }
    
    public String ruleUntuk(ArrayList<Word> words , int position){
        String message="";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("VB") || words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NN")){
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina, kata kerja, kata benda atau proper noun.";
        }else{
            if(words.get(position - 1).getWordPOS().equals("VB") || words.get(position - 1).getWordPOS().equals("NN") || words.get(position - 1).getWordName().toLowerCase().equals("itu")){
                if(words.get(position + 1).getWordPOS().equals("VB") || words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP")){
                }else{
                    if(words.get(position - 1).getWordPOS().equals("VB")){
                        if(!words.get(position + 1).getWordPOS().equals("NN"))
                            message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina, kata kerja, kata benda atau proper noun.";
                    }else   
                        message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina, kata kerja atau proper noun.";
                }
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja atau kata benda.";
        }
        
        return message;
    }
    
    public String ruleUntuk2(ArrayList<Word> words , int position){
        String message = "";
        
        if(position-1 < 0){
            if(words.get(position + 1).getWordPOS().equals("VB") || words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NN")){
            }else
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina, kata kerja, kata benda atau proper noun.";
        }else{
            int i=position-1;
            boolean status = false;
            while(i>0 && !words.get(i).getWordPOS().equals("Z") && !words.get(i).getWordPOS().equals("CC") && !words.get(i).getWordPOS().equals("SC")){
                if(words.get(i).getWordPOS().equals("VB")){
                    status=true;
                    break;
                }
                i--;
            }
            
            if(status==false)
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diawali oleh kata kerja.";
            else{
                if(words.get(position + 1).getWordPOS().equals("VB") || words.get(position + 1).getWordPOS().equals("PRP") || words.get(position + 1).getWordPOS().equals("NNP") || words.get(position + 1).getWordPOS().equals("NN")){
                }else
                    message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus dilengkapi oleh pronomina, kata kerja, kata benda atau proper noun.";
            }
        }
        
        return message;
    }
    
    public String ruleDanAtau(ArrayList<Word> words , int position, AffixService affixService) throws IOException, SQLException{
        String message="";
        boolean isEquals = false;
        if(position+1 < words.size()){
            Word right = new Word();
            right = words.get(position+1);
            //right.setAffix(affixService.getAffix(right.getWordName(), affixService.getLemma(right.getWordName())));
            
            for(int j=position+1;j<words.size();j++){
                if(right.getWordPOS().equals("SYM") || right.getWordPOS().equals("IN") || right.getWordPOS().equals("NEG") || right.getWordPOS().equals("CC") || right.getWordPOS().equals("DT") || right.getWordPOS().equals("MD") || right.getWordPOS().equals("RP") || right.getWordPOS().equals("SC") || right.getWordPOS().equals("UH") || right.getWordPOS().equals("X"))
                    right = words.get(j);
                else
                    break;
            }
            
            int i = position - 1;
            while(isEquals == false && i > -1){
                Word temp = new Word();
                temp = words.get(i);
                //temp.setAffix(affixService.getAffix(temp.getWordName(), affixService.getLemma(temp.getWordName())));

                if(right.getWordPOS().equals(temp.getWordPOS())){
                    //if(affixService.isEquals(temp.getAffix(), right.getAffix())){
                        isEquals = true;
                        break;
                    //}
                }else{
                    if((right.getWordPOS().equals("NNP") && temp.getWordPOS().equals("PRP")) || (right.getWordPOS().equals("PRP") && temp.getWordPOS().equals("NNP"))){
                        isEquals = true;
                        break;
                    }
                }
                    
                if(i>0){
                    if(!words.get(i-1).getWordPOS().equals("Z"))
                        i = i-1;
                    else
                        break;
                }else
                    break;
            }

            if(isEquals==false)
                message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus mengubungkan dua kata dengan jenis yang sama.";
        }else
            message = "Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus mengubungkan dua kata dengan jenis yang sama.";
        
        return message;
    }
}
