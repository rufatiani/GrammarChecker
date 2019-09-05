/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.service;

import grammar.checker.TextProcessing;
import grammar.checker.dao.AffixDAO;
import grammar.checker.dao.LemmaDAO;
import grammar.checker.dao.SentenceDAO;
import grammar.checker.dao.WordDAO;
import grammar.checker.models.Affix;
import grammar.checker.models.Lemma;
import grammar.checker.models.Rule;
import grammar.checker.models.Sentence;
import grammar.checker.models.Word;
import grammar.checker.service.AffixService;
import grammar.checker.service.WordService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ru'fatiani
 */
public class CheckerService {
    
    private static int limit = 10;
    private double valueRightGlobal=-1;
    private AffixService affixService;

    public CheckerService() throws IOException {
        affixService = new AffixService();
    }
    
    public boolean grammarCheckerFile(boolean usingRule, File input, File output) throws FileNotFoundException, IOException, SQLException{
        TextProcessing tp = new TextProcessing();
        ArrayList<String> content = tp.readFile(input.getPath());
        ArrayList<String> messages = new ArrayList<>();
        
        ArrayList<Word> words = new ArrayList<>();
        for(int i=0;i<content.size();i++){
            String temp[] = content.get(i).split("\t");

            if(temp.length > 1 && !temp[0].equals(".")){
                words.add(new Word(temp[0], temp[1]));
            }else{
                for(int j=0; j<words.size(); j++){
                    if(!grammarCheckerSentence(usingRule, words, j).equals(""))     
                        messages.add(grammarCheckerSentence(usingRule, words, j) + "\n");
                } 
                words = new ArrayList<>();
            }
        }
        
        tp.writeFile(output, messages);
        
        return true;
    }
    
    public ArrayList<String> grammarCheckerFile(boolean usingRule, ArrayList<String> content) throws FileNotFoundException, IOException, SQLException{
        ArrayList<String> messages = new ArrayList<>();
        
        ArrayList<Word> words = new ArrayList<>();
        for(int i=0;i<content.size();i++){
            String temp[] = content.get(i).split("\t");

            if(temp.length > 1 && !temp[0].equals(".")){
                words.add(new Word(temp[0], temp[1]));
            }else{
                for(int j=0; j<words.size(); j++){
                    if(!grammarCheckerSentence(usingRule, words, j).equals(""))     
                        messages.add(grammarCheckerSentence(usingRule, words, j) + "\n");
                } 
                words = new ArrayList<>();
            }
        }
        
        return messages;
    }
    
    public String grammarCheckerSentence(boolean usingRule, ArrayList<Word> words, int position) throws IOException, SQLException{
        String message="";
        
        /*if(usingRule){
            if(words.get(position).getWordPOS().equals("IN")){
            message = prepositionCheck(words, position);
            }else if(words.get(position).getWordPOS().equals("CC")){
                message = conjunctionCheck(words, position);
            }else
                message = wordCheck(words, position);
        }else
            message = wordCheck(words, position);*/
        
        /*USING LIMIT
        if(usingRule){  
            if(words.get(position).getWordPOS().equals("IN")){
            message = prepositionCheck(words, position);
            }else if(words.get(position).getWordPOS().equals("CC")){
                message = conjunctionCheck(words, position);
            }else
                message = wordCheckUsingLimit(words, position);
        }else
            message = wordCheckUsingLimit(words, position);*/
        
        //USING QUERY
        if(usingRule){  
            if(words.get(position).getWordPOS().equals("IN")){
            message = prepositionCheck(words, position);
            }else if(words.get(position).getWordPOS().equals("CC")){
                message = conjunctionCheck(words, position);
            }else
                message = wordCheckUsingQuery(words, position);
        }else
            message = wordCheckUsingQuery(words, position);

        return message;
    }
    
