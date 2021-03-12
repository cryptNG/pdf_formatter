package pdf.formatter.pdf_formatter.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pdf.formatter.pdf_formatter.tooling.ResultType;

@Entity

@Table(name = "orders")
public class Order {

    private @Id @GeneratedValue Long ID;
    String requestId;
    ResultType resultType;

    // ConversionSource Source /HTML/XMLXSL/FO Make possible to deliver fo document
    // instead of xsl

    @JsonIgnore
    String state;
    @JsonIgnore
    Date creationDate;
    @JsonIgnore
    Date documentCreationDate;
    @JsonIgnore
    @Lob
    byte[] documentData;

    public Order() {
    }

    public Order(ResultType expectedResultType, String requestId) {
        this.resultType = expectedResultType;
        this.requestId = requestId;
        this.state = "Created";
        this.creationDate = new Date();
    }

    public Order(ResultType expectedResultType, String requestId, Date documentCreationDate) {
        this.documentCreationDate = documentCreationDate;
        this.resultType = expectedResultType;
        this.requestId = requestId;
        this.state = "Created";
        this.creationDate = new Date();
    }

    public void setDocumentData(byte[] documentBytes) {
        this.documentData = documentBytes;
    }

    public byte[] getDocumentData() {
        return this.documentData;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    public void setDocumentCreationDate(Date date) {
        this.documentCreationDate = date;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public Long getId() {
        return this.ID;
    }

    public void setId(Long id) {
        this.ID = id;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Date getDocumentCreationDate() {
        return this.documentCreationDate;
    }

}