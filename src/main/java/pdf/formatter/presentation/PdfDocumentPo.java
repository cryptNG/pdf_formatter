package pdf.formatter.presentation;

import java.util.Date;
import javax.persistence.Lob;

public class PdfDocumentPo {

    @Lob
    String DataAsBase64; // b64
    Date CreationDate;

    public PdfDocumentPo() {
    }

    public PdfDocumentPo(String b64Data) {

        this.DataAsBase64 = b64Data;
        this.CreationDate = new Date();
    }

    public void setCreationDate(Date date) {
        this.CreationDate = date;
    }

    public Date getCreationDate() {
        return this.CreationDate;
    }

    public void setDataAsBase64(String base64StringArray) {
        this.DataAsBase64 = base64StringArray;
    }

    public String getDataAsBase64() {
        return this.DataAsBase64;
    }

}
