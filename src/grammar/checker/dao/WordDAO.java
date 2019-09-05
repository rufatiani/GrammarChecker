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
import grammar.checker.models.Word;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ru'fatiani
 */
public class WordDAO {
    
    private Connection conn;

    public WordDAO() {
        conn = ConnectionManager.getConn();
    }
    
    public Word getWordById(int id_word) throws SQLException{
        String sql = "SELECT * FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.KEY_WORD + " = " + id_word;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);
        
        Word w = new Word();
        if(rs.next()){
            w.setIdWord(rs.getInt(Configuration.KEY_WORD));
            w.setWordName(rs.getString(Configuration.TAG_WORD_NAME));
            w.setWordPOS(rs.getString(Configuration.TAG_WORD_POS));
            w.setLemma(new Lemma(rs.getInt(Configuration.KEY_LEMMA)));
            w.setAffix(new Affix(rs.getInt(Configuration.KEY_AFFIX)));
        }
        
        return w;
    }
    
    public int getWordId(Word word) throws SQLException{
        String sql = "SELECT " + Configuration.KEY_WORD + " FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.TAG_WORD_NAME + " LIKE '" ;
            if(word.getWordName().contains("'")){
                sql = sql + word.getWordName().replace("'", "\\'") + "'";
            }else
                sql = sql + word.getWordName() + "'";
        
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);
        
        int id=-1;
        if(rs.next()){
           id = rs.getInt(Configuration.KEY_WORD); 
        }
        return id;
    }
    
    public boolean isWordExist(String word_name) throws SQLException{
        String sql = "SELECT COUNT("+ Configuration.KEY_WORD +") as count_row FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.TAG_WORD_NAME + " LIKE '";
            if(word_name.contains("'")){
                sql = sql + word_name.replace("'", "\\'") + "'";
            } else
                sql = sql + word_name.toLowerCase() + "'";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);
        
        int row=-1;
        if(rs.next()) row = rs.getInt("count_row");
        
        if(row>0) return true;
        else return false;
    }
    
    public Word getWordByName(String word_name) throws SQLException{
        String sql = "SELECT * FROM " + Configuration.TAB_WORD + " WHERE " + Configuration.TAG_WORD_NAME + " LIKE '" ;
            if(word_name.contains("'")){
                sql = sql + word_name.replace("'", "\\'") + "'";
            } else
                sql = sql + word_name.toLowerCase() + "'";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);
        
        Word word = new Word();
        if(rs.next()){
            word.setIdWord(rs.getInt(Configuration.KEY_WORD));
            word.setWordName(rs.getString(Configuration.TAG_WORD_NAME));
            word.setWordPOS(rs.getString(Configuration.TAG_WORD_POS));
            word.setLemma(new Lemma(rs.getInt(Configuration.KEY_LEMMA)));
            word.setAffix(new Affix(rs.getInt(Configuration.KEY_AFFIX)));
        }
        
        return word;
    }
    
    public int insertWord(Word word) throws SQLException{
        String sql = "INSERT INTO " + Configuration.TAB_WORD + " (" + 
                Configuration.TAG_WORD_NAME + "," +
                Configuration.TAG_WORD_POS + "," +
                Configuration.KEY_AFFIX + "," +
                Configuration.KEY_LEMMA +
                ") VALUES (?,?,?,?)";
 
        PreparedStatement statement = conn.prepareStatement(sql);
        
        statement.setString(1,word.getWordName());
        statement.setString(2,word.getWordPOS());
        statement.setInt(3,word.getAffix().getIdAffix());
        statement.setInt(4,word.getLemma().getIdLemma());
        return statement.executeUpdate();
    }
    
    public void insertOrderWord(int id_sentence, int id_word, int number) throws SQLException{
        String sql = "INSERT INTO " + Configuration.TAB_SENTENCE_WORD + " (" + 
                Configuration.KEY_SENTENCE + "," +
                Configuration.KEY_WORD + "," +
                Configuration.TAG_NUMBER +
                ") VALUES (?,?,?)";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        statement.setInt(1,id_sentence);
        statement.setInt(2,id_word);
        statement.setInt(3,number);
        statement.executeUpdate();
    }
}
