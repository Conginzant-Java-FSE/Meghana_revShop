package org.example.service;

import org.example.dao.BuyerDao;
import org.example.dao.BuyerSecurityDao;
import org.example.dao.SellerDao;
import org.example.dao.SellerSecurityDao;
import org.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RecoveryService {

    private static final Logger log =
            LoggerFactory.getLogger(RecoveryService.class);

    private final BuyerDao buyerDao = new BuyerDao();
    private final SellerDao sellerDao = new SellerDao();
    private final BuyerSecurityDao buyerSecurityDao = new BuyerSecurityDao();
    private final SellerSecurityDao sellerSecurityDao = new SellerSecurityDao();

    public boolean buyerResetPassword(String email,
                                      Map<Integer, String> answers,
                                      String newPwd) {

        Integer buyerId = buyerDao.findBuyerIdByEmail(email);
        if (buyerId == null) {
            log.warn("Buyer reset password failed invalid email={}", email);
            return false;
        }

        if (!buyerSecurityDao.verifyAnswers(buyerId, answers)) {
            log.warn("Buyer reset password failed security verification buyerId={}", buyerId);
            return false;
        }

        boolean updated =
                buyerDao.resetPassword(email, newPwd);

        log.info("Buyer password reset buyerId={} success={}", buyerId, updated);
        return updated;
    }

    public boolean sellerResetPassword(String email,
                                       Map<Integer, String> answers,
                                       String newPwd) {

        Integer sellerId = sellerDao.findSellerIdByEmail(email);
        if (sellerId == null) {
            log.warn("Seller reset password failed invalid email={}", email);
            return false;
        }

        if (!sellerSecurityDao.verifyAnswers(sellerId, answers)) {
            log.warn("Seller reset password failed security verification sellerId={}", sellerId);
            return false;
        }

        boolean updated =
                sellerDao.resetPassword(email, newPwd);

        log.info("Seller password reset sellerId={} success={}", sellerId, updated);
        return updated;
    }

    public String buyerForgotEmail(String phone,
                                   Map<Integer, String> answers) {

        String email = buyerDao.findEmailByPhone(phone);
        if (email == null) {
            log.warn("Buyer forgot email failed phone not found phone={}", phone);
            return null;
        }

        Integer buyerId = buyerDao.findBuyerIdByEmail(email);
        if (buyerId == null) return null;

        if (!buyerSecurityDao.verifyAnswers(buyerId, answers)) {
            log.warn("Buyer forgot email failed security verification buyerId={}", buyerId);
            return null;
        }

        log.info("Buyer forgot email success buyerId={}", buyerId);
        return email;
    }

    public String sellerForgotEmail(String phone,
                                    Map<Integer, String> answers) {

        String email = sellerDao.findSellerEmailByPhone(phone);
        if (email == null) {
            log.warn("Seller forgot email failed phone not found phone={}", phone);
            return null;
        }

        Integer sellerId = sellerDao.findSellerIdByEmail(email);
        if (sellerId == null) return null;

        if (!sellerSecurityDao.verifyAnswers(sellerId, answers)) {
            log.warn("Seller forgot email failed security verification sellerId={}", sellerId);
            return null;
        }

        log.info("Seller forgot email success sellerId={}", sellerId);
        return email;
    }
}
