package org.example.service;

import org.example.dao.*;
import org.example.util.PasswordUtil;

import java.util.*;

public class RecoveryService {

    private final BuyerDao buyerDao = new BuyerDao();
    private final SellerDao sellerDao = new SellerDao();
    private final BuyerSecurityDao buyerSecDao = new BuyerSecurityDao();
    private final SellerSecurityDao sellerSecDao = new SellerSecurityDao();
    private final PasswordResetDao resetDao = new PasswordResetDao();


    public boolean resetBuyerPasswordByEmail(String email,
                                             Map<Integer,String> answers,
                                             String p1,String p2){

        if(!p1.equals(p2)) return false;

        Integer id = buyerDao.findBuyerIdByEmail(email);
        if(id==null) return false;

        if(!buyerSecDao.verifyAnswers(id,answers)) return false;

        return resetDao.updateBuyerPassword(id,
                PasswordUtil.hashPassword(p1));
    }

    public Map<Integer,String> getBuyerQuestionsByPhone(String phone){

        String email = buyerDao.findEmailByPhone(phone);
        if(email==null) return Map.of();

        Integer id = buyerDao.findBuyerIdByEmail(email);
        if(id==null) return Map.of();

        return buyerSecDao.getQuestionsForBuyer(id);
    }

    public Map<Integer,String> getBuyerQuestions(String email){

        Integer id = buyerDao.findBuyerIdByEmail(email);
        if(id==null) return Map.of();

        return buyerSecDao.getQuestionsForBuyer(id);
    }

    public String recoverBuyerEmail(String phone,Map<Integer,String> answers){

        String email = buyerDao.findEmailByPhone(phone);
        if(email==null) return null;

        Integer id = buyerDao.findBuyerIdByEmail(email);
        if(id==null) return null;

        if(!buyerSecDao.verifyAnswers(id,answers)) return null;

        return email;
    }


    public boolean resetSellerPasswordByEmail(String email,
                                              Map<Integer,String> answers,
                                              String p1,String p2){

        if(!p1.equals(p2)) return false;

        Integer id = sellerDao.findSellerIdByEmail(email);
        if(id==null) return false;

        if(!sellerSecDao.verifyAnswers(id,answers)) return false;

        return resetDao.updateSellerPassword(id,
                PasswordUtil.hashPassword(p1));
    }

    public Map<Integer,String> getSellerQuestions(String email){

        Integer id = sellerDao.findSellerIdByEmail(email);
        if(id==null) return Map.of();

        return sellerSecDao.getQuestionsForSeller(id);
    }

    public Map<Integer,String> getSellerQuestionsByPhone(String phone){

        String email = sellerDao.findSellerEmailByPhone(phone);
        if(email==null) return Map.of();

        Integer id = sellerDao.findSellerIdByEmail(email);
        if(id==null) return Map.of();

        return sellerSecDao.getQuestionsForSeller(id);
    }

    public String recoverSellerEmail(String phone,Map<Integer,String> answers){

        String email = sellerDao.findSellerEmailByPhone(phone);
        if(email==null) return null;

        Integer id = sellerDao.findSellerIdByEmail(email);
        if(id==null) return null;

        if(!sellerSecDao.verifyAnswers(id,answers)) return null;

        return email;
    }
}
