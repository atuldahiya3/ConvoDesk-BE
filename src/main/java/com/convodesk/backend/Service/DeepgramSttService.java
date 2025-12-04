package com.convodesk.backend.Service;

import okhttp3.*;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Simple Deepgram streaming client using OkHttp WebSocket.
 * This class establishes a WebSocket to Deepgram and forwards PCM frames.
 *
 * NOTE: Deepgram expects PCM16 linear16 frames. You must ensure audio format from caller is compatible.
 *       For telephony (8k), you may need to resample to 16k depending on model chosen.
 *
 * This class provides a method to open a socket, send binary audio frames, and register a callback that notifies
 * when transcripts arrive (via server-sent JSON messages).
 */
@Service
public class DeepgramSttService {

    @Value("${app.deepgram.apiKey}")
    private String deepgramKey;

    @Value("${app.deepgram.sttEndpoint}")
    private String sttEndpoint;

    private final CallService callService; // to add segments

    public DeepgramSttService(CallService callService) {
        this.callService = callService;
    }

    public WebSocket startRealtimeTranscription(Long callLogId, WebSocketListener listener) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(sttEndpoint)
                .addHeader("Authorization", "Token " + deepgramKey)
                .build();

        WebSocket ws = client.newWebSocket(request, listener);
        // client.dispatcher().executorService().shutdown();
        return ws;
    }

    /**
     * Example of a listener (use as inner class or lambda) which parses JSON messages sent by Deepgram,
     * extracts transcript text and invokes callService.addSegment(callLogId, "CALLER", text).
     *
     * See Deepgram realtime docs for exact JSON message structure.
     */
}
