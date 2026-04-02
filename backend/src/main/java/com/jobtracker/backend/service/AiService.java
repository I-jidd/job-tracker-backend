package com.jobtracker.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.backend.dto.AiAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    public AiAnalysisResponse analyzeResume(String resumeText,
                                            String jobDescription) {

        String prompt = buildPrompt(resumeText, jobDescription);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.3
        );

        try {
            String response = webClientBuilder.build()
                    .post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseResponse(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to call Groq API: "
                    + e.getMessage());
        }
    }

    private String buildPrompt(String resumeText, String jobDescription) {
        return """
                Analyze this resume against the job description.
                Respond in exactly this JSON format with no extra text,
                no markdown, no code blocks:
                {
                  "matchScore": <number 0-100>,
                  "missingKeywords": "<comma separated missing keywords>",
                  "feedback": "<specific actionable feedback>"
                }
                
                RESUME:
                %s
                
                JOB DESCRIPTION:
                %s
                """.formatted(resumeText, jobDescription);
    }

    private AiAnalysisResponse parseResponse(String rawResponse) {
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String text = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Clean markdown if present
            text = text.replace("```json", "")
                    .replace("```", "")
                    .trim();

            JsonNode result = objectMapper.readTree(text);

            AiAnalysisResponse response = new AiAnalysisResponse();
            response.setMatchScore(result.path("matchScore").asInt());
            response.setMissingKeywords(
                    result.path("missingKeywords").asText());
            response.setFeedback(result.path("feedback").asText());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: "
                    + e.getMessage());
        }
    }
}