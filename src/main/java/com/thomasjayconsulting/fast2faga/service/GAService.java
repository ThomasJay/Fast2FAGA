package com.thomasjayconsulting.fast2faga.service;

import com.thomasjayconsulting.fast2faga.model.User;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

@Service
@Slf4j
public class GAService {

    // mongoDB Collection Name
    private final String USERS_COLLECTION = "users";

    @Value("${companyName}")
    private String companyName;

    @Autowired
    private MongoTemplate mongoTemplate;

    String QR_PREFIX = "https://qrcode.tec-it.com/API/QRCode?data=";


    public String registerNewUser(String name, String email) {

        // Create new Secret
        String secret = Base32.random();

        String qrCodeURL = generateQRUrl(secret, email);

        User user = new User();

        user.setName(name.toUpperCase());
        user.setEmail(email.toUpperCase());
        user.setSecret(secret);

        mongoTemplate.insert(user, USERS_COLLECTION);


        return qrCodeURL;
    }

    public User authenticate(String email, String code) {

        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email.toUpperCase()));

        User user = mongoTemplate.findOne(query, User.class, USERS_COLLECTION);

        if (user != null) {

            Totp totp = new Totp(user.getSecret());

            // If this is a valid code for this time, then return the user
            if (totp.verify(code)) {
                return user;
            }

        }

        return null;
    }

    private String generateQRUrl(String secret, String email)  {

        log.info("Secret: {}", secret);

        try {
            return QR_PREFIX + URLEncoder.encode(String.format(
                            "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                            companyName, email, secret, companyName),
                    "UTF-8");

        }
        catch (Exception e) {
            return "error";
        }
    }
}
