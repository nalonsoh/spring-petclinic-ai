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
import org.springframework.ai.chat.model.ChatResponse;
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
	public Map<String, String> chatCompletion(@RequestParam String message) {
		String chatResponse = this.chatClient.prompt()
		    .user(message)
		    .call()
		    .content();
		
		return Map.of("completion", chatResponse);
	}
}

```
