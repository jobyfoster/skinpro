package dev.jobyfoster.skinpro.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.KeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private OpenAIClient client;

    public OpenAIServiceImpl(@Value("${OPENAI_API_KEY}") String openAiApiKey) {
        this.client = new OpenAIClientBuilder()
                .credential(new KeyCredential(openAiApiKey))
                .buildClient();
    }

    public void generateSkincareRoutine(String userQuery) {
        try {
            List<ChatRequestMessage> chatMessages = new ArrayList<>();
            chatMessages.add(new ChatRequestSystemMessage("You are a skincare expert."));
            chatMessages.add(new ChatRequestUserMessage(userQuery));

            ChatCompletions chatCompletions = client.getChatCompletions("gpt-4-0125-preview",
                    new ChatCompletionsOptions(chatMessages));

            for (ChatChoice choice : chatCompletions.getChoices()) {
                ChatResponseMessage message = choice.getMessage();
                // Replace System.out.println with appropriate logging
                System.out.println("Generated Skincare Routine:");
                System.out.println(message.getContent());
            }
        } catch (Exception e) {
            // Replace System.out.println with appropriate logging
            System.out.println("Failed to generate skincare routine");
        }
    }
}
