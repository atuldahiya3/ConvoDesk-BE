package com.convodesk.backend.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class LiveKitService {

    @Value("${app.livekit.apiKey}")
    private String apiKey;

    @Value("${app.livekit.apiSecret}")
    private String apiSecret;

    @Value("${app.livekit.url}")
    private String livekitUrl;

    // TTL for token in ms
    private final long TOKEN_TTL_MS = 1000L * 60 * 60; // 1 hour

    /**
     * Generate a LiveKit token (server JWT) for a given identity and room.
     * This token structure is compatible with LiveKit tokens (simple JWT claims).
     */
    public String generateLiveKitToken(String identity, String roomName) {
        // apiSecret must be a sufficiently long secret.
        byte[] secretBytes = apiSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(secretBytes);

        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + TOKEN_TTL_MS);

        Map<String, Object> grants = Map.of(
                "roomJoin", true,
                "room", roomName
        );

        String jwt = Jwts.builder()
                .setSubject(identity)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .claim("kid", apiKey)
                .claim("grants", grants)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }

    /**
     * Room creation: LiveKit auto-creates rooms when first participant connects.
     * Optionally you can call LiveKit Admin REST API to pre-create a room.
     * For MVP we'll rely on auto-create; if you need admin creation, implement REST POST /rooms.
     */
    public String ensureRoom(String roomName) {
        // For now just return roomName
        return roomName;
    }
}

