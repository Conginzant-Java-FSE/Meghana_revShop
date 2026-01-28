package org.example.service;

import org.example.dao.BuyerDao;
import org.example.models.Buyer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuyerService {

    private static final Logger log = LoggerFactory.getLogger(BuyerService.class);
    private final BuyerDao buyerDao = new BuyerDao();

    public Integer registerBuyer(Buyer buyer) {
        log.info("Register buyer {}", buyer.getEmail());
        return buyerDao.registerBuyerAndGetId(buyer);
    }
}
