package converter.bootstrap;

import converter.pdf.ConverterDocxToPdf;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLine implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
//        ConverterDocxToPdf.toPDF(args);
    }
}
