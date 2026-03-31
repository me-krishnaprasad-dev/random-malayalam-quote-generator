package com.kp.random_malayalam_quote_generator.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.random_malayalam_quote_generator.service.GeminiTranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Gemini Translation Service
 * This service translates quotes to Malayalam using Google Gemini AI REST API
 *
 * @author Krishna Prasad A
 * @since 31-03-2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiTranslationServiceImpl implements GeminiTranslationService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    @Override
    public String translateToMalayalam(String englishQuote) {
        log.info("[+] ഫംഗ്ഷൻ ഉദിച്ചുയരുന്നു : മലയാളത്തിലേക്ക് പരിവർത്തനം ചെയ്യുക() : ഇംഗ്ലീഷ് ഉദ്ധരണി -> {}", englishQuote);

        try {
            // Get API key from @Value or environment variable
            String key = apiKey;
            if (key == null || key.isEmpty()) {
                key = System.getenv("GEMINI_API_KEY");
            }

            // Check if API key is configured
            if (key == null || key.isEmpty()) {
                log.warn("[!] Gemini API Key is missing. Falling back to English quote.");
                return englishQuote;
            }

            log.debug("[*] Using Gemini API with key: {}...", key.substring(0, Math.min(10, key.length())));

            // Build Gemini API request
            Map<String, Object> requestBody = buildGeminiRequest(englishQuote);

            // Make HTTP request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String url = GEMINI_API_URL + "?key=" + key;
            log.debug("[*] Calling Gemini API: {}", GEMINI_API_URL);
            
            String response = restTemplate.postForObject(url, request, String.class);
            
            log.debug("[*] Gemini response: {}", response);

            // Extract translated text from response
            String malayalamQuote = extractTextFromResponse(response);

            log.info("[+] സാഫല്യം : മലയാളത്തിലേക്ക് പരിവർത്തനം ചെയ്യുക() : മലയാളം ഉദ്ധരണി -> {}", malayalamQuote);

            return malayalamQuote;
        } catch (Exception e) {
            log.error("[-] Gemini പരിവർത്തനം പരാജയപ്പെട്ടു: {}", e.getMessage(), e);
            log.warn("[!] യഥാർത്ഥ ഇംഗ്ലീഷ് ഉദ്ധരണി തിരികെ നൽകുന്നു");
            return englishQuote;
        }
    }

    private Map<String, Object> buildGeminiRequest(String quote) {
        Map<String, Object> request = new HashMap<>();

        // Create contents
        Map<String, Object> part = new HashMap<>();
        part.put("text", "Translate the following English quote to Malayalam. Only return the translated quote, nothing else:\n\n\"" + quote + "\"");

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        request.put("contents", List.of(content));

        return request;
    }

    private String extractTextFromResponse(String response) throws Exception {
        JsonNode rootNode = objectMapper.readTree(response);
        
        // Navigate through the JSON structure
        JsonNode candidates = rootNode.path("candidates");
        if (candidates.isArray() && candidates.size() > 0) {
            JsonNode firstCandidate = candidates.get(0);
            JsonNode content = firstCandidate.path("content");
            JsonNode parts = content.path("parts");
            
            if (parts.isArray() && parts.size() > 0) {
                JsonNode textNode = parts.get(0).path("text");
                if (textNode.isTextual()) {
                    return textNode.asText().trim();
                }
            }
        }

        return "Unable to extract response";
    }
}
