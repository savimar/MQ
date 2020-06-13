package ru.savimar.mq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import ru.savimar.mq.service.IJmsClient;
import ru.savimar.mq.service.IXMLService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

@RestController
public class MqController {
    @Autowired
    IJmsClient jsmClient;


    @RequestMapping(value = "/receive")
    public String receive() {
        return jsmClient.receive();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/xml",
            headers = "Accept=application/xml")
    public @ResponseBody
    String createXML(@RequestBody String xml) throws Exception {
         return  jsmClient.send(xml);
    }
}
