package pdf.formatter.pdf_formatter.controllers;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pdf.formatter.pdf_formatter.entities.Order;
import pdf.formatter.pdf_formatter.repositories.OrderRepository;
import pdf.formatter.pdf_formatter.tooling.OrderManager;
import pdf.formatter.presentation.OrderPo;
import pdf.formatter.presentation.PdfDocumentPo;

@RestController
public class OrderController {

    private final OrderRepository _repository;
    private ExecutorService _pool = Executors.newCachedThreadPool();

    Logger Log = LoggerFactory.getLogger(OrderController.class);

    OrderController(OrderRepository repository) {
        this._repository = repository;

    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.TEXT_PLAIN_VALUE })
    // @PostMapping("/orders")
    public String PostOrderAsync(@RequestBody OrderPo newOrderPo) {
        Order orderToCreate = new Order(newOrderPo.ResultType, newOrderPo.RequestId);

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Log.info("POST (/orders) " + "[" + methodName + "] called");

        Order checkAlreadyExists = _repository.findByRequestId(orderToCreate.getRequestId());

        if (checkAlreadyExists != null) {
            return checkAlreadyExists.getRequestId();
        }

        Order resultOrder = _repository.save(orderToCreate); // do not save xml/xsl
        _pool.submit(() -> configureAndBeginOrder(newOrderPo.XmlData, newOrderPo.XslData, newOrderPo.RequestId));

        // orderMan.OrderPdfGenerate(resultOrder, _repository);
        // TASK ID == ORDER ID
        return resultOrder.getRequestId();
    }

    private void configureAndBeginOrder(String xmlData, String xslData, String requestId) {
        OrderManager orderMan = new OrderManager();

        Future<byte[]> future = _pool.submit(() -> orderMan.OrderPdfGenerate(xmlData, xslData));
        byte[] resultDocumentData = null;
        Order newOrder = null;
        try {
            newOrder = _repository.findByRequestId(requestId);
        } catch (Exception e) {
            Log.error(e.getMessage());
            return;
        }

        try {
            resultDocumentData = future.get();
            newOrder.setDocumentData(resultDocumentData);
            newOrder.setDocumentCreationDate(new Date());
            newOrder.setState("Finished");
            Log.info("Document generated for " + newOrder.getRequestId());
        } catch (InterruptedException e) {
            Log.error(e.getMessage());
            newOrder.setDocumentData(e.getMessage().getBytes());
            newOrder.setDocumentCreationDate(new Date());
            newOrder.setState("Error");
            // Thread.currentThread().interrupt(); // Reset interrupted status
        } catch (ExecutionException e) {
            // Throwable exception = e.getCause();
            Log.error(e.getMessage());
            newOrder.setDocumentData(e.getMessage().getBytes());
            newOrder.setDocumentCreationDate(new Date());
            newOrder.setState("Error");
            // Forward to exception reporter
        }

        try {
            _repository.save(newOrder);
        } catch (Exception e) {
            Log.error("Could not save document in order due to exception: " + e.getMessage());
            e.printStackTrace();

        }
    }

    @RequestMapping(value = "/orders/{requestId}/state", method = RequestMethod.GET, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.TEXT_PLAIN_VALUE })
    // @GetMapping("/orders/{requestId}/state")
    public String GetOrderState(@PathVariable String requestId) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Log.info("GET (/orders/" + requestId + "/state) " + "[" + methodName + "] called");

        Order currentOrder = _repository.findByRequestId(requestId);
        if (currentOrder == null) {
            return "NotCreatedYet";
        }
        // If public, return state object with statechangeddate, to determine processing
        // time
        return currentOrder.getState(); // Should this be ENUM?
    }

    @RequestMapping(value = "/orders/{requestId}/document", method = RequestMethod.GET, consumes = {
            MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    // @GetMapping("/orders/{requestId}/document")
    public PdfDocumentPo GetPdfDocumentObject(@PathVariable String requestId) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Log.info("GET (/orders/" + requestId + "/document) " + "[" + methodName + "] called");

        Order currentOrder = _repository.findByRequestId(requestId); // check if this really works
        if (currentOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
        byte[] docData = currentOrder.getDocumentData();
        PdfDocumentPo returnDoc = new PdfDocumentPo(DatatypeConverter.printBase64Binary(docData));
        returnDoc.setCreationDate(currentOrder.getDocumentCreationDate());
        _repository.delete(currentOrder);
        return returnDoc;
    }

    @RequestMapping(value = "/orders/{requestId}", method = RequestMethod.DELETE, consumes = {
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    // @DeleteMapping("/orders/{requestId}")
    public void DeleteOrder(@PathVariable String requestId) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Log.info("DELETE (/orders/" + requestId + ") [" + methodName + "] called");

        Order toDelete = _repository.findByRequestId(requestId);
        _repository.deleteById(toDelete.getId());
    }
}
