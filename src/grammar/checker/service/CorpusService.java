/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.service;

import grammar.checker.TextProcessing;
import grammar.checker.Configuration;
import grammar.checker.ConnectionManager;
import grammar.checker.dao.AffixDAO;
import grammar.checker.dao.LemmaDAO;
import grammar.checker.dao.SentenceDAO;
import grammar.checker.dao.WordDAO;
import grammar.checker.models.Affix;
import grammar.checker.models.Lemma;
import grammar.checker.models.Sentence;
import grammar.checker.models.Word;
import grammar.checker.service.AffixService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class CorpusService {
    
    private AffixService affixService;

    public CorpusService() throws IOException {
        affixService = new AffixService();
    }
    
    public void improveCorpus(File file) throws FileNotFoundException, SQLException, IOException{
        TextProcessing tp = new TextProcessing();
        ArrayList<String> content = tp.readFile(file.getPath());
        
        ArrayList<Word> words = new ArrayList<>();
        for(int i=0;i<content.size();i++){
            String temp[] = content.get(i).split("\t"); //temp[0] = kata, temp[1] = pos
            
            WordDAO wordDao = new WordDAO();
            if(temp.length>1 && !temp[0].equals(".")){        
                if(!wordDao.isWordExist(temp[0])){
                    LemmaDAO lemmaDao = new LemmaDAO();
                 
                    if(lemmaDao.isLemma(temp[0]) > 0){
                        Lemma lemma = new Lemma();
                        lemma = lemmaDao.getLemmaByName(temp[0]);
                        Word word = new Word(0, temp[0], temp[1], new Affix(1), lemma);
                        wordDao.insertWord(word);
                        words.add(word);
                    }else{
                        Lemma lemma = new Lemma();
                        
                        String lemm = affixService.getLemma(temp[0]);
                        lemma = lemmaDao.getLemmaByName(lemm);
                        
                        if(lemma.getIdLemma()==0){
                            lemmaDao.insertLemma(new Lemma(0, lemm, temp[1]));
                            lemma = lemmaDao.getLemmaByName(lemm);
                        }

                        Affix affix = new Affix();
                        
                        affix = affixService.getAffix(temp[0], lemm);
                        AffixDAO affixDao = new AffixDAO();
                        
                        int id_affix = affixDao.getAffixId(affix);
                        if (id_affix == -1){
                            affixDao.insertAffix(affix);
                            id_affix = affixDao.getAffixId(affix);
                        }

                        if(id_affix== -1 || id_affix>176){
                            int ia = id_affix;
                        }
                        
                        Word word = new Word(0, temp[0], temp[1], new Affix(id_affix), lemma); 
                        wordDao.insertWord(word);
                        words.add(word);
                    }
                    
                    //words.add(new Word(wordDao.getWordId(new Word(temp[0]))));
                }else{
                    Word word = new Word();
                    int id = wordDao.getWordId(new Word(temp[0]));
                    word = wordDao.getWordById(id);
                    words.add(word);
                }
            }else{
                
                if(words.size()>0){
                    SentenceDAO sentenceDao = new SentenceDAO();
                
                    if(!sentenceDao.isSentenceExist2(words)){
                        sentenceDao.insertSentence(0);
                        
                        int id_sentence = sentenceDao.getLastSentenceId();
                        for(int j=0; j<words.size();j++){
                            wordDao.insertOrderWord(id_sentence, wordDao.getWordId(words.get(j)), j);
                        }
                    }
                }
                words = new ArrayList<>();
            }
        }
    }
    
    public void improveCorpus (ArrayList<String> content) throws SQLException, IOException{
        WordDAO wordDao = new WordDAO();
        AffixDAO affixDao = new AffixDAO();
        SentenceDAO sentenceDao = new SentenceDAO();
        ArrayList<Word> words = new ArrayList<>();
        
        for(int i=0;i<content.size();i++){
            String temp[] = content.get(i).split("\t");
            
            if(temp.length>1 && !temp[0].equals(".")){        
                if(wordDao.isWordExist(temp[0])){
                    LemmaDAO lemmaDao = new LemmaDAO();
                 
                    if(lemmaDao.isLemma(temp[0]) > 0){
                        Lemma lemma = new Lemma();
                        lemma = lemmaDao.getLemmaByName(temp[0]);
                        Word word = new Word(0, temp[0], temp[1], new Affix(1), lemma);
                        wordDao.insertWord(word);
                        words.add(word);
                    }else{
                        Lemma lemma = new Lemma();
                        
                        String lemm = affixService.getLemma(temp[0]);
                        lemma = lemmaDao.getLemmaByName(lemm);
                        
                        if(lemma.getIdLemma()==0){
                            lemmaDao.insertLemma(new Lemma(0, lemm, temp[1]));
                            lemma = lemmaDao.getLemmaByName(lemm);
                        }

                        Affix affix = new Affix();
                        affix = affixService.getAffix(temp[0], lemm);
                        
                        int id_affix = affixDao.getAffixId(affix);
                        if (id_affix == -1){
                            affixDao.insertAffix(affix);
                            id_affix = affixDao.getAffixId(affix);
                        }

                        Word word = new Word(0, temp[0], temp[1], new Affix(id_affix), lemma); 
                        wordDao.insertWord(word);
                        words.add(word);
                    }
                    
                    //words.add(new Word(wordDao.getWordId(new Word(temp[0]))));
                }else{
                    Word word = new Word();
                    int id = wordDao.getWordId(new Word(temp[0]));
                    word = wordDao.getWordById(id);
                    words.add(word);
                }
            }else{
                
                if(words.size()>0){
                    if(!sentenceDao.isSentenceExist(words)){
                        sentenceDao.insertSentence(0);
                        
                        int id_sentence = sentenceDao.getLastSentenceId();
                        for(int j=0; j<words.size();j++){
                            wordDao.insertOrderWord(id_sentence, wordDao.getWordId(words.get(j)), j);
                        }
                    }
                }
                words = new ArrayList<>();
            }
        }
    }
    
 }
