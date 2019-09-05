/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.checker.dao;

import grammar.checker.Configuration;
import grammar.checker.ConnectionManager;
import grammar.checker.models.Affix;
import grammar.checker.models.Lemma;
import grammar.checker.models.Sentence;
import grammar.checker.models.Word;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Ru'fatiani
 */
public class SentenceDAO {
    
    private Connection conn;

    public SentenceDAO() {
        conn = ConnectionManager.getConn();
    }
    
    public int insertSentence(int id_sentence) throws SQLException{
        String sql = "INSERT INTO "+ Configuration.TAB_SENTENCE +" (" + Configuration.KEY_SENTENCE + ") VALUES (?)";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        statement.setInt(1,id_sentence);
        return statement.executeUpdate();
    }
    
    public int getLastSentenceId() throws SQLException{
        String sql = "SELECT last_insert_id() as last_id from " + Configuration.TAB_SENTENCE;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);
        
        int id=-1;
        if(rs.next()){
            id = rs.getInt("last_id");
        }
        
        return id;
    }
    
    public ArrayList<Word> getSentenceById(int id_sentence) throws SQLException{
        String sql = "SELECT * FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE id_sentence = " + id_sentence + " ORDER BY " + Configuration.TAG_NUMBER + " ASC";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<Word> words = new ArrayList<>();
        WordDAO wordDAO = new WordDAO();
        while(rs.next()){ 
            words.add(wordDAO.getWordById(rs.getInt(Configuration.KEY_WORD)));
        }
        
        return words;
    }
    
    public int countSentnecesByWordId(int id_word) throws SQLException{
        String sql = "SELECT count(*) as count FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE id_word = " + id_word;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count=-1;
        while(rs.next()){   
            count = rs.getInt("count");
        }
        return count;
    }
    
    public ArrayList<Sentence> getSentencesByWordId(int id_word) throws SQLException{
        String sql = "SELECT DISTINCT "+ Configuration.KEY_SENTENCE +" FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE " + Configuration.KEY_WORD + " = " + id_word;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<Sentence> sentences = new ArrayList<>();
        while(rs.next()){ 
            
            Sentence sentence = new Sentence();
            sentence.setWords(getSentenceById(rs.getInt(Configuration.KEY_SENTENCE)));
            sentences.add(sentence);
        }
        return sentences;
    }
    
    public ArrayList<Sentence> getSentencesByWordId(int id_word, int start, int limit) throws SQLException{
        String sql = "SELECT DISTINCT "+ Configuration.KEY_SENTENCE +" FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE " + Configuration.KEY_WORD + " = " + id_word + " LIMIT " + start + "," + limit;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<Sentence> sentences = new ArrayList<>();
        while(rs.next()){ 
            
            Sentence sentence = new Sentence();
            sentence.setWords(getSentenceById(rs.getInt(Configuration.KEY_SENTENCE)));
            sentences.add(sentence);
        }
        return sentences;
    }
    
    public ArrayList<Sentence> getSentencesByWordId(int id_word, int limit) throws SQLException{
        String sql = "SELECT a."+ Configuration.KEY_WORD +", b."+ Configuration.TAG_WORD_NAME +" FROM "+ Configuration.TAB_SENTENCE_WORD +" a, "+ Configuration.TAB_WORD +" b WHERE a."+ Configuration.KEY_SENTENCE +" IN (SELECT DISTINCT "+ Configuration.KEY_SENTENCE +" FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE "+ Configuration.KEY_WORD +" = "+ id_word +") AND a."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" ORDER by a."+ Configuration.KEY_SENTENCE +", a."+ Configuration.TAG_NUMBER ;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<Sentence> sentences = new ArrayList<>();
        ArrayList<Sentence> words = null;
        while(rs.next()){ 
            Word word = new Word();
            word.setIdWord(id_word);
            
            
            Sentence sentence = new Sentence();
            sentence.setWords(getSentenceById(rs.getInt(Configuration.KEY_SENTENCE)));
            sentences.add(sentence);
        }
        return sentences;
    }
    
    public int countSentnecesByWordPos(String pos, int id_affix) throws SQLException{
        String temp[] = pos.split(",");
        
        String sql ="SELECT count(*) as count FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE "+ Configuration.KEY_WORD +" IN (SELECT "+ Configuration.KEY_WORD +" FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.KEY_AFFIX +" = "+ id_affix + " AND " +  Configuration.TAG_WORD_POS +" LIKE '"+ pos  +"' ";
        if(temp.length>1){
            sql = sql + "OR " + Configuration.TAG_WORD_POS + " LIKE '" + temp[1] + "')";
        }else
            sql = sql + ")";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count=-1;
        while(rs.next()){   
            count = rs.getInt("count");
        }
        return count;
    }
    
    public boolean isLeftBigramExist(String word, String left) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count "
                + "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b "
                + "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" "
                + "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" "
                + "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" "
                + "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" "
                + "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) "
                + "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' "
                + "AND b."+ Configuration.TAG_WORD_NAME +" LIKE '"+ left +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isLeftBigramExist(String word, String posLeft, int affixLeft) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' " +
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posLeft +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = " + affixLeft;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isLeftBigramExist(String posWord, int affix, String left) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND a."+ Configuration.TAG_WORD_POS +" LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " " +
                    "AND b."+ Configuration.TAG_WORD_NAME +" LIKE '"+ left +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
        public boolean isLeftBigramExist(String posWord, int affix, String posLeft, int affixLeft) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND a."+ Configuration.TAG_WORD_POS +" LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " " +
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posLeft +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = " + affixLeft;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isRightBigramExist(String word, String right) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count "
                + "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.KEY_WORD +" a, "+ Configuration.KEY_WORD +" b "
                + "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" "
                + "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" "
                + "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" "
                + "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" "
                + "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) "
                + "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' "
                + "AND b."+ Configuration.TAG_WORD_NAME +" LIKE '"+ right +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isRightBigramExist(String word, String posRight, int affixRight) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' " +
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posRight +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = " + affixRight;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isRightBigramExist(String posWord, int affix, String right) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_POS +" LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " " +
                    "AND b."+ Configuration.TAG_WORD_NAME +" LIKE '"+ right +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isRightBigramExist(String posWord, int affix, String posRight, int affixRight) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD+ " b " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_POS +" LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " " +
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posRight +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = " + affixRight;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String word, String left, String right) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") as count "
                + "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c "
                + "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" "
                + "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" "
                + "AND ss."+ Configuration.KEY_SENTENCE +"= sw."+ Configuration.KEY_SENTENCE +" "
                + "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" "
                + "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" "
                + "AND sw."+ Configuration.KEY_WORD +" = sw."+ Configuration.KEY_WORD +" "
                + "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) "
                + "AND sw."+ Configuration.TAG_NUMBER +"=(s."+ Configuration.TAG_NUMBER +"+1) "
                + "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' "
                + "AND b."+ Configuration.TAG_WORD_NAME+ " LIKE '"+ left +"' "
                + "AND c."+ Configuration.TAG_WORD_NAME +" LIKE '"+ right +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String word, String posLeft, int affixLeft, String right) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = sw."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND sw."+ Configuration.KEY_WORD +" = c."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND sw."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' " +
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posLeft +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = " + affixLeft + " " +
                    "AND c."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String word, String left, String posRight, int affixRight) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = sw."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND sw."+ Configuration.KEY_WORD +" = c."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND sw."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' " +
                    "AND b."+ Configuration.TAG_WORD_NAME +" LIKE '" + left +"' " +
                    "AND c."+ Configuration.TAG_WORD_POS +" LIKE '"+ posRight +"' " +
                    "AND c."+ Configuration.KEY_AFFIX +" = " + affixRight;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String word, String posLeft, int affixLeft, String posRight, int affixRight) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = sw."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND sw."+ Configuration.KEY_WORD +" = c."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND sw."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_NAME +" LIKE '"+ word +"' " +
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posLeft +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = " + affixLeft + " "+
                    "AND c."+ Configuration.TAG_WORD_POS +" LIKE '"+ posRight +"' " +
                    "AND c."+ Configuration.KEY_AFFIX +" = " + affixRight;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String posWord, int affix, String left, String right) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = sw."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND sw."+ Configuration.KEY_WORD +" = c."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND sw."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_POS +" LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " "+
                    "AND b."+ Configuration.TAG_WORD_NAME +" LIKE '"+ left +"' " +
                    "AND c."+ Configuration.TAG_WORD_NAME +" LIKE '"+ right +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String posWord, int affix, String posLeft, int affixLeft, String right) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s , "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = sw."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND sw."+ Configuration.KEY_WORD +" = c."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND sw."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_POS+ " LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " "+
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posLeft +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = " + affixLeft + " "+
                    "AND c."+ Configuration.TAG_WORD_NAME +" LIKE '"+ right +"'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String posWord, int affix, String left, String posRight, int affixRight) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count " +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = sw."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND sw."+ Configuration.KEY_WORD +" = c."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND sw."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_POS +" LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " "+
                    "AND b."+ Configuration.TAG_WORD_NAME +" LIKE '"+ left +"' " +
                    "AND c."+ Configuration.TAG_WORD_POS +" LIKE '"+ posRight +"' " +
                    "AND c."+ Configuration.KEY_AFFIX +" = " + affixRight;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isTrigramExist(String posWord, int affix, String posLeft, int affixLeft, String posRight, int affixRight) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT COUNT(ss."+ Configuration.KEY_SENTENCE +") AS count \n" +
                    "FROM "+ Configuration.TAB_SENTENCE +" ss, "+ Configuration.TAB_SENTENCE_WORD +" s, "+ Configuration.TAB_SENTENCE_WORD +" w, "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" a, "+ Configuration.TAB_WORD +" b, "+ Configuration.TAB_WORD +" c " +
                    "WHERE ss."+ Configuration.KEY_SENTENCE +" = s."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = w."+ Configuration.KEY_SENTENCE +" " +
                    "AND ss."+ Configuration.KEY_SENTENCE +" = sw."+ Configuration.KEY_SENTENCE +" " +
                    "AND s."+ Configuration.KEY_WORD +" = a."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.KEY_WORD +" = b."+ Configuration.KEY_WORD +" " +
                    "AND sw."+ Configuration.KEY_WORD +" = c."+ Configuration.KEY_WORD +" " +
                    "AND w."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"-1) " +
                    "AND sw."+ Configuration.TAG_NUMBER +" = (s."+ Configuration.TAG_NUMBER +"+1) " +
                    "AND a."+ Configuration.TAG_WORD_POS +" LIKE '"+ posWord +"' " +
                    "AND a."+ Configuration.KEY_AFFIX +" = " + affix + " "+
                    "AND b."+ Configuration.TAG_WORD_POS +" LIKE '"+ posLeft +"' " +
                    "AND b."+ Configuration.KEY_AFFIX +" = "+ affixLeft + " "+
                    "AND c."+ Configuration.TAG_WORD_POS +" LIKE '"+ posRight +"' " +
                    "AND c."+ Configuration.KEY_AFFIX +" = " + affixRight;
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public ArrayList<Sentence> getSentencesByWordPos(String pos, int id_affix) throws SQLException{
        
        String temp[] = pos.split(",");
        
        String sql ="SELECT DISTINCT "+ Configuration.KEY_SENTENCE +" FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE id_word IN (SELECT id_word FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.KEY_AFFIX +"="+ id_affix + " AND " +  Configuration.TAG_WORD_POS +" LIKE '"+ pos  +"' ";
        if(temp.length>1){
            sql = sql + "OR " + Configuration.TAG_WORD_POS + " LIKE '" + temp[1] + "')";
        }else
            sql = sql + ")";
        //String sql ="SELECT * FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE "+ Configuration.KEY_WORD +" IN (SELECT "+ Configuration.KEY_WORD +" FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.KEY_AFFIX +"="+ id_affix + " AND " +  Configuration.TAG_WORD_POS +" LIKE '"+ pos  +"')";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<Sentence> sentences = new ArrayList<>();
        while(rs.next()){ 
            
            Sentence sentence = new Sentence();
            sentence.setWords(getSentenceById(rs.getInt(Configuration.KEY_SENTENCE)));
            sentences.add(sentence);
        }
        return sentences;
    }
    
    public ArrayList<Sentence> getSentencesByWordPos(String pos, int id_affix, int start, int limit) throws SQLException{
        
        String temp[] = pos.split(",");
        
        String sql ="SELECT DISTINCT "+ Configuration.KEY_SENTENCE +" FROM "+ Configuration.TAB_SENTENCE_WORD +" WHERE "+ Configuration.KEY_WORD +" IN (SELECT "+ Configuration.KEY_WORD +" FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.KEY_AFFIX +"="+ id_affix + " AND " +  Configuration.TAG_WORD_POS +" LIKE '"+ pos  +"' ";
        if(temp.length>1){
            sql = sql + "OR " + Configuration.TAG_WORD_POS + " LIKE '" + temp[1] + "')";
        }else
            sql = sql + ")";
        
        sql = sql + " LIMIT " + start + "," + limit;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<Sentence> sentences = new ArrayList<>();
        while(rs.next()){ 
            
            Sentence sentence = new Sentence();
            sentence.setWords(getSentenceById(rs.getInt(Configuration.KEY_SENTENCE)));
            sentences.add(sentence);
        }
        return sentences;
    }
    
    public boolean isSentenceExist(Word word) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT count(*) as count  FROM " + Configuration.TAB_SENTENCE_WORD + " WHERE " + Configuration.KEY_WORD + " = " + word.getIdWord();
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int count =-1;
        while(rs.next()){
            count = rs.getInt("count");
        }
        
        if(count>0) isExist = true;
        
        return isExist;
    }
    
    public boolean isSentenceExist(ArrayList<Word> words) throws SQLException{
        boolean isExist = false;
        String sql = "SELECT * FROM " + Configuration.TAB_SENTENCE_WORD + " WHERE " + Configuration.KEY_WORD + " = " + words.get(0).getIdWord() + " AND " + Configuration.TAG_NUMBER + " = " + 0;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<String> sentences = new ArrayList<>();
        while(rs.next()){
            Sentence sentence = new Sentence();
            sentence.setWords(getSentenceById(Integer.valueOf(rs.getString(Configuration.KEY_SENTENCE))));
            
            if(sentence.getWords().size()==words.size()){
                
                for(int j=0;j<sentence.getWords().size();j++){
                    if(!sentence.getWords().get(j).getWordName().equals(words.get(j).getWordName())){
                        isExist = false;
                        break;
                    }else isExist = true;
                }
                
                return true;
            }else{
                isExist = false;
            }
        }
        
        for(int i=0;i<sentences.size();i++){
            Sentence sentence = new Sentence();
            sentence.setWords(getSentenceById(Integer.valueOf(sentences.get(i))));
            
            if(sentence.getWords().size()==words.size()){
                
                for(int j=0;j<sentence.getWords().size();j++){
                    if(!sentence.getWords().get(j).getWordName().equals(words.get(j).getWordName())){
                        isExist = false;
                        break;
                    }else isExist = true;
                }
                
                return true;
            }else{
                isExist = false;
            }
        }
        
       return isExist;
    }
    
    public boolean isSentenceExist2(ArrayList<Word> words) throws SQLException{
        String sql;
        PreparedStatement statement; 
        ResultSet rs;
        
        for(int i=0;i<words.size();i++){
            sql = "SELECT sw."+ Configuration.KEY_SENTENCE +" "
                    + "FROM "+ Configuration.TAB_SENTENCE_WORD +" sw, "+ Configuration.TAB_WORD +" w "
                    + "WHERE sw."+ Configuration.KEY_WORD +" = w."+ Configuration.KEY_WORD +" "
                    + "AND w."+ Configuration.TAG_WORD_NAME +" LIKE '";
                    
            if(words.get(i).getWordName().contains("'")){
                sql = sql + words.get(i).getWordName().replace("'", "\\'") + "' ";
            }else
                sql = sql + words.get(i).getWordName() + "' ";
                    
            sql = sql + "AND sw."+ Configuration.TAG_NUMBER +" = " + i;
 
            statement = conn.prepareStatement(sql); 
            rs = statement.executeQuery(sql);

            int id=-1;
            if(rs.next()){
                id = rs.getInt(Configuration.KEY_SENTENCE);
            }
            
            if(id==-1){
                return false;
            }
        }
        return true;
    }
    
}
