package com.blackcoffee.projectmanagement.config;

import com.blackcoffee.projectmanagement.utils.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtProvider {
      static SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());


    public static String generateToken(Authentication auth){
        Date current= new Date();
        Date expireDate= new Date(current.getTime()+JwtConstant.EXPIRED_JWT);
        String token= Jwts.builder()
                .issuedAt(new Date())
                .expiration(expireDate)
                .claim("email", auth.getName())
                .signWith(key).compact();
        return token;

    }
    public static String getEmail(String token){
        token= token.substring(7);
        Claims claims= Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String email= String.valueOf(claims.get("email"));
        return email;
    }

//    public static String generateTokenToProject(Long invitationDto){
//        Date current= new Date();
//        Date expireDate= new Date(current.getTime()+JwtConstant.EXPIRED_JWT);
//        String token= Jwts.builder()
//                .issuedAt(new Date())
//                .expiration(expireDate)
//                .claim("projectId", invitationDto)
//                .signWith(key).compact();
//        return token;
//
//    }
    public static Long getProjectId(String token){
        token= token.substring(7);
        Claims claims= Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Long projectId= (Long) claims.get("projectId");
        return projectId;
    }
}
