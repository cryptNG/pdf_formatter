package pdf.formatter.pdf_formatter.exceptionHandling;

public class OrderNotFoundException extends RuntimeException {

    /**
     * Autogenerated by IDE, why?
     */
    private static final long serialVersionUID = 1L;

    public OrderNotFoundException(Long id) {
        super("Could not find order " + id);
    }
}