    public String wordCheck(ArrayList<Word> words, int position) throws SQLException, IOException{
        WordDAO wordDao = new WordDAO();
        Word tempWord = new Word(), word = new Word();
        SentenceDAO sentenceDao = new SentenceDAO();
        ArrayList<Sentence> sentences = new ArrayList<>();
        AffixDAO affixDao = new AffixDAO();
        Affix affix = new Affix();
        String message = "", reason ="";
        double value=0;
        
        word = words.get(position);
        
        if(wordDao.isWordExist(word.getWordName())){
            word = wordDao.getWordByName(word.getWordName());
            sentences = sentenceDao.getSentencesByWordId(word.getIdWord());
            value = getValueGram(word, words, sentences);
            
            if(value <= 0.2){
                sentences = sentenceDao.getSentencesByWordPos(word.getWordPOS(), word.getAffix().getIdAffix());
                value = getValueGram(word, words, sentences);
            }
        }else{
            word.setAffix(affixService.getAffix(word.getWordName(), affixService.getLemma(word.getWordName())));
            sentences = sentenceDao.getSentencesByWordPos(word.getWordPOS(), affixDao.getAffixId(word.getAffix()));
            value = getValueGram(word, words, sentences);
        }
            
        if(value <= 0.5){
            WordService wordService = new WordService();
            message = "Kata ke-" + (position + 1) + " (" + word.getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " kurang tepat. - " + value ;
        }
        
        return message;
    }
    
    public String wordCheckUsingLimit(ArrayList<Word> words, int position) throws SQLException, IOException{
        WordDAO wordDao = new WordDAO();
        Word tempWord = new Word(), word = new Word();
        SentenceDAO sentenceDao = new SentenceDAO();
        ArrayList<Sentence> sentences;
        AffixDAO affixDao = new AffixDAO();
        Affix affix = new Affix();
        String message = "", reason ="";
        double value=0;
        
        word = words.get(position);
        
        if(wordDao.getWordByName(word.getWordName()).getIdWord()>0){
            word = wordDao.getWordByName(word.getWordName());
            
            int count;
            count = sentenceDao.countSentnecesByWordId(word.getIdWord());
            
            if(count > 0){
                int page = count / limit;
                int mod = count % limit;
            
                if(mod>0) page++;
            
                int start;
            
                for(int i=0; i<page;i++){
                    if(i>0) start = i*limit;
                    else start =0;
                
                    sentences = null;
                    sentences = new ArrayList<>();
                    sentences = sentenceDao.getSentencesByWordId(word.getIdWord(), start, limit);
                    value = getValueGram(word, words, sentences);
                
                    if(value > 0.5) break;
                }
            }       
        }
        
        if(value <= 0.25){
            LemmaDAO lemmaDao = new LemmaDAO();
            if(lemmaDao.isLemma(word.getWordName())> 0){
                word.setAffix(new Affix(1));
            }else
                word.setAffix(affixService.getAffix(word.getWordName(), affixService.getLemma(word.getWordName())));
            
            int id_affix=0;
            if(affixDao.getAffixId(word.getAffix())>0){
                id_affix = affixDao.getAffixId(word.getAffix());
            }else{
                affixDao.insertAffix(affix);
                id_affix = affixDao.getAffixId(affix);
            }
            
            int count;
            count = sentenceDao.countSentnecesByWordPos(word.getWordPOS(), id_affix);
            
            int page = count / limit;
            int mod = count % limit;
            
            if(mod>1) page++;
            
            int start;
            
            for(int i=0; i<page;i++){
                if(i>0) start = i*limit;
                else start =0;
                
                sentences = null;
                sentences = new ArrayList<>();
                sentences = sentenceDao.getSentencesByWordPos(word.getWordPOS(), id_affix, start, limit);
                value = getValueGram(word, words, sentences);
                    
                if(value > 0.5) break;
            }    
        }
            
        if(value <= 0.5){
            WordService wordService = new WordService();
            message = "Kata ke-" + (position + 1) + " (" + word.getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " kurang tepat. - " + value;
        }
        
        return message;
    }
    
    public String wordCheckUsingQuery(ArrayList<Word> words, int position) throws SQLException, IOException{
        String message = "";
        Word word = new Word();
        double value = 0;
        
        word = words.get(position);
        value = getValueGram(word, words);
        
        if(value <= 0.5){
            WordService wordService = new WordService();
            message = "Kata ke-" + (position + 1) + " (" + word.getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " kurang tepat. - " + value;
        }
        
        return message;
    }
    
