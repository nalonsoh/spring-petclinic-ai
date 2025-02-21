```
<!-- Spring AI -->
<spring-ai.version>1.0.0-M2</spring-ai.version>

<repositories>
		<repository>
			<id>spring-milestones</id>
			<name>Spring Milestones</name>
			<url>https://repo.spring.io/milestone</url>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>
		<repository>
			<id>spring-snapshots</id>
			<name>Spring Snapshots</name>
			<url>https://repo.spring.io/snapshot</url>
			<releases>
				<enabled>false</enabled>
			</releases>
		</repository>
	</repositories>

 	<dependencyManagement>
  		<dependencies>
			<!-- Spring AI -->
			<dependency>
				<groupId>org.springframework.ai</groupId>
				<artifactId>spring-ai-bom</artifactId>
				<version>${spring-ai.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```
properties:
```
spring.ai:
  openai:
    api-key: ${OPENAI_API_KEY}
    chat.options.model: gpt-4o-mini
    embedding:
      enabled: true
      options.model: text-embedding-3-small
```

Dependencia para arreglar swagger-ui
```
       <dependency>
		    <groupId>io.swagger.core.v3</groupId>
		    <artifactId>swagger-annotations</artifactId>
		    <version>2.2.16</version>
		</dependency>
```
Controlador:
```
package org.springframework.samples.petclinic.rest.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.client.DefaultChatClient.DefaultChatClientRequestSpec;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIRestController {
	
	private ChatClient chatClient;
	private ChatMemory chatMemory;
	
	public AIRestController(ChatClient chatClient, ChatMemory chatMemory) {
		super();
		this.chatClient = chatClient;
		this.chatMemory = chatMemory;
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
		
		return Map.of(
				"completion", chatClientRequestSpec.call().content());
	}

}

record AIRequest (String message, String useChatMemory, String cid) {}

```
"marked": "^15.0.7",

```
package org.springframework.samples.petclinic.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AIConfig {

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}
	
	@Bean
	ChatMemory chatMemory() {
		return new InMemoryChatMemory();
	}
}

```
