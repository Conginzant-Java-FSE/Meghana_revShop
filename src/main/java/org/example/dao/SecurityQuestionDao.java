package org.example.dao;

import org.example.config.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class SecurityQuestionDao {

    private static final Logger log = LoggerFactory.getLogger(SecurityQuestionDao.class);

    public Map<Integer,String> getAllQuestions() {

        Map<Integer,String> map = new LinkedHashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT question_id,question_text FROM security_question");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) map.put(rs.getInt(1), rs.getString(2));

        } catch (Exception e) {
            log.error("getAllQuestions failed", e);
        }

        return map;
    }
}
