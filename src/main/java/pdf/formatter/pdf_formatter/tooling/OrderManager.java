package pdf.formatter.pdf_formatter.tooling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pdf.formatter.pdf_formatter.fop.PdfGenerator;

public class OrderManager {

    Logger Log = LoggerFactory.getLogger(PdfGenerator.class);

    public byte[] OrderPdfGenerate(String xmlData, String xslData) throws Exception {
        PdfGenerator generator = new PdfGenerator();

        byte[] resultDocumentData = generator.ExportToPDF(xmlData, xslData);
        Runtime.getRuntime().gc();
        return resultDocumentData;

    }

}
