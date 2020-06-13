package ru.savimar.mq.service;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

public interface IXMLService {
    List<String> parseXPath(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException;
    List<String> parseSax(String xml) throws ParserConfigurationException, IOException, SAXException;
    List<String> parseStax(String xml);
    boolean checkXMLforXSD(String s, String s1) throws Exception;
    void parseToDb(String msg) throws XPathExpressionException;
}