    public String prepositionCheck(ArrayList<Word> words , int position) throws IOException, SQLException{
        String message = "";
        WordService wordService = new WordService();
        Rule rule = new Rule();
       
        if(position+1 < words.size()){
            if(words.get(position).getWordName().toLowerCase().equals("di") || words.get(position).getWordName().toLowerCase().equals("ke")){
                message = rule.ruleDiKe(words, position);
            }else if(words.get(position).getWordName().toLowerCase().equals("dari")){
                message = rule.ruleDari(words, position);
            }else if(words.get(position).getWordName().toLowerCase().equals("dengan")){
                message = rule.ruleDengan(words, position);
            }else if(words.get(position).getWordName().toLowerCase().equals("oleh")){
                message = rule.ruleOleh(words, position, affixService);
            }else if(words.get(position).getWordName().toLowerCase().equals("pada")){
                message = rule.rulePada(words, position);
            }else if(words.get(position).getWordName().toLowerCase().equals("untuk")){
                message = rule.ruleUntuk(words, position);
            }else
                message ="rule is unavailable";
        }else
            message ="Kata ke-" + (position + 1) + " (" + words.get(position).getWordName() +") " + "pada kalimat " + wordService.printWords(words) + " harus diiringi/ dilengkapi oleh kata lain.";;

        return message;
    }
    
    public String conjunctionCheck(ArrayList<Word> words , int position) throws IOException, SQLException{
        String message = "";
        Rule rule = new Rule();
        
        if(words.get(position).getWordName().toLowerCase().equals("dan") || words.get(position).getWordName().toLowerCase().equals("atau")){
            message = rule.ruleDanAtau(words, position, affixService);
        }else
            message = "rule is unavailable";
        
        return message;
    }
    
    private double valueLeftBigram(Word word, Word left, ArrayList<Sentence> sentences) throws IOException, SQLException{
        WordService wordService = new WordService();
        double count=0;
        ArrayList<Integer> index= new ArrayList<>();
        
        if(valueRightGlobal == -1){
            if(left != null){
                if(!left.getWordPOS().equals("Z")){
                    for(int i=0;i<sentences.size();i++){
                        index = wordService.wordPositionsByName(sentences.get(i).getWords(),word);
            
                        if(index.size()<1){
                            index = wordService.wordPositionsByPOS(sentences.get(i).getWords(),word);
                        }
            
                        for(int l=0;l<index.size();l++){
                            if(index.get(l) > 0){
                                Word tempLeft = new Word();
                                tempLeft = sentences.get(i).getWords().get(index.get(l) - 1);
                
                                if(tempLeft.getWordName().equals(left.getWordName())){
                                    count = 1;
                                    break;
                                }
                                else{
                                    String separateLeft[] = left.getWordPOS().split(",");
                                    String separateTempLeft[] = tempLeft.getWordPOS().split(",");
                        
                                    for(int j=0;j<separateLeft.length;j++){
                                        for(int k=0;k<separateTempLeft.length;k++){
                                            if(separateTempLeft[k].equals(separateLeft[j])){   
                                                left.setAffix(affixService.getAffix(left.getWordName(), affixService.getLemma(left.getWordName())));
                                                tempLeft.setAffix(affixService.getAffix(tempLeft.getWordName(), affixService.getLemma(tempLeft.getWordName())));
                        
                                                if(affixService.isEquals(left.getAffix(), tempLeft.getAffix())){
                                                    count = 1;
                                                    break;
                                                }
                                            }
                                        }
                                        if(count>0) break;
                                    }
                                }    
                            }
                            if(count>0) break;
                        }
                        if(count>0){
                            //System.out.print(wordService.printWords(sentences.get(i).getWords())+ "\n");
                            break;
                        }
                    }
                }else
                    count=1;
            }else
                count=1;
        }else{
            count = valueRightGlobal;
        }
        
        return count;
    }
    
