package org.springframework.samples.petclinic.rest.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.client.DefaultChatClient.DefaultChatClientRequestSpec;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
@RequestMapping("/api/ai")
public class AIRestController {
	
	private ChatClient chatClient;
	private ChatMemory chatMemory;
	private VectorStore vectorStoreAPI;
	private final String[] functionNames;
	private AiConfigProperties properties;
	 
	public AIRestController(
			ChatClient chatClient,
			ChatMemory chatMemory,
			@Qualifier("vectorStoreAPI") VectorStore vectorStoreAPI,
			String[] functionNames,
			AiConfigProperties properties) {
		
		super();
		this.chatClient = chatClient;
		this.chatMemory = chatMemory;
		this.vectorStoreAPI = vectorStoreAPI;
		this.functionNames = functionNames;
		this.properties = properties;
	}

	@GetMapping("/chat")
	public Map<String, String> chatCompletion(@RequestParam String message) {
		
		ChatClientRequestSpec chatClientRequestSpec = this.chatClient.prompt()
				.user(message);
		
		return Map.of(
				"completion", chatClientRequestSpec.call().content());
	}
	
	@PostMapping("/chat")
	public Map<String, String> chatCompletion(@RequestBody AIRequest aiRequest) {
		
		DefaultChatClientRequestSpec chatClientRequestSpec = (DefaultChatClientRequestSpec) this.chatClient.prompt()
				.user(aiRequest.message());
		
		if (Boolean.parseBoolean(aiRequest.useChatMemory())) {
			chatClientRequestSpec = (DefaultChatClientRequestSpec) chatClientRequestSpec.advisors(new PromptChatMemoryAdvisor(this.chatMemory));
			chatClientRequestSpec.getAdvisorParams().put(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, aiRequest.cid());
		}
		
		if ("app-control".equalsIgnoreCase(aiRequest.mode())) {
			
			// Añadimos las Tools
			chatClientRequestSpec.functions(this.functionNames);
			
			// Configuramos el prompt del systema
			String systemMessage = this.properties.getSystemMessages().get("app-control");
			chatClientRequestSpec.system(systemMessage);
		}
		
		
		if ("api-info".equalsIgnoreCase(aiRequest.mode())) {
			
			// Añadimos la base de datos del API
			chatClientRequestSpec.advisors(new QuestionAnswerAdvisor(this.vectorStoreAPI, SearchRequest.defaults()));
		}
		
		return Map.of(
				"completion", chatClientRequestSpec.call().content());
	}

}

record AIRequest (String message, String useChatMemory, String cid, String mode) {}

@Component
@ConfigurationProperties("ai")
@Data
class AiConfigProperties {

	private Map<String, String> systemMessages;
}