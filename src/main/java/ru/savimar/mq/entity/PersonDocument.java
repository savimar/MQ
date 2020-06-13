package ru.savimar.mq.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class PersonDocument {
    private long id;
    private Person person;
    private String docKindName;
    private int docSubType;
    private String docSerie;
    private String docNumber;
    private LocalDateTime docDate;
    private String whoSign;
    private String divisionCode;
    private String docFileId;


    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getDocKindName() {
        return docKindName;
    }

    public void setDocKindName(String docKindName) {
        this.docKindName = docKindName;
    }

    public int getDocSubType() {
        return docSubType;
    }

    public void setDocSubType(int docSubType) {
        this.docSubType = docSubType;
    }

    public String getDocSerie() {
        return docSerie;
    }

    public void setDocSerie(String docSerie) {
        this.docSerie = docSerie;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public LocalDateTime getDocDate() {
        return docDate;
    }

    public void setDocDate(LocalDateTime docDate) {
        this.docDate = docDate;
    }

    public String getWhoSign() {
        return whoSign;
    }

    public void setWhoSign(String whoSign) {
        this.whoSign = whoSign;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getDocFileId() {
        return docFileId;
    }

    public void setDocFileId(String docFileId) {
        this.docFileId = docFileId;
    }


}
