package ru.savimar.mq.service;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public interface IJmsClient {
   String send(String msg) throws Exception;
   String receive();

}
