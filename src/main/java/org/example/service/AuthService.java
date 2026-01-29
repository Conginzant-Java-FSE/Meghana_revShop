package org.example.service;

import org.example.dao.BuyerDao;
import org.example.dao.SellerDao;
import org.example.models.Buyer;
import org.example.models.Seller;
import org.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthService.class);

    private final BuyerDao buyerDao = new BuyerDao();
    private final SellerDao sellerDao = new SellerDao();

    public boolean registerBuyer(String name,
                                 String email,
                                 String password,
                                 String phone) {

        Buyer b = new Buyer();
        b.setFullName(name);
        b.setEmail(email);
        b.setPassword(PasswordUtil.hashPassword(password));
        b.setPhone(phone);

        Integer id = buyerDao.registerBuyerAndGetId(b);
        log.info("Buyer registration email={} success={}", email, id != null);
        return id != null;
    }

    public boolean registerSeller(String biz,
                                  String owner,
                                  String email,
                                  String password,
                                  String phone) {

        Seller s = new Seller();
        s.setBusinessName(biz);
        s.setOwnerName(owner);
        s.setEmail(email);
        s.setPassword(PasswordUtil.hashPassword(password));
        s.setPhone(phone);

        Integer id = sellerDao.registerSellerAndGetId(s);
        log.info("Seller registration email={} success={}", email, id != null);
        return id != null;
    }

    public Buyer buyerLogin(String email, String password) {
        return buyerDao.loginBuyer(email, password);
    }

    public Seller sellerLogin(String email, String password) {
        return sellerDao.loginSeller(email, password);
    }

    public Integer findBuyerIdByEmail(String email) {
        return buyerDao.findBuyerIdByEmail(email);
    }

    public Integer findSellerIdByEmail(String email) {
        return sellerDao.findSellerIdByEmail(email);
    }

    public String findBuyerEmailByPhone(String phone) {
        return buyerDao.findEmailByPhone(phone);
    }

    public String findSellerEmailByPhone(String phone) {
        return sellerDao.findSellerEmailByPhone(phone);
    }
}
