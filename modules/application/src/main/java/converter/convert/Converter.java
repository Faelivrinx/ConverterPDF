package converter.convert;

import lombok.Getter;
import org.docx4j.services.client.Format;

@Getter
public class Converter {
    private Format from;
    private Format to;
    private String fileInputPath;
    private String fileOutputPath;

    public void convert(ConvertStrategy strategy){
        strategy.convert(this);
    }

    public static Builder builder(){
        return new Builder();
    }


    public static class Builder implements NeedFormatIn, NeedFormatOut, NeedFilePath, CanBeBuild {
        private Format from;
        private Format to;
        private String fileInputPath;
        private String fileOutputPath ;


        @Override
        public NeedFormatOut inFormat(Format intFormat) {
            this.from = intFormat;
            return this;
        }

        @Override
        public NeedFilePath outFormat(Format outFormat) {
            this.to = outFormat;
            return this;
        }

        @Override
        public CanBeBuild and(String filePath) {
            this.fileInputPath = filePath;
            return this;
        }

        @Override
        public CanBeBuild fileOutputPath(String outputPath) {
            this.fileOutputPath = outputPath;
            return this;
        }

        @Override
        public Converter build() {
            Converter converter = new Converter();
            converter.from = from;
            converter.to= to;
            converter.fileInputPath = fileInputPath;
            if (fileInputPath != null){
                converter.fileOutputPath = fileOutputPath;
            } else {
                converter.fileOutputPath = "";
            }
            return converter;
        }
    }

    public interface NeedFormatIn{
        NeedFormatOut inFormat(Format intFormat);
    }
    public interface NeedFormatOut{
        NeedFilePath outFormat(Format outFormat);
    }

    public interface NeedFilePath{
        CanBeBuild and(String filePath);
    }

    public interface CanBeBuild{
        CanBeBuild fileOutputPath(String outputPath);

        Converter build();
    }

}
