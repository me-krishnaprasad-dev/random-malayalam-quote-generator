package com.kp.random_malayalam_quote_generator.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.random_malayalam_quote_generator.client.QuoteClient;
import com.kp.random_malayalam_quote_generator.document.QuoteGeneratedCounter;
import com.kp.random_malayalam_quote_generator.dto.AdviceDTO;
import com.kp.random_malayalam_quote_generator.dto.QuoteResponseDTO;
import com.kp.random_malayalam_quote_generator.dto.SlipDTO;
import com.kp.random_malayalam_quote_generator.repository.QuoteGeneratedCounterRepository;
import com.kp.random_malayalam_quote_generator.service.GeminiTranslationService;
import com.kp.random_malayalam_quote_generator.service.QuoteGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kp.random_malayalam_quote_generator.constant.RandomQuoteGeneratorConstant.DEV;
import static com.kp.random_malayalam_quote_generator.constant.RandomQuoteGeneratorConstant.SERVICE_UNAVAILABLE;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuoteGeneratorServiceImpl implements QuoteGeneratorService {

    private final QuoteGeneratedCounterRepository quoteGeneratedCounterRepository;
    private final QuoteClient quoteClient;
    private final ObjectMapper objectMapper;
    private final GeminiTranslationService geminiTranslationService;

    @Override
    public QuoteResponseDTO getMyMalayalamQuote() {

        log.info("[+] ഫംഗ്ഷൻ ഉദിച്ചുയരുന്നു : എന്റെ മലയാളം ഉദ്ധരണി നൽകുക().");

        log.info("[+] പ്രവർത്തന വിവരങ്ങൾ : എന്റെ മലയാളം ഉദ്ധരണി നൽകുക() : ഉദ്ധരണി എണ്ണൽ ശേഖരം വിളിക്കുന്നു.");

        QuoteGeneratedCounter counter = quoteGeneratedCounterRepository
                .findById("clicks")
                .orElseGet(() -> new QuoteGeneratedCounter("clicks", 0));

        log.info("[+] പ്രവർത്തന വിവരങ്ങൾ : എന്റെ മലയാളം ഉദ്ധരണി നൽകുക() : ഉദ്ധരണി എണ്ണം -> {}.", counter);

        ResponseEntity<String> response = quoteClient.getRandomQuote("application/json");

        SlipDTO slipDTO = Optional.ofNullable(response.getBody())
                .map(body -> {
                    try {
                        JsonNode json = objectMapper.readTree(body);
                        return objectMapper.convertValue(json, SlipDTO.class);
                    } catch (Exception e) {
                        log.error("[-] JSON പരിവർത്തനം പരാജയപ്പെട്ടു: {}", e.getMessage());
                        return null;
                    }
                })
                .orElse(null);

        String englishQuote = Optional.ofNullable(slipDTO)
                .map(SlipDTO::getSlip)
                .map(AdviceDTO::getAdvice)
                .orElse(SERVICE_UNAVAILABLE);

        // Translate to Malayalam using Gemini AI
        log.info("[+] Gemini ഉപയോഗിച്ച് മലയാളത്തിലേക്ക് പരിവർത്തനം ചെയ്യുന്നു...");
        String malayalamQuote = geminiTranslationService.translateToMalayalam(englishQuote);

        counter.incrementCount();
        quoteGeneratedCounterRepository.save(counter);

        QuoteResponseDTO quoteResponse = new QuoteResponseDTO(malayalamQuote, counter.getCount(), DEV);

        log.info("[+] പ്രവർത്തനം പൂർത്തിയായി : എന്റെ മലയാളം ഉദ്ധരണി നൽകുക() : ഉദ്ധരണി -> {}.", quoteResponse);

        return quoteResponse;
    }
}
