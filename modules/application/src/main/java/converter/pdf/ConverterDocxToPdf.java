package converter.pdf;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
public class ConverterDocxToPdf {
    public static void toPDF (String [] args){
        ConverterOutPDF converterOutPDF = new ConverterOutPDF();
        converterOutPDF.convert(args);
    }
}
