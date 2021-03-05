package pdf.formatter.pdf_formatter.tooling;

import java.io.*;
import java.nio.file.*;

public class PdfConverter {

    public byte[] FileToByteArray(String pathToPdf) throws IOException {

        Path pdfPath = Paths.get(pathToPdf);
        byte[] pdf = Files.readAllBytes(pdfPath);
        return pdf;
    }

    public void ByteArrayToFile(String fileToCreate, byte[] fileData) throws IOException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(fileToCreate);
            stream.write(fileData);
        } finally {
            stream.close();
        }

    }

}
