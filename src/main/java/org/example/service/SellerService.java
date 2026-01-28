package org.example.service;

import org.example.dao.SellerDao;

public class SellerService {

    private final SellerDao dao = new SellerDao();

    public boolean updateBusinessProfile(int sellerId,String gst,String web,String addr){
        return dao.updateBusinessProfile(sellerId,gst,web,addr);
    }
}
