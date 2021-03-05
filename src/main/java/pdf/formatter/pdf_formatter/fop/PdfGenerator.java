package pdf.formatter.pdf_formatter.fop;

import java.io.File;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SAXDestination;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

public class PdfGenerator {

    Logger Log = LoggerFactory.getLogger(PdfGenerator.class);

    public byte[] ExportToPDF(String xmlData, String xslData) throws Exception {

        Source xsl = new StreamSource(new StringReader(xslData));
        Source xml = new StreamSource(new StringReader(xmlData));

        Processor processor = new Processor(false);
        XsltCompiler compiler = processor.newXsltCompiler();
        XsltExecutable stylesheet = null;
        try {
            stylesheet = compiler.compile(xsl);
        } catch (Exception ex) {
            Log.error(ex.getMessage());
        }

        Serializer out = processor.newSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // out.setOutputStream(baos);

        // out.setOutputProperty(Serializer.Property.METHOD, "xml");
        // out.setOutputProperty(Serializer.Property.INDENT, "yes");

        Xslt30Transformer transformer = stylesheet.load30();

        // TransformerFactory factory = new net.sf.saxon.TransformerFactoryImp();

        SAXDestination pdfDst = null;
        try {
            FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI());
            builder.setStrictFOValidation(false);
            // FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FopFactory fopFactory = builder.build();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, baos);
            pdfDst = new SAXDestination(fop.getDefaultHandler());
        } catch (Exception ex) {
            Log.error(ex.getMessage());
        }

        try {
            transformer.transform(xml, pdfDst);

        } catch (Exception ex) {
            Log.error(ex.getMessage());
            throw new Exception("Could not create file");
        }
        out.close();
        baos.close();
        pdfDst.close();

        return baos.toByteArray();
    }

}