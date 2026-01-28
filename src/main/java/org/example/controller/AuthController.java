package org.example.controller;

import org.example.dao.SecurityQuestionDao;
import org.example.dao.BuyerSecurityDao;
import org.example.dao.SellerSecurityDao;
import org.example.models.Buyer;
import org.example.models.Seller;
import org.example.service.AuthService;
import org.example.service.RecoveryService;

import java.util.*;

public class AuthController {

    private final AuthService authService = new AuthService();
    private final RecoveryService recoveryService = new RecoveryService();

    private final SecurityQuestionDao questionDao = new SecurityQuestionDao();
    private final BuyerSecurityDao buyerSecDao = new BuyerSecurityDao();
    private final SellerSecurityDao sellerSecDao = new SellerSecurityDao();

    private final Scanner sc = new Scanner(System.in);

    public Buyer buyerLogin() {

        System.out.println("""
1.Login
2.Forgot Password
3.Forgot Email
0.Back
""");

        System.out.print("Select an option: ");

        int ch;
        try{
            ch=Integer.parseInt(sc.nextLine());
        }catch(Exception e){
            System.out.println("Invalid input");
            return null;
        }

        if(ch==2){ forgotBuyerPassword(); return null;}
        if(ch==3){ forgotBuyerEmail(); return null;}
        if(ch==0) return null;

        System.out.print("Email: ");
        String email=sc.nextLine();

        System.out.print("Password: ");
        String pass=sc.nextLine();

        Buyer b=authService.buyerLogin(email,pass);

        if(b==null) System.out.println("Invalid credentials");

        return b;
    }

    public Seller sellerLogin(){

        System.out.println("""
1.Login
2.Forgot Password
3.Forgot Email
0.Back
""");

        System.out.print("Select an option: ");

        int ch;
        try{
            ch=Integer.parseInt(sc.nextLine());
        }catch(Exception e){
            System.out.println("Invalid input");
            return null;
        }

        if(ch==2){ forgotSellerPassword(); return null; }
        if(ch==3){ forgotSellerEmail(); return null; }
        if(ch==0) return null;

        System.out.print("Email: ");
        String email=sc.nextLine();

        System.out.print("Password: ");
        String pass=sc.nextLine();

        Seller s=authService.sellerLogin(email,pass);

        if(s==null) System.out.println("Invalid credentials");

        return s;
    }

    public void registerBuyer(){

        System.out.print("Full Name: ");
        String name=sc.nextLine();

        System.out.print("Email: ");
        String email=sc.nextLine();

        System.out.print("Password: ");
        String pass=sc.nextLine();

        System.out.print("Phone: ");
        String phone=sc.nextLine();

        Buyer buyer=authService.registerBuyerReturnBuyer(name,email,pass,phone);
        if(buyer==null){ System.out.println("Registration failed"); return;}

        captureBuyerSecurity(buyer.getBuyerId());
        System.out.println("Buyer registered");
    }

    public void registerSeller(){

        System.out.print("Business Name: ");
        String biz=sc.nextLine();

        System.out.print("Owner Name: ");
        String owner=sc.nextLine();

        System.out.print("Email: ");
        String email=sc.nextLine();

        System.out.print("Password: ");
        String pass=sc.nextLine();

        System.out.print("Phone: ");
        String phone=sc.nextLine();

        Seller seller=authService.registerSellerReturnSeller(biz,owner,email,pass,phone);
        if(seller==null){ System.out.println("Registration failed"); return;}

        captureSellerSecurity(seller.getSellerId());
        System.out.println("Seller registered");
    }

    private void captureBuyerSecurity(int id){
        questionDao.getAllQuestions().forEach((k,v)->{
            System.out.println(k+". "+v);
            System.out.print("Answer (select 0 to stop): ");
            String a=sc.nextLine();
            if(!a.equals("0")) buyerSecDao.saveAnswer(id,k,a);
        });
    }

    private void captureSellerSecurity(int id){
        questionDao.getAllQuestions().forEach((k,v)->{
            System.out.println(k+". "+v);
            System.out.print("Answer (select 0 to stop): ");
            String a=sc.nextLine();
            if(!a.equals("0")) sellerSecDao.saveAnswer(id,k,a);
        });
    }

    private void forgotBuyerPassword(){}
    private void forgotBuyerEmail(){}
    private void forgotSellerPassword(){}
    private void forgotSellerEmail(){}
}
