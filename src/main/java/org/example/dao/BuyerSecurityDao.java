package org.example.dao;

import org.example.config.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class BuyerSecurityDao {

    private static final Logger log = LoggerFactory.getLogger(BuyerSecurityDao.class);

    public void saveAnswer(int buyerId, int qid, String answer) {

        String sql = "INSERT INTO buyer_security(buyer_id,question_id,answer) VALUES(?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ps.setInt(2, qid);
            ps.setString(3, answer);
            ps.executeUpdate();

        } catch (Exception e) {
            log.error("save buyer security failed", e);
        }
    }

    public Map<Integer,String> getBuyerQuestions(int buyerId){

        Map<Integer,String> map=new LinkedHashMap<>();

        String sql="""
        SELECT q.question_id,q.question_text
        FROM buyer_security bs
        JOIN security_question q ON bs.question_id=q.question_id
        WHERE bs.buyer_id=?
        """;

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(sql)){

            ps.setInt(1,buyerId);
            var rs=ps.executeQuery();

            while(rs.next())
                map.put(rs.getInt(1),rs.getString(2));

        }catch(Exception e){}

        return map;
    }

    public boolean verifyAnswers(int buyerId, Map<Integer,String> answers) {

        String sql =
                "SELECT answer FROM buyer_security WHERE buyer_id=? AND question_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (var entry : answers.entrySet()) {

                ps.setInt(1, buyerId);
                ps.setInt(2, entry.getKey());

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) return false;

                if (!rs.getString(1).equalsIgnoreCase(entry.getValue()))
                    return false;
            }

            return true;

        } catch (Exception e) {
            log.error("verify buyer answers failed", e);
        }

        return false;
    }
    public Map<Integer,String> getQuestionsForBuyer(int buyerId){

        Map<Integer,String> map = new LinkedHashMap<>();

        String sql = """
        SELECT bs.question_id, sq.question_text
        FROM buyer_security bs
        JOIN security_question sq
        ON bs.question_id = sq.question_id
        WHERE bs.buyer_id = ?
    """;

        try(var con = DBConnection.getConnection();
            var ps = con.prepareStatement(sql)){

            ps.setInt(1, buyerId);

            var rs = ps.executeQuery();

            while(rs.next()){
                map.put(rs.getInt(1), rs.getString(2));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return map;
    }


}
