package dev.jobyfoster.skinpro.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.KeyCredential;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final OpenAIClient client;
    public OpenAIServiceImpl() {
        Dotenv dotenv = Dotenv.load(); // Load the .env file
        String apiKey = dotenv.get("OPENAI_API_KEY"); // Access the OPENAI_API_KEY variable
        this.client = new OpenAIClientBuilder()
                .credential(new KeyCredential(apiKey))
                .httpClient(new NettyAsyncHttpClientBuilder().responseTimeout(Duration.ofMinutes(2)).build())
                .buildClient();
    }

    public String generateSkincareRoutine(String userQuery) {
        String systemPrompt = """
Craft a day and night skincare routine tailored to user needs, segmented into steps. Each step should include a sequence number, action description, and product suggestion. Ensure routines address goals like cleansing and moisturizing.
Format response as a JSON object with 'day' and 'night' keys, each containing a 'steps' array. For 'steps', include:
stepNumber: Sequence of the step.
description: Purpose and application method.
productRecommendation: { "productName": "...", "descriptor": "..." }
Aim for concise, practical advice with real product examples.
""";
        try {
            List<ChatRequestMessage> chatMessages = List.of(
                    new ChatRequestSystemMessage(systemPrompt),
                    new ChatRequestUserMessage(userQuery));

            ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
            chatCompletionsOptions.setResponseFormat(new ChatCompletionsJsonResponseFormat());
            ChatCompletions chatCompletions = client.getChatCompletions("gpt-4-0125-preview", chatCompletionsOptions);

            return chatCompletions.getChoices().stream()
                    .map(choice -> choice.getMessage().getContent())
                    .findFirst()
                    .orElse("Routine generation failed. Please try again later.");
        } catch (Exception e) {
            // Log the error for debugging purposes
            System.out.println(e.getMessage());
            return "An error occurred while generating the skincare routine. Please try again later.";
        }

    }
}
