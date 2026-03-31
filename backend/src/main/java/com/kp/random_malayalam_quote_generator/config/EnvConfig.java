package com.kp.random_malayalam_quote_generator.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void loadEnvVariables() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        
        String mongodbUri = dotenv.get("MONGODB_URI");
        if (mongodbUri != null) {
            System.setProperty("spring.data.mongodb.uri", mongodbUri);
        }
        
        String geminiApiKey = dotenv.get("GEMINI_API_KEY");
        if (geminiApiKey != null) {
            System.setProperty("gemini.api.key", geminiApiKey);
        }
    }
}
