package ru.savimar.mq.service;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.savimar.mq.jms.Receiver;
import ru.savimar.mq.jms.Sender;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class JmsClient implements IJmsClient {
    @Autowired
    private Receiver receiver;
    @Autowired
    private Sender sender;
    @Autowired
    private IXMLService xmlService;
   // @PersistenceContext
 //   private EntityManager entityManager;


    private static final Logger LOGGER =
            LoggerFactory.getLogger(JmsClient.class);


    @Override
   @Transactional
    public String send(String msg) throws Exception {
      /*
        Session session = null;
        if (entityManager == null
                || (session = entityManager.unwrap(Session.class)) == null) {

            throw new NullPointerException();
        }

        //  session.beginTransaction();
         */

        /* pasreSax*/
        long middleNamesBegin = System.currentTimeMillis();

        List<String> middleNames = xmlService.parseSax(msg);
        long middleNamesEnd = System.currentTimeMillis();
        long middleNamesDelta = middleNamesEnd - middleNamesBegin;

        /* parseXPath*/
        long lastNamesBegin = System.currentTimeMillis();
        List<String> lastNames = xmlService.parseXPath(msg);
        long lastNamesEnd = System.currentTimeMillis();
        long lastNamesDelta = lastNamesEnd - lastNamesBegin;

        /*parseStax*/
        long firstNamesBegin = System.currentTimeMillis();
        List<String> firstNames = xmlService.parseStax(msg);
        long firstNamesEnd = System.currentTimeMillis();
        long firstNamesDelta = firstNamesEnd - firstNamesBegin;

        /*parseToDb*/
        long dbBegin = System.currentTimeMillis();
        xmlService.parseToDb(msg);
        long dbEnd = System.currentTimeMillis();
        long dbDelta = dbEnd - dbBegin;

       //  session.getTransaction().commit();
       //  session.close();
        boolean b = xmlService.checkXMLforXSD("C:\\Users\\Maria\\Downloads\\Telegram Desktop\\example61.xml", "C:\\Users\\Maria\\Downloads\\ServiceV6_1_9.xsd");
        System.out.println("XML соответствует XSD : " + b);

        String str = String.format("Имена: %s, отчества %s, фамилии %s. \nВыполнение parseSax %d, выполнение parseXPath %d, выполнение parseStax %d, выполнение parseToDb %d \nXML соответствует XSD : %s", firstNames.toString(), middleNames.toString(), lastNames.toString(), middleNamesDelta, lastNamesDelta, firstNamesDelta, dbDelta, b);
        sender.send(str);

        return str;
    }

    @Override
    @Transactional
    public String receive() {
        return receiver.receive();
    }

}