    private double valueLeftBigram(Word word, Word left) throws SQLException, IOException{
        SentenceDAO sentenceDAO = new SentenceDAO();
        WordDAO wordDAO = new WordDAO();
        AffixDAO affixDAO = new AffixDAO();
        
        if(valueRightGlobal==-1){
            if(left != null){
                if(!left.getWordPOS().equals("Z")){
                if(wordDAO.isWordExist(word.getWordName())){
                    if(sentenceDAO.isLeftBigramExist(word.getWordName(), left.getWordName()) == true){
                        return 1;
                    }else {
                        Affix affixLeft = new Affix();
                        affixLeft = affixService.getAffix(left.getWordName(), affixService.getLemma(left.getWordName()));
                        int idLeft = affixDAO.getAffixId(affixLeft);
                
                        if(idLeft < 0){
                            affixDAO.insertAffix(affixLeft);
                            idLeft = affixDAO.getAffixId(affixLeft);
                        }
                
                        if(sentenceDAO.isLeftBigramExist(word.getWordName(), left.getWordPOS(), idLeft) == true){
                            return 1;
                        }
                    }
                }
                    Affix affix = new Affix();
                    affix = affixService.getAffix(word.getWordName(), affixService.getLemma(word.getWordName()));
                    int id = affixDAO.getAffixId(affix);
                
                    if(id < 0){
                        affixDAO.insertAffix(affix);
                        id = affixDAO.getAffixId(affix);
                    }
            
                    if(sentenceDAO.isLeftBigramExist(word.getWordPOS(), id, left.getWordName())){
                        return 1;
                    }else{
                        Affix affixLeft = new Affix();
                        affixLeft = affixService.getAffix(left.getWordName(), affixService.getLemma(left.getWordName()));
                        int idLeft = affixDAO.getAffixId(affixLeft);
                
                        if(idLeft < 0){
                            affixDAO.insertAffix(affixLeft);
                            idLeft = affixDAO.getAffixId(affixLeft);
                        }
                
                        if(sentenceDAO.isLeftBigramExist(word.getWordPOS(), id, left.getWordPOS(), idLeft) == true){
                            return 1;
                        }
                    }
            }else{
                return 1;
            }
            }else{
                return 1;
            }
        }else{
            return valueRightGlobal;
        }

        return 0;
    }
    
    private double valueRightBigram(Word word, Word right, ArrayList<Sentence> sentences) throws IOException, SQLException{
        WordService wordService = new WordService();
        double result=-1, count=0;
        ArrayList<Integer> index= new ArrayList<>();
        
        if(right != null){
            if(!right.getWordPOS().equals("Z")){
            for(int i=0;i<sentences.size();i++){
                index = wordService.wordPositionsByName(sentences.get(i).getWords(),word);
            
                if(index.size()<1){    
                    index = wordService.wordPositionsByPOS(sentences.get(i).getWords(),word);
                }
            
                for(int l=0;l<index.size();l++){
                    if (index.get(l) >= 0 && index.get(l) < sentences.get(i).getWords().size() - 2){
                        Word tempRight = new Word();
                        tempRight = sentences.get(i).getWords().get(index.get(l) + 1);
                
                        if(tempRight.getWordName().equals(right.getWordName())){
                            count=1;
                            break;
                        }
                        else{
                            String separateRight[] = right.getWordPOS().split(",");
                            String separateTempRight[] = tempRight.getWordPOS().split(",");
                        
                            for(int j=0;j<separateRight.length;j++){
                                for(int k=0;k<separateTempRight.length;k++){
                                    if(separateTempRight[k].equals(separateRight[j])){
                                        right.setAffix(affixService.getAffix(right.getWordName(), affixService.getLemma(right.getWordName())));
                                        tempRight.setAffix(affixService.getAffix(tempRight.getWordName(), affixService.getLemma(tempRight.getWordName())));
                        
                                        if(affixService.isEquals(right.getAffix(), tempRight.getAffix())){
                                            count=1;
                                            break;
                                        }
                                    }
                                }
                                
                                if(count>0) break;
                            }
                        }             
                    }
                    if(count>0) break;
                }
                if(count>0) {
                    //System.out.print(wordService.printWords(sentences.get(i).getWords()) + "\n");
                    break;
                }
            }
            }else
                count=1;
        }else
            count=1;
        
        return count;
    }
    
