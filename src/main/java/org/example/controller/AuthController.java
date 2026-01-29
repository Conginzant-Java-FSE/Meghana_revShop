package org.example.controller;

import org.example.dao.BuyerSecurityDao;
import org.example.dao.SellerSecurityDao;
import org.example.dao.SecurityQuestionDao;
import org.example.models.Buyer;
import org.example.models.Seller;
import org.example.service.AuthService;
import org.example.service.RecoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class AuthController {

    private static final Logger log =
            LoggerFactory.getLogger(AuthController.class);

    private final Scanner sc = new Scanner(System.in);
    private final AuthService authService = new AuthService();
    private final RecoveryService recoveryService = new RecoveryService();

    private final SecurityQuestionDao questionDao = new SecurityQuestionDao();
    private final BuyerSecurityDao buyerSecurityDao = new BuyerSecurityDao();
    private final SellerSecurityDao sellerSecurityDao = new SellerSecurityDao();

    public void registerBuyer() {

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();

        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();

        if (name.isEmpty() || email.isEmpty() || pwd.isEmpty() || phone.isEmpty()) {
            System.out.println("All fields are mandatory");
            return;
        }

        Integer buyerId = authService.registerBuyer(name, email, pwd, phone)
                ? authService.findBuyerIdByEmail(email)
                : null;

        if (buyerId == null) {
            System.out.println("Registration failed");
            return;
        }

        askSecurityQuestionsForBuyer(buyerId);

        log.info("Buyer registered successfully email={}", email);
        System.out.println("Registered");
    }

    public Buyer buyerLogin() {

        while (true) {

            System.out.println("1.Login");
            System.out.println("2.Forgot Password");
            System.out.println("3.Forgot Email");
            System.out.println("0.Back");

            System.out.print("Select an option: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {

                case 1 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine().trim();

                    System.out.print("Password: ");
                    String pwd = sc.nextLine().trim();

                    if (email.isEmpty() || pwd.isEmpty()) {
                        System.out.println("Email and password are required");
                        continue;
                    }

                    Buyer buyer = authService.buyerLogin(email, pwd);

                    if (buyer == null) {
                        Integer id = authService.findBuyerIdByEmail(email);
                        System.out.println(id == null
                                ? "Email not registered"
                                : "Incorrect password");
                        log.info("Buyer login failed email={}", email);
                        continue;
                    }

                    log.info("Buyer login success email={}", email);
                    return buyer;
                }

                case 2 -> handleBuyerForgotPassword();
                case 3 -> handleBuyerForgotEmail();
                case 0 -> { return null; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    public void registerSeller() {

        System.out.print("Business Name: ");
        String biz = sc.nextLine().trim();

        System.out.print("Owner Name: ");
        String owner = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();

        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();

        if (biz.isEmpty() || owner.isEmpty() || email.isEmpty() || pwd.isEmpty() || phone.isEmpty()) {
            System.out.println("All fields are mandatory");
            return;
        }

        Integer sellerId = authService.registerSeller(biz, owner, email, pwd, phone)
                ? authService.findSellerIdByEmail(email)
                : null;

        if (sellerId == null) {
            System.out.println("Registration failed");
            return;
        }

        askSecurityQuestionsForSeller(sellerId);

        log.info("Seller registered successfully email={}", email);
        System.out.println("Registered");
    }

    public Seller sellerLogin() {

        while (true) {

            System.out.println("1.Login");
            System.out.println("2.Forgot Password");
            System.out.println("3.Forgot Email");
            System.out.println("0.Back");

            System.out.print("Select an option: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {

                case 1 -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine().trim();

                    System.out.print("Password: ");
                    String pwd = sc.nextLine().trim();

                    if (email.isEmpty() || pwd.isEmpty()) {
                        System.out.println("Email and password are required");
                        continue;
                    }

                    Seller seller = authService.sellerLogin(email, pwd);

                    if (seller == null) {
                        Integer id = authService.findSellerIdByEmail(email);
                        System.out.println(id == null
                                ? "Email not registered"
                                : "Incorrect password");
                        log.info("Seller login failed email={}", email);
                        continue;
                    }

                    log.info("Seller login success email={}", email);
                    return seller;
                }

                case 2 -> handleSellerForgotPassword();
                case 3 -> handleSellerForgotEmail();
                case 0 -> { return null; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void askSecurityQuestionsForBuyer(int buyerId) {

        Map<Integer, String> questions = questionDao.getAllQuestions();
        Map<Integer, String> answers = new LinkedHashMap<>();

        System.out.println("Answer at least ONE security question (press Enter to skip)");

        for (var q : questions.entrySet()) {
            System.out.print(q.getValue() + ": ");
            String ans = sc.nextLine().trim();
            if (!ans.isEmpty()) {
                answers.put(q.getKey(), ans);
            }
        }

        if (answers.isEmpty()) {
            System.out.println("At least one answer required");
            askSecurityQuestionsForBuyer(buyerId);
            return;
        }

        answers.forEach((qid, ans) ->
                buyerSecurityDao.saveAnswer(buyerId, qid, ans));
    }

    private void askSecurityQuestionsForSeller(int sellerId) {

        Map<Integer, String> questions = questionDao.getAllQuestions();
        Map<Integer, String> answers = new LinkedHashMap<>();

        System.out.println("Answer at least ONE security question (press Enter to skip)");

        for (var q : questions.entrySet()) {
            System.out.print(q.getValue() + ": ");
            String ans = sc.nextLine().trim();
            if (!ans.isEmpty()) {
                answers.put(q.getKey(), ans);
            }
        }

        if (answers.isEmpty()) {
            System.out.println("At least one answer required");
            askSecurityQuestionsForSeller(sellerId);
            return;
        }

        answers.forEach((qid, ans) ->
                sellerSecurityDao.saveAnswer(sellerId, qid, ans));
    }

    private void handleBuyerForgotPassword() {

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        Integer buyerId = authService.findBuyerIdByEmail(email);
        if (buyerId == null) {
            System.out.println("Email not registered");
            return;
        }

        Map<Integer, String> questions =
                buyerSecurityDao.getQuestionsForBuyer(buyerId);

        Map<Integer, String> answers = new LinkedHashMap<>();
        for (var q : questions.entrySet()) {
            System.out.print(q.getValue() + ": ");
            answers.put(q.getKey(), sc.nextLine().trim());
        }

        System.out.print("New password: ");
        String pwd = sc.nextLine().trim();

        boolean ok =
                recoveryService.buyerResetPassword(email, answers, pwd);

        System.out.println(ok ? "Password updated" : "Security verification failed");
    }

    private void handleBuyerForgotEmail() {

        System.out.print("Enter phone: ");
        String phone = sc.nextLine().trim();

        String email = authService.findBuyerEmailByPhone(phone);
        if (email == null) {
            System.out.println("Phone not registered");
            return;
        }

        Integer buyerId = authService.findBuyerIdByEmail(email);

        Map<Integer, String> questions =
                buyerSecurityDao.getQuestionsForBuyer(buyerId);

        Map<Integer, String> answers = new LinkedHashMap<>();
        for (var q : questions.entrySet()) {
            System.out.print(q.getValue() + ": ");
            answers.put(q.getKey(), sc.nextLine().trim());
        }

        String result =
                recoveryService.buyerForgotEmail(phone, answers);

        System.out.println(result == null
                ? "Verification failed"
                : "Your email: " + result);
    }

    private void handleSellerForgotPassword() {

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        Integer sellerId = authService.findSellerIdByEmail(email);
        if (sellerId == null) {
            System.out.println("Email not registered");
            return;
        }

        Map<Integer, String> questions =
                sellerSecurityDao.getQuestionsForSeller(sellerId);

        Map<Integer, String> answers = new LinkedHashMap<>();
        for (var q : questions.entrySet()) {
            System.out.print(q.getValue() + ": ");
            answers.put(q.getKey(), sc.nextLine().trim());
        }

        System.out.print("New password: ");
        String pwd = sc.nextLine().trim();

        boolean ok =
                recoveryService.sellerResetPassword(email, answers, pwd);

        System.out.println(ok ? "Password updated" : "Security verification failed");
    }

    private void handleSellerForgotEmail() {

        System.out.print("Enter phone: ");
        String phone = sc.nextLine().trim();

        String email = authService.findSellerEmailByPhone(phone);
        if (email == null) {
            System.out.println("Phone not registered");
            return;
        }

        Integer sellerId = authService.findSellerIdByEmail(email);

        Map<Integer, String> questions =
                sellerSecurityDao.getQuestionsForSeller(sellerId);

        Map<Integer, String> answers = new LinkedHashMap<>();
        for (var q : questions.entrySet()) {
            System.out.print(q.getValue() + ": ");
            answers.put(q.getKey(), sc.nextLine().trim());
        }

        String result =
                recoveryService.sellerForgotEmail(phone, answers);

        System.out.println(result == null
                ? "Verification failed"
                : "Your email: " + result);
    }
}
