package com.convodesk.backend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Service
public class AzureTtsService {

    @Value("${app.azure.tts.subscriptionKey}")
    private String azureKey;

    @Value("${app.azure.tts.region}")
    private String azureRegion;

    private final WebClient webClient;

    public AzureTtsService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://" + azureRegion + ".tts.speech.microsoft.com").build();
    }

    /**
     * Synthesize text to PCM WAV bytes using Azure Cognitive Services REST TTS.
     * Returns raw audio bytes (WAV).
     */
    public byte[] synthesizeToWave(String text) {
        // Create SSML body
        String ssml = "<speak version='1.0' xml:lang='en-US'>" +
                "<voice name='en-US-AriaNeural'>" +
                text +
                "</voice></speak>";

        Mono<byte[]> resp = webClient.post()
                .uri("/cognitiveservices/v1")
                .header("Ocp-Apim-Subscription-Key", azureKey)
                .header("Content-Type", "application/ssml+xml")
                .header("X-MICROSOFT-OutputFormat", "riff-16khz-16bit-mono-pcm")
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(ssml)
                .retrieve()
                .bodyToMono(byte[].class);

        return resp.block();
    }
}