    private double valueRightBigram(Word word, Word right) throws SQLException, IOException{
        SentenceDAO sentenceDAO = new SentenceDAO();
        WordDAO wordDAO = new WordDAO();
        AffixDAO affixDAO = new AffixDAO();
        
        if(right != null){
            if(!right.getWordPOS().equals("Z")){
            if(wordDAO.isWordExist(word.getWordName())){
                if(sentenceDAO.isLeftBigramExist(word.getWordName(), right.getWordName()) == true){
                    valueRightGlobal = 1;
                    return 1;
                }else {
                    Affix affixright = new Affix();
                    affixright = affixService.getAffix(right.getWordName(), affixService.getLemma(right.getWordName()));
                    int idright = affixDAO.getAffixId(affixright);
                
                    if(idright < 0){
                        affixDAO.insertAffix(affixright);
                        idright = affixDAO.getAffixId(affixright);
                    }
                
                    if(sentenceDAO.isLeftBigramExist(word.getWordName(), right.getWordPOS(), idright) == true){
                        valueRightGlobal = 1;
                        return 1;
                    }
                }                
            }
                Affix affix = new Affix();
                affix = affixService.getAffix(word.getWordName(), affixService.getLemma(word.getWordName()));
                int id = affixDAO.getAffixId(affix);
                
                if(id < 0){
                    affixDAO.insertAffix(affix);
                    id = affixDAO.getAffixId(affix);
                }
            
                if(sentenceDAO.isLeftBigramExist(word.getWordPOS(), id, right.getWordName())){
                    valueRightGlobal = 1;
                    return 1;
                }else{
                    Affix affixright = new Affix();
                    affixright = affixService.getAffix(right.getWordName(), affixService.getLemma(right.getWordName()));
                    int idright = affixDAO.getAffixId(affixright);
                
                    if(idright < 0){
                        affixDAO.insertAffix(affixright);
                        idright = affixDAO.getAffixId(affixright);
                    }
                
                    if(sentenceDAO.isLeftBigramExist(word.getWordPOS(), id, right.getWordPOS(), idright) == true){
                        valueRightGlobal = 1;
                        return 1;
                    }
                }
        }else{
            valueRightGlobal = 1;
            return 1;
        }
        }else{
            //if(word.getWordPOS().equals("IN") || word.getWordPOS().equals("CC")){
            //    valueRightGlobal = 0;
            //    return 0;
            //}else{
                valueRightGlobal = 1;
                return 1;
            //}
        }
        
        valueRightGlobal = 0;
        return 0;
    }
    
