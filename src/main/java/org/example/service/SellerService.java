package org.example.service;

import org.example.dao.SellerDao;
import org.example.models.Seller;

public class SellerService {

    private final SellerDao sellerDao = new SellerDao();

    public boolean registerSeller(Seller seller){
        Integer id = sellerDao.registerSellerAndGetId(seller);
        if(id == null) return false;
        seller.setSellerId(id);
        return true;
    }

    public Seller login(String email,String password){
        return sellerDao.loginSeller(email,password);
    }

    public boolean resetPassword(String email,String newPwd){
        return sellerDao.resetPassword(email,newPwd);
    }

    public String findSellerEmailByPhone(String phone){
        return sellerDao.findSellerEmailByPhone(phone);
    }

    public Integer findSellerIdByEmail(String email){
        return sellerDao.findSellerIdByEmail(email);
    }
}
