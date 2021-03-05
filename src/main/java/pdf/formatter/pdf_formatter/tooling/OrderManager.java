package pdf.formatter.pdf_formatter.tooling;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdf.formatter.pdf_formatter.entities.Order;
import pdf.formatter.pdf_formatter.fop.PdfGenerator;
import pdf.formatter.pdf_formatter.repositories.OrderRepository;

public class OrderManager {

    Logger Log = LoggerFactory.getLogger(PdfGenerator.class);

    public void OrderPdfGenerate(Order newOrder, OrderRepository repository) {
        PdfGenerator generator = new PdfGenerator();
        try {
            byte[] resultDocumentData = generator.ExportToPDF(newOrder.getXmlData(), newOrder.getXslData());

            newOrder.setDocumentData(resultDocumentData);
            newOrder.setDocumentCreationDate(new Date());
            Log.info("Document generated for " + newOrder.getRequestId());
            newOrder.setState("Finished");
        } catch (Exception ex) {
            Log.error("Could not create document due to exception: " + ex.getMessage());
            ex.printStackTrace();
            newOrder.setState("Error");
        }
        try {

            Log.info("Order " + newOrder.getState());
            repository.save(newOrder);
        } catch (Exception ex) {
            Log.error("Could not save Orderstate to Repository " + ex.getMessage());
        }
    }

}