    private double valueTrigram(Word word, Word left, Word right, ArrayList<Sentence> sentences, double valueLeft, double valueRight) throws IOException, SQLException{
        WordService wordService = new WordService();
        double count=0;
        ArrayList<Integer> index= new ArrayList<>();
        
        if(valueLeft > 0 && valueRight > 0){
            Word tempLeft = new Word(), tempRight = new Word();
            if(left != null){
                if(!left.getWordPOS().equals("Z")){
                if(right != null){
                    if(!right.getWordPOS().equals("Z")){
                    for(int i=0;i<sentences.size();i++){
                        index = wordService.wordPositionsByName(sentences.get(i).getWords(),word);
            
                        if(index.size() < 1){    
                            index = wordService.wordPositionsByPOS(sentences.get(i).getWords(),word);
                        }
                        
                        for(int l=0;l<index.size();l++){
                            if(index.get(l) > 0 && index.get(l) < sentences.get(i).getWords().size() - 1){
                            tempLeft = sentences.get(i).getWords().get(index.get(l) - 1);
                            tempRight = sentences.get(i).getWords().get(index.get(l) + 1);
                
                            if(tempLeft.getWordName().equals(left.getWordName()) && tempRight.getWordName().equals(right.getWordName())){
                                count=1;
                                break;
                            }
                            else{
                                String separateRight[] = right.getWordPOS().split(",");
                                String separateTempRight[] = tempRight.getWordPOS().split(",");
                                boolean statRight=false;
                        
                                for(int j=0;j<separateRight.length;j++){
                                    for(int k=0;k<separateTempRight.length;k++){
                                        if(separateTempRight[k].equals(separateRight[j])){
                                            statRight=true;
                                            break;
                                        }
                                    }
                                    if(statRight==true) break;
                                }
                                
                                String separateLeft[] = left.getWordPOS().split(",");
                                String separateTempLeft[] = tempLeft.getWordPOS().split(",");
                                boolean statLeft=false;
                                
                                for(int j=0;j<separateLeft.length;j++){
                                    for(int k=0;k<separateTempLeft.length;k++){
                                        if(separateTempLeft[k].equals(separateLeft[j])){
                                            statLeft=true;
                                            break;
                                        }
                                    }
                                    if(statLeft==true) break;
                                }
                                        
                                if(statLeft==true && statRight==true){
                                    left.setAffix(affixService.getAffix(left.getWordName(), affixService.getLemma(left.getWordName())));
                                    tempLeft.setAffix(affixService.getAffix(tempLeft.getWordName(), affixService.getLemma(tempLeft.getWordName())));
                                    right.setAffix(affixService.getAffix(right.getWordName(), affixService.getLemma(right.getWordName())));
                                    tempRight.setAffix(affixService.getAffix(tempRight.getWordName(), affixService.getLemma(tempRight.getWordName())));
                                        
                                    if(affixService.isEquals(left.getAffix(), tempLeft.getAffix()) && affixService.isEquals(right.getAffix(), tempRight.getAffix())){
                                        count=1;
                                        break;
                                    }
                                }
                            }          
                        }
                        if(count>0) {
                            //System.out.print(wordService.printWords(sentences.get(i).getWords()) + "\n");
                            break;
                        }
                        }
            
                        
                    }
                    }else
                        count=1;
                }else{
                    count=1;
                }
                }else
                    count=1;
            } else
                count=1;
        }
        
        return count;
    }
    
