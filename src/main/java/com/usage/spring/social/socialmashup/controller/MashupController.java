package com.usage.spring.social.socialmashup.controller;

import com.usage.spring.social.socialmashup.twitter.streaming.client.StreamingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@RestController
public class MashupController {

    @Autowired
    private StreamingClient streamingClient;

    @GetMapping("/ping")
    public String ping(){
        return "Success";
    }

    @GetMapping(value = "/twitter/trends", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public Flux<String> trends(@RequestParam("topic") String topic) throws InterruptedException {
        if(StringUtils.isEmpty(topic) || topic.length() < 5) throw new RuntimeException("Topic is mandatory");
        streamingClient.streamClient(topic);
        return Flux.fromStream(Stream.generate(() -> streamingClient.consume(topic))
                .limit(1));
    }

}
