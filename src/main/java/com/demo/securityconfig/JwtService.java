package com.demo.securityconfig;



import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
		
	public static final String SECURITY_KEY= "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";	
	
	// Method to generate Jwt token
	public String generateToken(String email) throws InvalidKeyException, NoSuchAlgorithmException {
		Map<String, Object> claims = new HashMap<>();
		
		log.info("In JwtService generateToken() enter");
		
		
	
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30000)).and()
				.signWith(getKey())
				.compact();
		
	}

	// Method to generate key for Jwt token
	private SecretKey getKey() {
		
		log.info("In JwtService getKey() enter");
		
		byte[] keyBytes = Decoders.BASE64.decode(SECURITY_KEY);
		
		
		log.info("In JwtService getKey() exit");
		
        return Keys.hmacShaKeyFor(keyBytes);

	}

	// Method to validate the Token (True if valid || False if invalid)
	public Boolean validateToken(String token, UserDetails userDetails) {
		/**
		 * First we'll need to get the username from token
		 * 
		 * Follow the flow 1 2 3.... --> To get the the username from token method will
		 * be called getUserName()
		 */
		final String userName = getUserName(token);// -->1
		
		log.info("In "+this+" validateToken() method with name "+userName);
		
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// Method to get the username from the token
	public String getUserName(String token) {

		// -->2
		// Here we will return the user name of from token

		/**
		 * 
		 * Before Returning the user name we have to get it from the claims of the token
		 * BY calling extractClaim()
		 * 
		 */
		// extract the username from jwt token
		return extractClaim(token, Claims::getSubject);
	}

	// Method to extract username from token

	// Method to verify if the token is Exired or not
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Method to extract the token expiration date
	private Date extractExpiration(String token) {

		return extractClaim(token, Claims::getExpiration);
	}

	// Method to get Extract claim
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

		// -->3

		// Here we have to call extractAllClaims() final to get all the claims

		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	// Method to get all the claims from the token
	private Claims extractAllClaims(String token) {
		// --> 4
		// This method will finally return the claim for us to get the user name

		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();

	}

}