    private double valueTrigram(Word word, Word left, Word right, double valueLeft, double valueRight) throws SQLException, IOException{
        SentenceDAO sentenceDAO = new SentenceDAO();
        WordDAO wordDAO = new WordDAO();
        AffixDAO affixDAO = new AffixDAO();
        
        if(valueLeft>0 && valueRight>0){
            if(left != null){
                if(!left.getWordPOS().equals("Z")){
                if(right != null){
                    if(!right.getWordPOS().equals("Z")){
                    if(wordDAO.isWordExist(word.getWordName())){
                        if(sentenceDAO.isTrigramExist(word.getWordName(), left.getWordName(), right.getWordName())){
                            return 1;
                        }else{
                            Affix affixLeft = new Affix();
                            affixLeft = affixService.getAffix(left.getWordName(), affixService.getLemma(left.getWordName()));
                            int idLeft = affixDAO.getAffixId(affixLeft);
                
                            if(idLeft<0){
                                affixDAO.insertAffix(affixLeft);
                                idLeft = affixDAO.getAffixId(affixLeft);
                            }
                
                            if(sentenceDAO.isTrigramExist(word.getWordName(), left.getWordPOS(), idLeft, right.getWordName())){
                                return 1;
                            }else{
                                Affix affixRight = new Affix();
                                affixRight = affixService.getAffix(right.getWordName(), affixService.getLemma(right.getWordName()));
                                int idRight = affixDAO.getAffixId(affixRight);
                
                                if(idRight<0){
                                    affixDAO.insertAffix(affixRight);
                                    idRight = affixDAO.getAffixId(affixRight);
                                }
                    
                                if(sentenceDAO.isTrigramExist(word.getWordName(), left.getWordName(), right.getWordPOS(), idRight)){
                                    return 1;
                                }else{
                                    if(sentenceDAO.isTrigramExist(word.getWordName(), left.getWordPOS(), idLeft, right.getWordPOS(), idRight)){
                                        return 1;
                                    }
                                }
                            }
                        }
                        
                    }
                        Affix affix = new Affix();
                        affix = affixService.getAffix(word.getWordName(), affixService.getLemma(word.getWordName()));
                        int id = affixDAO.getAffixId(affix);
            
                        if(id<0){
                            affixDAO.insertAffix(affix);
                            id = affixDAO.getAffixId(affix);
                        }
            
                        if(sentenceDAO.isTrigramExist(word.getWordPOS(), id, left.getWordName(), right.getWordName())){
                            return 1;
                        }else{
                            Affix affixLeft = new Affix();
                            affixLeft = affixService.getAffix(left.getWordName(), affixService.getLemma(left.getWordName()));
                            int idLeft = affixDAO.getAffixId(affixLeft);
                
                            if(idLeft<0){
                                affixDAO.insertAffix(affixLeft);
                                idLeft = affixDAO.getAffixId(affixLeft);
                            }
                
                            if(sentenceDAO.isTrigramExist(word.getWordPOS(), id, left.getWordPOS(), idLeft, right.getWordName())){
                                return 1;
                            }else{
                                Affix affixRight = new Affix();
                                affixRight = affixService.getAffix(right.getWordName(), affixService.getLemma(right.getWordName()));
                                int idRight = affixDAO.getAffixId(affixRight);
                
                                if(idRight<0){
                                    affixDAO.insertAffix(affixRight);
                                    idRight = affixDAO.getAffixId(affixRight);
                                }
                    
                                if(sentenceDAO.isTrigramExist(word.getWordPOS(), id, left.getWordName(), right.getWordPOS(), idRight)){
                                    return 1;
                                }else{
                                    if(sentenceDAO.isTrigramExist(word.getWordPOS(), id, left.getWordPOS(), idLeft, right.getWordPOS(), idRight)){
                                        return 1;
                                    }
                                }
                            }
                        }
                }else{
                    return 1;
                }
                }else{
                    return 1;
                }
            }else{
                return 1;
            }
            }else{
                return 1;
            }
        }
 
        return 0;
    }
    
    private double valueTotal(double leftBigram, double rightBigram, double trigram){
        
        //if(leftBigram==0.5 && rightBigram==1) leftBigram=1;
        //else leftBigram=0;
        
        return ((0.25 * leftBigram) + (0.25 * rightBigram) + (0.5 * trigram));
    } 
    
    private double getValueGram(Word word, ArrayList<Word> words, ArrayList<Sentence> sentences) throws IOException, SQLException{
        Word left = new Word(), right = new Word();
        WordService wordService = new WordService();
            
        if(wordService.wordPositionByName(words, word) - 1 >= 0)
            left = words.get(wordService.wordPositionByName(words, word) - 1);
        else
            left = null;
            
        if(wordService.wordPositionByName(words, word) + 1 < words.size())
            right = words.get(wordService.wordPositionByName(words, word) + 1);
        else
            right = null;
            
        double valueLeft = valueLeftBigram(word, left, sentences);
        double valueRight = valueRightBigram(word, right, sentences);
        valueRightGlobal = valueRight;
        double valueTrigram = valueTrigram(word, left, right, sentences, valueLeft, valueRight);
            
        return valueTotal(valueLeft, valueRight, valueTrigram);
    }
    
    private double getValueGram(Word word, ArrayList<Word> words) throws SQLException, IOException{
        Word left = new Word(), right = new Word();
        WordService wordService = new WordService();
            
        if(wordService.wordPositionByName(words, word) - 1 >= 0)
            left = words.get(wordService.wordPositionByName(words, word) - 1);
        else
            left = null;
            
        if(wordService.wordPositionByName(words, word) + 1 < words.size())
            right = words.get(wordService.wordPositionByName(words, word) + 1);
        else
            right = null;
            
        double valueLeft = valueLeftBigram(word, left);
        double valueRight = valueRightBigram(word, right);
        valueRightGlobal = valueRight;
        double valueTrigram = valueTrigram(word, left, right, valueLeft, valueRight);
            
        return valueTotal(valueLeft, valueRight, valueTrigram);
    }
}
