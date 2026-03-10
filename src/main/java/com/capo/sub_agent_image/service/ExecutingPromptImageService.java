package com.capo.sub_agent_image.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ExecutingPromptImageService {
	
	private final ImageModel imageModel;
	
	@Value(value="${image.model.name}")
	private String imageModelName;
	
	public ExecutingPromptImageService(@Qualifier("customOpenAiImageModel") ImageModel imageModel) {
		this.imageModel=imageModel;
	}
	
	
	public CompletableFuture<String> generateImageAsync(String prompt) {
		return CompletableFuture.supplyAsync(() -> {
			var options = OpenAiImageOptions.builder()
					.model(imageModelName)
					.build();

			ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
			ImageResponse response = imageModel.call(imagePrompt);

			return response.getResult().getOutput().getB64Json();
		});
	}
}

