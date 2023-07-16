package com.consultas.proyecto.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;



@Service
public class TokenService {
    final static SecretKey key= Keys.secretKeyFor(SignatureAlgorithm.HS512);


    public String generateToken(String username, int segundos) {

        Date exp = getExpiration(new Date(), segundos);

        return Jwts.builder().setSubject(username).signWith(key).setExpiration(exp).compact();
    }

    private Date getExpiration(Date date, int segundos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, segundos);

        return calendar.getTime();
    }
    public static boolean validateToken(String token) {

        String prefix = "Bearer";
        try {

            if (token.startsWith(prefix)) {
                token = token.substring(prefix.length()).trim();
            }


            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();


            return true;
        } catch (ExpiredJwtException exp) {
            System.out.println("El Token es valido, pero expiro su tiempo de validez");
            return false;
        } catch (JwtException e) {

            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }}
