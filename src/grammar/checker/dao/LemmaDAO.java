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
public class LemmaDAO {
    
    private Connection conn;

    public LemmaDAO() {
        conn = ConnectionManager.getConn();
    }
    
    public Lemma getLemmaByName(String lemma) throws SQLException{
        String sql = "SELECT * FROM " + Configuration.TAB_LEMMA + " WHERE " + Configuration.TAG_LEMMA_NAME + " LIKE '" ;
            if(lemma.contains("'")){
                sql = sql + lemma.toLowerCase().replace("'", "\\'") + "'";
            } else
                sql = sql + lemma.toLowerCase() + "'";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        Lemma l = new Lemma();
        while(rs.next()){    
            l.setIdLemma(rs.getInt(Configuration.KEY_LEMMA));
            l.setLemmaName(rs.getString(Configuration.TAG_LEMMA_NAME));
            l.setLemmaPOS(rs.getString(Configuration.TAG_LEMMA_POS));
        }

        return l;
    }
    
    public Lemma getLemmaById(int id_lemma) throws SQLException{
        String sql = "SELECT * FROM " + Configuration.TAB_LEMMA + " WHERE " + Configuration.KEY_LEMMA + " = " + id_lemma;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        Lemma l = new Lemma();
        while(rs.next()){    
            l.setIdLemma(rs.getInt(Configuration.KEY_LEMMA));
            l.setLemmaName(rs.getString(Configuration.TAG_LEMMA_NAME));
            l.setLemmaPOS(rs.getString(Configuration.TAG_LEMMA_POS));
        }
        
        return l;
    }
    
    public int isLemma(String lemma_name) throws SQLException{
        String sql = "SELECT COUNT("+ Configuration.KEY_LEMMA +") as count_row FROM " + Configuration.TAB_LEMMA + " WHERE " + Configuration.TAG_LEMMA_NAME + " LIKE '";
            if(lemma_name.contains("'")){
                sql = sql + lemma_name.toLowerCase().replace("'", "\\'") + "'";
            } else
                sql = sql + lemma_name.toLowerCase() + "'";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);
        
        int row=-1;
        if(rs.next()) row = rs.getInt("count_row");

        return row;
    }
    
    public int insertLemma(Lemma lemma) throws SQLException{
        String sql = "INSERT INTO " + Configuration.TAB_LEMMA + " (" + 
                Configuration.TAG_LEMMA_NAME + "," +
                Configuration.TAG_LEMMA_POS +
                ") VALUES (?,?)";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        statement.setString(1,lemma.getLemmaName());
        statement.setString(2,lemma.getLemmaPOS());
        return statement.executeUpdate();
    }
}
