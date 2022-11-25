package pdf.formatter.presentation;

import java.util.Date;
import javax.persistence.Lob;

public class OrderResultPo {

    @Lob
    String DataAsBase64; // b64
    Date CreationDate;
    String State;

    public OrderResultPo() {
    }

    public OrderResultPo(String b64Data, String state) {

        this.DataAsBase64 = b64Data;
        this.State = state;
        this.CreationDate = new Date();
    }

    public void setCreationDate(Date date) {
        this.CreationDate = date;
    }

    public Date getCreationDate() {
        return this.CreationDate;
    }

    public String getState() {
        return this.State;
    }

    public void setDataAsBase64(String base64StringArray) {
        this.DataAsBase64 = base64StringArray;
    }

    public String getDataAsBase64() {
        return this.DataAsBase64;
    }

    public void setState(String state) {
        this.State = state;
    }

}
