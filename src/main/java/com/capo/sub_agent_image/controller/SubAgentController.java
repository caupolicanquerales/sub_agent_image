package com.capo.sub_agent_image.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.capo.sub_agent_image.request.GenerationSyntheticDataRequest;
import com.capo.sub_agent_image.service.ExecutingPromptImageService;
import com.capo.sub_agent_image.utils.SseStreamUtil;


@RestController
@RequestMapping("sub-agent-image")
@CrossOrigin(origins = "${app.frontend.url}")
public class SubAgentController {
	
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final ExecutingPromptImageService executingImage;
	
	@Value(value="${event.name.image}")
	private String eventName;
	
	public SubAgentController(ExecutingPromptImageService executingImage) {
		this.executingImage= executingImage;
	}
	
	@PostMapping(path = "/stream-image", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamImageGeneration(@RequestBody GenerationSyntheticDataRequest request) {
        return SseStreamUtil.stream(executor, eventName, "Image generation started for prompt",
                () -> executingImage.generateImageAsync(request.getPrompt()));
	}
}
