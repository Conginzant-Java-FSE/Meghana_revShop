package org.example.service;

import org.example.dao.FavoriteDao;
import org.example.models.Product;

import java.util.List;

public class FavoriteService {

    private final FavoriteDao dao = new FavoriteDao();

    public boolean add(int buyerId, int productId) {
        return dao.addFavorite(buyerId, productId);
    }

    public boolean remove(int buyerId, int productId) {
        return dao.removeFavorite(buyerId, productId);
    }

    public List<Product> list(int buyerId) {
        return dao.getFavorites(buyerId);
    }
}
