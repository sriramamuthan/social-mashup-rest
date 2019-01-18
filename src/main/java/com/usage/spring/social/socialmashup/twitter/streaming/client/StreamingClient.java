package com.usage.spring.social.socialmashup.twitter.streaming.client;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Scope(value = "singleton")
public class StreamingClient {

    @Value("${twitter.apiKey}")
    private String apiKey;

    @Value("${twitter.apiSecret}")
    private String apiSecret;

    @Value("${twitter.token}")
    private String token;

    @Value("${twitter.secret}")
    private String secret;

    private Map<String, Client> clients = new TreeMap<>();

    Map<String, BlockingQueue<String>> queues = new TreeMap<>();

    public Client addClient(String topic) throws InterruptedException {
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(Lists.newArrayList( topic));
        Authentication auth = new OAuth1(apiKey, apiSecret, token, secret);
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);
        queues.put(topic, queue);
        Client client = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue))
                .build();
        client.connect();
        return client;
    }

    public void streamClient(String topic) throws InterruptedException {
        if(this.clients.get(topic) !=null) return;
        if (this.clients.size() <= 3){
            clients.put(topic , addClient(topic));
        }else{
            String key = Lists.newArrayList(clients.keySet()).get(0);
            clients.get(key).stop();
            clients.remove(key);
            queues.clear();
            addClient(topic);
        }
    }

    public String consume(String topic)  {
        try {
            return queues.get(topic).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    @PreDestroy
    public void disconnect(){
        clients.values().forEach(client -> client.stop());
    }
}
