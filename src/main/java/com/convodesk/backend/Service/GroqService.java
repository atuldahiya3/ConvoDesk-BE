package com.convodesk.backend.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Simple Groq client calling Groq's chat/completion endpoint.
 * Replace API path according to your Groq plan.
 */
@Service
public class GroqService {

    @Value("${app.groq.apiKey}")
    private String groqKey;

    @Value("${app.groq.apiUrl}")
    private String groqUrl;

    private final WebClient webClient;

    public GroqService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(groqUrl).build();
    }

    public String generateReply(String prompt) {
        // Minimal payload — adjust according to Groq API contract.
        Map<String, Object> body = Map.of(
                "model", "groq-1",   // change to your model name
                "input", prompt,
                "max_output_tokens", 256
        );

        Mono<Map> resp = webClient.post()
                .uri("/completions") // change path if needed
                .header("Authorization", "Bearer " + groqKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class);

        Map result = resp.block();
        // parse result according to response shape
        if (result != null && result.get("output") != null) {
            Object out = result.get("output");
            // very generic fallback parsing:
            return out.toString();
        } else {
            return "Sorry, I couldn't process that right now.";
        }
    }
}

