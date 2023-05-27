package com.project.kafkasetup.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    
    @Value(value = "${spring.kafka.topic.name}")
	private String topicName;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/send")
    public void sendMessageToKafka(){
        String message = "Hello Test kafka";
        sendMessage(message);
    }

    public void sendMessage(String message) {
		CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				System.out.println("Sent message=[" + message + 
					"] with offset=[" + result.getRecordMetadata().offset() + "]");
			} else {
				System.out.println("Unable to send message=[" + 
					message + "] due to : " + ex.getMessage());
			}
		});
   }
   @KafkaListener(topics = "kafkaTopic", groupId = "kafkaConsumer")
public void listenGroupFoo(String message) {
    System.out.println("Received Message in group kafkaConsumer: " + message);
}
    
}
