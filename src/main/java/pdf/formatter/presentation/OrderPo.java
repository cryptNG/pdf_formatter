package pdf.formatter.presentation;

import pdf.formatter.pdf_formatter.tooling.ResultType;

public class OrderPo {

    public String RequestId;
    public String XmlData;
    public String XslData;
    public ResultType ResultType;

    // ConversionSource Source /HTML/XMLXSL/FO Make possible to deliver fo document
    // instead of xsl

    public OrderPo() {
    }

}