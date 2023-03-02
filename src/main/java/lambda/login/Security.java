package lambda.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Security {

    public String generateToken(String login,String password) {

        // Header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // Payload
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("login_id", login);
        payloads.put("password",password);

        int validity = 1000 * 60 * 60;//1시간 토큰

        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + validity);

        String secretKey = System.getenv("secretKey");
        byte[] decode = Decoders.BASE64.decode(secretKey);

        Key key=Keys.hmacShaKeyFor(decode);

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

}
