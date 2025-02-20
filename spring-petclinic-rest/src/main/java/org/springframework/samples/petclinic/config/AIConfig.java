package org.springframework.samples.petclinic.config;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	@Bean
	String[] functionNames(ApplicationContext appCtx) {
		
		String[] functionNames = appCtx.getBeanNamesForType(Function.class);
		log.info(" => Function names: " + Arrays.toString(functionNames));
		
		functionNames = Stream.of(functionNames).filter(name -> name.endsWith("Function")).toList().toArray(new String[0]);
		log.info(" => Function names (filtrado): " + Arrays.toString(functionNames));
		
		return functionNames;
	}
}
