package com.springboot.app.auth.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.auth.SimpleGrantedAuthorityMixin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTServiceImpl implements JWTService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String SECRET = Base64Utils.encodeToString("Hello.From.Another.Api.In.SpringBoot".getBytes());
	
	public static final long EXPIRATION_DATE = 3600000L;
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization";
	
	@Override
	public String create(Authentication auth) throws IOException {
		String username = ((User) auth.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));

		String token = Jwts.builder().setSubject(username).setClaims(claims)
				.signWith(new SecretKeySpec(SECRET.getBytes(),
						SignatureAlgorithm.HS512.getJcaName()))
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE)).compact();
		return token;
	}

	@Override
	public boolean validate(String token) {
		try {
			this.getClaims(token);
			return true;
			
		} catch (JwtException | IllegalArgumentException e) {
			logger.info("Error: " + e.getMessage());
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {
	  Claims claims = Jwts.parser().
				setSigningKey(SECRET.getBytes())
			    .parseClaimsJws(this.resolveToken(token))
			    .getBody();
		return claims;
	}

	@Override
	public String getDataOfUserFromToken(String token) {
		return this.getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = this.getClaims(token).get("authorities");
		
		Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
				.addMixIn(SimpleGrantedAuthority.class,SimpleGrantedAuthorityMixin.class)
				.readValue(roles.toString().getBytes(),SimpleGrantedAuthority[].class)); 
		
		return authorities;
	}

	@Override
	public String resolveToken(String token) {
		if(token != null & token.startsWith(TOKEN_PREFIX))
			return token.replace(TOKEN_PREFIX, "");
		return null;
	}

}
