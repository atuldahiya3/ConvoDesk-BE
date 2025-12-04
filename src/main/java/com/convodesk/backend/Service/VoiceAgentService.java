package com.convodesk.backend.Service;

import com.convodesk.backend.Entity.CallLog;
import com.convodesk.backend.Service.CallService;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class VoiceAgentService {

    private final LiveKitService liveKitService;
    private final DeepgramSttService deepgramSttService;
    private final GroqService groqService;
    private final AzureTtsService azureTtsService;
    private final CallService callService;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public VoiceAgentService(LiveKitService liveKitService,
                             DeepgramSttService deepgramSttService,
                             GroqService groqService,
                             AzureTtsService azureTtsService,
                             CallService callService) {
        this.liveKitService = liveKitService;
        this.deepgramSttService = deepgramSttService;
        this.groqService = groqService;
        this.azureTtsService = azureTtsService;
        this.callService = callService;
    }

    /**
     * Start agent for given call log and roomName.
     * This launches the STT websocket, registers a listener, and calls LLM for each completed utterance.
     */
    public void startAgent(Long callLogId, String roomName) {
        // Ensure room exists
        liveKitService.ensureRoom(roomName);

        // Generate identity & token for agent if needed (used if agent must join via LiveKit SDK)
        String agentIdentity = "agent-" + callLogId;
        String token = liveKitService.generateLiveKitToken(agentIdentity, roomName);

        // TODO: if you plan to join the room via LiveKit Java SDK, do it here and subscribe to caller audio,
        // then forward audio frames to Deepgram. Implementation depends on chosen LiveKit client.

        // For MVP we open a Deepgram websocket and expect telephony provider or LiveKit to push PCM frames to Deepgram.
        // Here we create a WebSocketListener to receive transcript messages and trigger LLM+TTS.
        WebSocketListener listener = new WebSocketListener() {
            private StringBuilder buffer = new StringBuilder();

            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                // Deepgram sends JSON transcript messages; parse and extract text
                // Very simplified parsing — replace with proper JSON parsing:
                String transcript = parseTranscriptFromDeepgramJson(text);
                if (transcript != null && !transcript.isBlank()) {
                    // Save caller segment
                    callService.addSegment(callLogId, "CALLER", transcript);

                    // Send to LLM (synchronously or using executor to avoid blocking)
                    executor.submit(() -> {
                        try {
                            // Build prompt with last few segments and business FAQ (fetch if needed from API)
                            String prompt = buildPromptForBusiness(callLogId, transcript);
                            String reply = groqService.generateReply(prompt);

                            // Save bot segment
                            callService.addSegment(callLogId, "BOT", reply);

                            // Convert to audio bytes
                            byte[] audioBytes = azureTtsService.synthesizeToWave(reply);

                            // Publish audioBytes back into LiveKit room (TODO: implement publish)
                            publishAudioToLivekit(roomName, agentIdentity, token, audioBytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // on error, mark call or attempt fallback message
                        }
                    });
                }
            }

            @Override
            public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
                // finalization
            }
        };

        // Start Deepgram WS for this call
        WebSocket ws = deepgramSttService.startRealtimeTranscription(callLogId, listener);

        // Save ws handle if you want to close it later (e.g., on endCall)
        // storeWebSocketHandle(callLogId, ws);
    }

    private String parseTranscriptFromDeepgramJson(String json) {
        // TODO: parse Deepgram JSON messages and extract "transcript" field.
        // For example, Deepgram realtime might send: { "channel":0, "is_final": true, "alternatives":[{"transcript":"hello"}] }
        // Use Jackson ObjectMapper here to parse properly.
        try {
            com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode node = om.readTree(json);
            if (node.has("channel")) {
                if (node.has("alternatives")) {
                    com.fasterxml.jackson.databind.JsonNode alt = node.get("alternatives").get(0);
                    if (alt != null && alt.has("transcript")) {
                        return alt.get("transcript").asText();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buildPromptForBusiness(Long callLogId, String transcript) {
        // TODO: fetch business FAQs from DB via callService or other service, combine with transcript.
        // For MVP, just return a minimal prompt
        return "Caller said: " + transcript + "\nAnswer concisely.";
    }

    private void publishAudioToLivekit(String roomName, String agentIdentity, String token, byte[] audioBytes) {
        // IMPORTANT: Implement publishing of audioBytes into the LiveKit room.
        // Options:
        // 1) Use LiveKit Java SDK to join the room as participant and publish PCM audio track.
        // 2) Use an external RTP/SIP gateway to inject audio into the call.
        //
        // This method is intentionally a placeholder because the implementation depends on chosen approach.
        //
        // If you choose LiveKit Java SDK, you'll:
        // - connect with token (roomName, agentIdentity)
        // - create an audio track from PCM/WAV bytes and publish
        //
        // If you prefer not to publish audio into LiveKit directly here, you can instead upload audioBytes to
        // a storage URL and instruct Twilio to play that URL (less real-time).
        //
        // For MVP testing, consider storing audio file and playing it via telephony provider playback API.
    }
}

