package converter.convert;

import org.docx4j.services.client.Format;
import org.springframework.stereotype.Component;

@Component
public class ConverterFactoryImpl implements ConverterFactory {
    @Override
    public ConvertStrategy createStrategy(Converter converter) {
        if (converter.getFrom() == Format.DOCX && converter.getTo() == Format.PDF){
            return new DocxToPDF();
        } else {
            throw new RuntimeException("Invalid convertion type!");
        }
    }
}
