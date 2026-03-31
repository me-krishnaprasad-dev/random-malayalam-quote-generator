package com.kp.random_malayalam_quote_generator.controller.impl;

import com.kp.random_malayalam_quote_generator.controller.RandomQuoteGeneratorController;
import com.kp.random_malayalam_quote_generator.dto.QuoteResponseDTO;
import com.kp.random_malayalam_quote_generator.service.QuoteGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller
 * This controller is used for getting all the information related to quote
 *
 * @author Krishna Prasad A
 * @since 20-02-2025
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/quote")
@Slf4j
@CrossOrigin(origins = "*")
public class RandomQuoteGeneratorControllerImpl implements RandomQuoteGeneratorController {

    private final QuoteGeneratorService quoteGeneratorService;

    /**
     * This method is used to Random Generated Malayalam Quote
     *
     * @return Response Entity of QuoteResponseDTO
     */
    @Override
    @GetMapping("/random")
    public ResponseEntity<QuoteResponseDTO> getMyMalayalamQuote() {
        log.info("[+] ഫംഗ്ഷൻ ഉദിച്ചുയരുന്നു : എന്റെ മലയാളം ഉദ്ധരണി നൽകുക().");

        QuoteResponseDTO quoteResponse = quoteGeneratorService.getMyMalayalamQuote();

        log.info("[+] പ്രവർത്തനം പൂർത്തിയായി : എന്റെ മലയാളം ഉദ്ധരണി നൽകുക() : ഉദ്ധരണി -> {}.", quoteResponse);
        return ResponseEntity.ok(quoteResponse);
    }
}
