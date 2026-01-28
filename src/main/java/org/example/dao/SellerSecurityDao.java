package org.example.dao;

import org.example.config.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class SellerSecurityDao {

    private static final Logger log = LoggerFactory.getLogger(SellerSecurityDao.class);

    public void saveAnswer(int sellerId, int qid, String answer) {

        String sql = "INSERT INTO seller_security(seller_id,question_id,answer) VALUES(?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ps.setInt(2, qid);
            ps.setString(3, answer);

            ps.executeUpdate();

        } catch (Exception e) {
            log.error("save seller security failed", e);
        }
    }

    public boolean verifyAnswers(int sellerId, Map<Integer,String> answers) {

        String sql =
                "SELECT answer FROM seller_security WHERE seller_id=? AND question_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (var e : answers.entrySet()) {

                ps.setInt(1, sellerId);
                ps.setInt(2, e.getKey());

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) return false;

                if (!rs.getString(1).equalsIgnoreCase(e.getValue()))
                    return false;
            }

            return true;

        } catch (Exception ex) {
            log.error("verify seller answers failed", ex);
        }

        return false;
    }
    public Map<Integer,String> getQuestionsForSeller(int sellerId){

        Map<Integer,String> map = new LinkedHashMap<>();

        String sql = """
        SELECT sq.question_id,sq.question_text
        FROM seller_security ss
        JOIN security_question sq
        ON ss.question_id=sq.question_id
        WHERE ss.seller_id=?
        """;

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(sql)){

            ps.setInt(1,sellerId);
            var rs=ps.executeQuery();

            while(rs.next())
                map.put(rs.getInt(1),rs.getString(2));

        }catch(Exception e){
            log.error("get seller questions failed",e);
        }

        return map;
    }

}
