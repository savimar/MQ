package ru.savimar.mq;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.savimar.mq.jms.Receiver;
import ru.savimar.mq.jms.Sender;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MqApplicationTests {

    @Autowired
    private Sender sender;

    @Autowired
    private Receiver receiver;

    @Test
    public void testReceive() throws Exception {
        sender.send("Hello Spring JMS ActiveMQ!");

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount()).isEqualTo(0);
    }
}