package org.example.service;

import org.example.dao.CategoryDao;
import org.example.models.Category;

import java.util.List;

public class CategoryService {

    private final CategoryDao dao = new CategoryDao();

    public List<Category> getAllCategories() {
        return dao.getAllCategories();
    }
}
