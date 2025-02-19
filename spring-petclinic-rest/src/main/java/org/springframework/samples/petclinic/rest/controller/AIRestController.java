package org.springframework.samples.petclinic.rest.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIRestController {
	
	private ChatClient chatClient;
	
	public AIRestController(ChatClient chatClient) {
		super();
		this.chatClient = chatClient;
	}

	@GetMapping("/chat")
	public Map<String, String> chatCompletion(@RequestParam("message") String message) {
		
		ChatClientRequestSpec chatClientRequestSpec = this.chatClient.prompt()
				.user(message);
		
		return Map.of(
				"completion", chatClientRequestSpec.call().content());
	}

}

record AIRequest (String message) {}
