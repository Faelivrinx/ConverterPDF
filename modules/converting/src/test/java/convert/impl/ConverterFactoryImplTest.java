package convert.impl;

import convert.ConvertStrategy;
import convert.Convertable;
import convert.exception.InvalidFormatConvertion;
import org.assertj.core.api.Assertions;
import org.docx4j.services.client.Format;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class ConverterFactoryImplTest {

    private ConverterFactoryImpl factory;

    @Mock
    Convertable convertable;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        factory = new ConverterFactoryImpl();
    }

    @Test(expected = InvalidFormatConvertion.class)
    public void whenInvalidConvertionTypeShouldThrowException(){
        when(convertable.getFrom()).thenReturn(Format.TOC);
        when(convertable.getTo()).thenReturn(Format.TOC);

        factory.createStrategy(convertable);
    }

    @Test
    public void shouldCreateValidDocxToPdfStrategy(){
        when(convertable.getFrom()).thenReturn(Format.DOCX);
        when(convertable.getTo()).thenReturn(Format.PDF);

        ConvertStrategy result = factory.createStrategy(convertable);

        Assertions.assertThat(result).isInstanceOf(DocxToPDF.class);
    }

}
