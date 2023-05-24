package nus.iss.chatapp.com.server.dev;

public record JwtResponse(String username, String userToken, String expiresAt) {}
