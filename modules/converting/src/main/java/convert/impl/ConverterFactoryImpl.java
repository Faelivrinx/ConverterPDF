package convert.impl;

import convert.ConvertStrategy;
import convert.Convertable;
import convert.ConverterFactory;
import convert.exception.InvalidFormatConvertion;
import org.docx4j.services.client.Format;

public class ConverterFactoryImpl implements ConverterFactory {

    @Override
    public ConvertStrategy createStrategy(Convertable converter) {
        if (converter.getFrom() == Format.DOCX && converter.getTo() == Format.PDF){
            return new DocxToPDF();
        } else {
            throw new InvalidFormatConvertion("Invalid convertion type!");
        }
    }
}
