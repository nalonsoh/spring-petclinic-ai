package org.springframework.samples.petclinic.config;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class StoreLoader {

	public StoreLoader(@Value("classpath:openapi.yml") Resource jsonApiResource, @Qualifier("vectorStoreAPI") VectorStore vectorStoreAPI) throws IOException {
		super();
		//this.loadAPIStore(jsonApiResource, vectorStoreAPI);
	}

	private void loadAPIStore(Resource jsonApiResource, VectorStore vectorStoreAPI) throws IOException {
		log.info("Cargando info de API en BDD de Spring AI...");
		log.info("Resource: {}", jsonApiResource.getURI());

		TextReader reader = new TextReader(jsonApiResource);
		List<Document> documents = reader.get();
		log.info("{} documentos encontrados.", documents.size());
		
		var textSplitter = new TokenTextSplitter(
				800 * 4,// defaultChunkSize: The target size of each text chunk in tokens
				350, 	// minChunkSizeChars: The minimum size of each text chunk in characters
				5, 		// minChunkLengthToEmbed: Discard chunks shorter than this
				10000, 	// maxNumChunks: The maximum number of chunks to generate from a text
				true 	//keepSeparator
			);
		List<Document> chunks = textSplitter.apply(documents);
		log.info("{} fragmentos creados.", chunks.size());
		
		vectorStoreAPI.add(chunks);
		
		log.info("Carga de info de API completada.");
	}
}
