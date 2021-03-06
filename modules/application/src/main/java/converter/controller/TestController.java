package converter.controller;

import convert.ConverterFactory;
import converter.convert.Converter;
import lombok.RequiredArgsConstructor;
import org.docx4j.services.client.Format;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final ConverterFactory factory;


    @GetMapping("/convert")
    public void convertToPDF(@RequestParam("from") Format from,
                             @RequestParam("to")Format to,
                             @RequestParam("filePath") String filePath,
                             @RequestParam("outPath") Optional<String >outPath){
        String dest = outPath.orElse("");
        Converter converter = Converter.builder()
                .inFormat(from)
                .outFormat(to)
                .and(filePath)
                .fileOutputPath(dest)
                .build();

        converter.convert(factory.createStrategy(converter));
    }
}
