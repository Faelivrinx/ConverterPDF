package converter.convert;

public interface ConverterFactory {
    ConvertStrategy createStrategy(Converter converter);
}
