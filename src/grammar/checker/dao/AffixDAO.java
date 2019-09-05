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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ru'fatiani
 */
public class AffixDAO {

    private Connection conn;
    
    public AffixDAO() {
        conn = ConnectionManager.getConn();
    }
    
    public int insertAffix(Affix affix) throws SQLException{
        String sql = "INSERT INTO " + Configuration.TAB_AFFIX + " (" + 
                Configuration.TAG_PREFIX_1 + "," +
                Configuration.TAG_PREFIX_2 + "," +
                Configuration.TAG_PREFIX_3 + "," +
                Configuration.TAG_SUFFIX_1 + "," +
                Configuration.TAG_SUFFIX_2 +
                ") VALUES (?,?,?,?,?)";
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        statement.setString(1,affix.getPrefix1());
        statement.setString(2,affix.getPrefix2());
        statement.setString(3,affix.getPrefix3());
        statement.setString(4,affix.getSuffix1());
        statement.setString(5,affix.getSuffix2());
        return statement.executeUpdate();
    }
    
    public int getAffixId(Affix affix) throws SQLException{
        String sql = "SELECT " + Configuration.KEY_AFFIX + " FROM " + Configuration.TAB_AFFIX + " WHERE ";
            if(affix.getPrefix1() != null){
                sql = sql + Configuration.TAG_PREFIX_1 + " LIKE '" + affix.getPrefix1().toLowerCase() + "' ";
            }else sql = sql + Configuration.TAG_PREFIX_1 + " IS NULL ";
     
            if(affix.getPrefix2() != null){
                sql = sql + "AND " + Configuration.TAG_PREFIX_2 + " LIKE '" + affix.getPrefix2().toLowerCase() + "' ";
            }else sql = sql + "AND " + Configuration.TAG_PREFIX_2 + " IS NULL ";
            
            if(affix.getPrefix3() != null){
                sql = sql + "AND " + Configuration.TAG_PREFIX_3 + " LIKE '" + affix.getPrefix2().toLowerCase() + "' ";
            }else sql = sql + "AND " + Configuration.TAG_PREFIX_3 + " IS NULL ";
            
            if(affix.getSuffix1() != null){
                sql = sql + "AND " + Configuration.TAG_SUFFIX_1 + " LIKE '" + affix.getSuffix1().toLowerCase() + "' ";
            }else sql = sql + "AND " + Configuration.TAG_SUFFIX_1 + " IS NULL ";
            
            if(affix.getSuffix2() != null){
                sql = sql + "AND " + Configuration.TAG_SUFFIX_2 + " LIKE '" + affix.getSuffix2().toLowerCase() + "'";
            }else sql = sql + "AND " + Configuration.TAG_SUFFIX_2 + " IS NULL";

        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        int id = -1;
        if(rs.next()){    
            id = rs.getInt(Configuration.KEY_AFFIX);
        }
        
        return id;
    }
    
    public Affix getAffixById(int id_affix) throws SQLException{
        String sql = "SELECT * FROM " + Configuration.TAB_AFFIX + " WHERE " + Configuration.KEY_AFFIX + " = " + id_affix;
 
        PreparedStatement statement = conn.prepareStatement(sql); 
        ResultSet rs = statement.executeQuery(sql);

        Affix affix = new Affix();
        while(rs.next()){    
            affix.setIdAffix(rs.getInt(Configuration.KEY_AFFIX));
            affix.setPrefix1(rs.getString(Configuration.TAG_PREFIX_1));
            affix.setPrefix2(rs.getString(Configuration.TAG_PREFIX_2));
            affix.setPrefix2(rs.getString(Configuration.TAG_PREFIX_3));
            affix.setSuffix1(rs.getString(Configuration.TAG_SUFFIX_1));
            affix.setSuffix2(rs.getString(Configuration.TAG_SUFFIX_2));
        }
        
        return affix;
    }
    
}
