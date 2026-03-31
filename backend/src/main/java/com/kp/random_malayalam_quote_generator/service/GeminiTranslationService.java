package com.kp.random_malayalam_quote_generator.service;

/**
 * Gemini Translation Service Interface
 * This service will handle translation of quotes to Malayalam using Google Gemini
 *
 * @author Krishna Prasad A
 * @since 31-03-2026
 */
public interface GeminiTranslationService {

    /**
     * Translate a quote to Malayalam using Google Gemini AI
     *
     * @param englishQuote the quote to translate
     * @return translated quote in Malayalam
     */
    String translateToMalayalam(String englishQuote);
}
