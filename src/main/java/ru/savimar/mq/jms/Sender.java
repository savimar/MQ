package ru.savimar.mq.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

public class Sender {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Sender.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    public Sender() {
      // this.jmsTemplate.setSessionTransacted(true);
    }



    @Value("${jms.queue.destination}")
    String destinationQueue;

    public void send(String message) {


        LOGGER.info("sending message='{}'", message);
        jmsTemplate.convertAndSend(destinationQueue, message);
    }
}