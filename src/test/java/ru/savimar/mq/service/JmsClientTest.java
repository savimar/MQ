package ru.savimar.mq.service;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;
import ru.savimar.mq.jms.Receiver;
import ru.savimar.mq.jms.Sender;
import ru.savimar.mq.repository.IPersonRepository;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
class JmsClientTest {

    private String xml1 = null;
    private String xml2 = null;
    private String emptyXml = "";
    @Mock
    IPersonRepository repository;

    @Autowired
    IXMLService xmlService;

    @BeforeEach
    void setUp() {
        xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ns1:CoordinateMessage xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns1=\"http://asguf.mos.ru/rkis_gu/coordinate/v6_1/\" xsi:schemaLocation=\"http://asguf.mos.ru/rkis_gu/coordinate/v6_1/ file:///C:/Users/Maria/Downloads/ServiceV6_1_9.xsd\">\n" +
                "\t<ns1:CoordinateDataMessage>\n" +
                "\t\t<ns1:Service>\n" +
                "\t\t\t<ns1:RegNum>198676269</ns1:RegNum>\n" +
                "\t\t\t<ns1:RegDate>2020-03-23T14:30:30+03:00</ns1:RegDate>\n" +
                "\t\t\t<ns1:ServiceNumber>0001-9000003-067201-0456796/20</ns1:ServiceNumber>\n" +
                "\t\t\t<ns1:ServicePrice xsi:nil=\"true\"/>\n" +
                "\t\t\t<ns1:PrepareTargetDate xsi:nil=\"true\"/>\n" +
                "\t\t\t<ns1:OutputTargetDate xsi:nil=\"true\"/>\n" +
                "\t\t\t<ns1:Responsible>\n" +
                "\t\t\t\t<ns1:LastName>оператор Портала</ns1:LastName>\n" +
                "\t\t\t\t<ns1:FirstName>оператор Портала</ns1:FirstName>\n" +
                "\t\t\t\t<ns1:MiddleName>оператор Портала</ns1:MiddleName>\n" +
                "\t\t\t\t<ns1:JobTitle>оператор Портала</ns1:JobTitle>\n" +
                "\t\t\t\t<ns1:Phone>+7 (495) 539-55-55</ns1:Phone>\n" +
                "\t\t\t\t<ns1:Email>cpgu@mos.ru</ns1:Email>\n" +
                "\t\t\t</ns1:Responsible>\n" +
                "\t\t\t<ns1:Department>\n" +
                "\t\t\t\t<ns1:Name>Департамент информационных технологий города Москвы</ns1:Name>\n" +
                "\t\t\t\t<ns1:Code>2043</ns1:Code>\n" +
                "\t\t\t\t<ns1:Inn>7710878000</ns1:Inn>\n" +
                "\t\t\t\t<ns1:Ogrn>1107746943347</ns1:Ogrn>\n" +
                "\t\t\t\t<ns1:RegDate>2010-11-18T00:00:00</ns1:RegDate>\n" +
                "\t\t\t\t<ns1:SystemCode>9000003</ns1:SystemCode>\n" +
                "\t\t\t</ns1:Department>\n" +
                "\t\t\t<ns1:CreatedByDepartment>\n" +
                "\t\t\t\t<ns1:Name>ПГУ</ns1:Name>\n" +
                "\t\t\t\t<ns1:Code>1</ns1:Code>\n" +
                "\t\t\t\t<ns1:Inn>7710878000</ns1:Inn>\n" +
                "\t\t\t\t<ns1:Ogrn>1107746943347</ns1:Ogrn>\n" +
                "\t\t\t\t<ns1:RegDate>2009-11-18T00:00:00</ns1:RegDate>\n" +
                "\t\t\t\t<ns1:SystemCode>1</ns1:SystemCode>\n" +
                "\t\t\t</ns1:CreatedByDepartment>\n" +
                "\t\t\t<ns1:PrepareFactDate>2020-03-23T14:30:31+03:00</ns1:PrepareFactDate>\n" +
                "\t\t\t<ns1:OutputKind>Portal</ns1:OutputKind>\n" +
                "\t\t\t<ns1:PortalNum>198676269</ns1:PortalNum>\n" +
                "\t\t</ns1:Service>\n" +
                "\t\t<ns1:SignService Id=\"aa77d645-be22-498f-86da-8e85285ca970\">\n" +
                "\t\t\t<ns1:ServiceType>\n" +
                "\t\t\t\t<ns1:Code>067201</ns1:Code>\n" +
                "\t\t\t\t<ns1:Name>Подача запроса на предоставление доступа к электронной медицинской карте</ns1:Name>\n" +
                "\t\t\t</ns1:ServiceType>\n" +
                "\t\t\t<ns1:Copies>1</ns1:Copies>\n" +
                "\t\t\t<ns1:Contacts>\n" +
                "\t\t\t\t<ns1:BaseDeclarant Id=\"declarant\" xsi:type=\"ns1:RequestContact\">\n" +
                "\t\t\t\t\t<ns1:Type>Declarant</ns1:Type>\n" +
                "\t\t\t\t\t<ns1:Documents>\n" +
                "\t\t\t\t\t\t<ns1:ServiceDocument>\n" +
                "\t\t\t\t\t\t\t<ns1:DocKind>\n" +
                "\t\t\t\t\t\t\t\t<ns1:Code>20001</ns1:Code>\n" +
                "\t\t\t\t\t\t\t\t<ns1:Name>Фотография заявителя с паспортом РФ</ns1:Name>\n" +
                "\t\t\t\t\t\t\t</ns1:DocKind>\n" +
                "\t\t\t\t\t\t\t<ns1:DocSubType>1</ns1:DocSubType>\n" +
                "\t\t\t\t\t\t\t<ns1:DocSerie>4510</ns1:DocSerie>\n" +
                "\t\t\t\t\t\t\t<ns1:DocNumber>761268</ns1:DocNumber>\n" +
                "\t\t\t\t\t\t\t<ns1:DocDate>2010-07-30T00:00:00</ns1:DocDate>\n" +
                "\t\t\t\t\t\t\t<ns1:WhoSign>ОТД. ПО Р-НУ БУТЫРСКИЙ ОУФМС РОССИИ ПО Г. МОСКВЕ В СВАО</ns1:WhoSign>\n" +
                "\t\t\t\t\t\t\t<ns1:DivisionCode>770-079</ns1:DivisionCode>\n" +
                "\t\t\t\t\t\t\t<ns1:DocFiles>\n" +
                "\t\t\t\t\t\t\t\t<ns1:CoordinateFileReference>\n" +
                "\t\t\t\t\t\t\t\t\t<ns1:Id>7a5516d5-250d-4829-8d76-8ecef2ccdc68</ns1:Id>\n" +
                "\t\t\t\t\t\t\t\t\t<ns1:FileName/>\n" +
                "\t\t\t\t\t\t\t\t\t<ns1:FileHash/>\n" +
                "\t\t\t\t\t\t\t\t</ns1:CoordinateFileReference>\n" +
                "\t\t\t\t\t\t\t</ns1:DocFiles>\n" +
                "\t\t\t\t\t\t</ns1:ServiceDocument>\n" +
                "\t\t\t\t\t</ns1:Documents>\n" +
                "\t\t\t\t\t<ns1:LastName>Обухова</ns1:LastName>\n" +
                "\t\t\t\t\t<ns1:FirstName>Ольга</ns1:FirstName>\n" +
                "\t\t\t\t\t<ns1:MiddleName>Владимировна</ns1:MiddleName>\n" +
                "\t\t\t\t\t<ns1:Gender xsi:nil=\"true\"/>\n" +
                "\t\t\t\t\t<ns1:BirthDate>1978-07-14</ns1:BirthDate>\n" +
                "\t\t\t\t\t<ns1:Snils>057-862-052 86</ns1:Snils>\n" +
                "\t\t\t\t\t<ns1:MobilePhone>79265893133</ns1:MobilePhone>\n" +
                "\t\t\t\t\t<ns1:CitizenshipType xsi:nil=\"true\"/>\n" +
                "\t\t\t\t\t<ns1:OMSNum>7703008204140778</ns1:OMSNum>\n" +
                "\t\t\t\t\t<ns1:SsoId>73033f91-195f-419a-9328-c6a4a7ce6a4c</ns1:SsoId>\n" +
                "\t\t\t\t</ns1:BaseDeclarant>\n" +
                "\t\t\t\t<ns1:BaseDeclarant Id=\"contact\" xsi:type=\"ns1:RequestContact\">\n" +
                "\t\t\t\t\t<ns1:Type>Child</ns1:Type>\n" +
                "\t\t\t\t\t<ns1:LastName>Обухов</ns1:LastName>\n" +
                "\t\t\t\t\t<ns1:FirstName>Ростислав</ns1:FirstName>\n" +
                "\t\t\t\t\t<ns1:MiddleName>Дмитриевич</ns1:MiddleName>\n" +
                "\t\t\t\t\t<ns1:Gender xsi:nil=\"true\"/>\n" +
                "\t\t\t\t\t<ns1:BirthDate>2010-12-29</ns1:BirthDate>\n" +
                "\t\t\t\t\t<ns1:CitizenshipType xsi:nil=\"true\"/>\n" +
                "\t\t\t\t\t<ns1:OMSNum>7700003021795210</ns1:OMSNum>\n" +
                "\t\t\t\t</ns1:BaseDeclarant>\n" +
                "\t\t\t</ns1:Contacts>\n" +
                "\t\t\t<ns1:CustomAttributes>\n" +
                "\t\t\t\t<ServiceProperties>\n" +
                "\t\t\t\t\t<confirmed>false</confirmed>\n" +
                "\t\t\t\t\t<information>true</information>\n" +
                "\t\t\t\t\t<packetnum>ЭМК-0052792/20</packetnum>\n" +
                "\t\t\t\t\t<type>gu</type>\n" +
                "\t\t\t\t\t<lastname>Обухов</lastname>\n" +
                "\t\t\t\t\t<firstname>Ростислав</firstname>\n" +
                "\t\t\t\t\t<middlename>Дмитриевич</middlename>\n" +
                "\t\t\t\t\t<birthdate>2010-12-29T00:00:00</birthdate>\n" +
                "\t\t\t\t\t<omsnum>7700003021795210</omsnum>\n" +
                "\t\t\t\t\t<cctcode>46074</cctcode>\n" +
                "\t\t\t\t\t<careeventid>732812</careeventid>\n" +
                "\t\t\t\t\t<careeventstarted>2020-03-23T14:30:27</careeventstarted>\n" +
                "\t\t\t\t\t<simiversion>4</simiversion>\n" +
                "\t\t\t\t\t<authorlogin>MPGU</authorlogin>\n" +
                "\t\t\t\t\t<status>CREATED</status>\n" +
                "\t\t\t\t\t<patientid>19293590</patientid>\n" +
                "\t\t\t\t\t<cctschema>ru.emias.simi.terminology.cct</cctschema>\n" +
                "\t\t\t\t\t<cctdescription>Заявление на предоставление доступа к ЭМК</cctdescription>\n" +
                "\t\t\t\t\t<mimeclass>APPLICATION</mimeclass>\n" +
                "\t\t\t\t\t<mimetype>application/xml</mimetype>\n" +
                "\t\t\t\t\t<documentid>d313ea78-6e16-4edc-9521-a48249ae15c2</documentid>\n" +
                "\t\t\t\t\t<documentcreated>2020-03-23T14:30:27</documentcreated>\n" +
                "\t\t\t\t</ServiceProperties>\n" +
                "\t\t\t</ns1:CustomAttributes>\n" +
                "\t\t</ns1:SignService>\n" +
                "\t</ns1:CoordinateDataMessage>\n" +
                "</ns1:CoordinateMessage>\n";


        xml2 = "<ns1:CoordinateMessage xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns1=\"http://asguf.mos.ru/rkis_gu/coordinate/v6_1/\"><ns1:CoordinateDataMessage><ns1:Service><ns1:RegNum>198678624</ns1:RegNum><ns1:RegDate>2020-03-23T14:55:19+03:00</ns1:RegDate><ns1:ServiceNumber>0001-9000003-067201-0456929/20</ns1:ServiceNumber><ns1:ServicePrice xsi:nil=\"true\"/><ns1:PrepareTargetDate xsi:nil=\"true\"/><ns1:OutputTargetDate xsi:nil=\"true\"/><ns1:Responsible><ns1:LastName>оператор Портала</ns1:LastName><ns1:FirstName>оператор Портала</ns1:FirstName><ns1:MiddleName>оператор Портала</ns1:MiddleName><ns1:JobTitle>оператор Портала</ns1:JobTitle><ns1:Phone>+7 (495) 539-55-55</ns1:Phone><ns1:Email>cpgu@mos.ru</ns1:Email></ns1:Responsible><ns1:Department><ns1:Name>Департамент информационных технологий города Москвы</ns1:Name><ns1:Code>2043</ns1:Code><ns1:Inn>7710878000</ns1:Inn><ns1:Ogrn>1107746943347</ns1:Ogrn><ns1:RegDate>2010-11-18T00:00:00</ns1:RegDate><ns1:SystemCode>9000003</ns1:SystemCode></ns1:Department><ns1:CreatedByDepartment><ns1:Name>ПГУ</ns1:Name><ns1:Code>1</ns1:Code><ns1:Inn>7710878000</ns1:Inn><ns1:Ogrn>1107746943347</ns1:Ogrn><ns1:RegDate>2009-11-18T00:00:00</ns1:RegDate><ns1:SystemCode>1</ns1:SystemCode></ns1:CreatedByDepartment><ns1:PrepareFactDate>2020-03-23T14:55:19+03:00</ns1:PrepareFactDate><ns1:OutputKind>Portal</ns1:OutputKind><ns1:PortalNum>198678624</ns1:PortalNum></ns1:Service><ns1:SignService Id=\"0c04cf47-01ff-40a3-a30b-eb0566ffb8a2\"><ns1:ServiceType><ns1:Code>067201</ns1:Code><ns1:Name>Подача запроса на предоставление доступа к электронной медицинской карте</ns1:Name></ns1:ServiceType><ns1:Copies>1</ns1:Copies><ns1:Contacts><ns1:BaseDeclarant Id=\"declarant\" xsi:type=\"ns1:RequestContact\"><ns1:Type>Declarant</ns1:Type><ns1:Documents><ns1:ServiceDocument><ns1:DocKind><ns1:Code>20001</ns1:Code><ns1:Name>Фотография заявителя с паспортом РФ</ns1:Name></ns1:DocKind><ns1:DocSubType>1</ns1:DocSubType><ns1:DocSerie>4602</ns1:DocSerie><ns1:DocNumber>656855</ns1:DocNumber><ns1:DocDate>2002-04-13T00:00:00</ns1:DocDate><ns1:WhoSign>пушкинским городским отделом милиции Московской области</ns1:WhoSign><ns1:DivisionCode>502-050</ns1:DivisionCode><ns1:DocFiles><ns1:CoordinateFileReference><ns1:Id>c446cfa6-65d3-493a-b6d8-2ef70bcd93a7</ns1:Id><ns1:FileName/><ns1:FileHash/></ns1:CoordinateFileReference></ns1:DocFiles></ns1:ServiceDocument></ns1:Documents><ns1:LastName>Егунова</ns1:LastName><ns1:FirstName>Оксана</ns1:FirstName><ns1:MiddleName>Юрьевна</ns1:MiddleName><ns1:Gender xsi:nil=\"true\"/><ns1:BirthDate>1977-02-03</ns1:BirthDate><ns1:Snils>093-620-690 74</ns1:Snils><ns1:MobilePhone>79165195330</ns1:MobilePhone><ns1:CitizenshipType xsi:nil=\"true\"/><ns1:OMSNum>5057220896001976</ns1:OMSNum><ns1:SsoId>7ac0da27-f48c-4da4-8d6c-4eaa6e403b15</ns1:SsoId></ns1:BaseDeclarant></ns1:Contacts><ns1:CustomAttributes><ServiceProperties><confirmed>false</confirmed><information>true</information><type>gu</type><cctcode>46074</cctcode><careeventid>732812</careeventid><careeventstarted>2020-03-23T14:55:13</careeventstarted><simiversion>4</simiversion><authorlogin>MPGU</authorlogin><status>CREATED</status><patientid>2245473898</patientid><cctschema>ru.emias.simi.terminology.cct</cctschema><cctdescription>Заявление на предоставление доступа к ЭМК</cctdescription><mimeclass>APPLICATION</mimeclass><mimetype>application/xml</mimetype><documentid>086bb595-47a1-46c8-b862-9c0423962028</documentid><documentcreated>2020-03-23T14:55:13</documentcreated></ServiceProperties></ns1:CustomAttributes></ns1:SignService></ns1:CoordinateDataMessage><ns1:Files><ns1:CoordinateFile><ns1:Id>c446cfa6-65d3-493a-b6d8-2ef70bcd93a7</ns1:Id><ns1:FileIdInStore>F76EBB34-579D-4D9F-99DB-EF6EA312C88D</ns1:FileIdInStore><ns1:FileName>Без имени-1.jpg</ns1:FileName><ns1:CmsSignature/><ns1:FileHash/></ns1:CoordinateFile></ns1:Files></ns1:CoordinateMessage>\n";

    }

    @AfterEach
    void tearDown() {
        xml1 = null;
        xml2 = null;
    }

    @Test
    void sendXml1() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        List<String> middleNames = xmlService.parseSax(xml1);
        List<String> lastNames = xmlService.parseXPath(xml1);
        List<String> firstNames = xmlService.parseStax(xml1);
        assertEquals(firstNames.toString() + " " + middleNames.toString() + " "+ lastNames.toString(), "[оператор Портала, Ольга, Ростислав] [оператор Портала, Владимировна, Дмитриевич] [оператор Портала, Обухова, Обухов]");
    }

    @Test
    void receive() {
    }
}