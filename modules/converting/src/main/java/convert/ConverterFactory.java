package convert;

import convert.exception.InvalidFormatConvertion;

public interface ConverterFactory {
    /**
     * Create implementation of ConvertStrategy. It using {{@link Convertable}} fields and choose right strategy.
     * Example:
     * if (converter.from == Format.DOCX && converter.to == Format.PDF) then use {{@link convert.impl.DocxToPDF}} strategy.
     * @param converter contains information about source, destination type and input, output file path.
     * @return implementation of {{@link ConvertStrategy}}
     * @throws InvalidFormatConvertion when source and destiny format aren't able to convert or library doesn't support this.
     */
    ConvertStrategy createStrategy(Convertable converter) throws InvalidFormatConvertion;
}
