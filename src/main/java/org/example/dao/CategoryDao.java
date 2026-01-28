package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class CategoryDao {

    private static final Logger log = LoggerFactory.getLogger(CategoryDao.class);

    public List<Category> getAllCategories() {

        List<Category> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM category");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt(1));
                c.setCategoryName(rs.getString(2));
                list.add(c);
            }

        } catch (Exception e) {
            log.error("getAllCategories failed", e);
        }

        return list;
    }
}
