package convert;


import io.reactivex.Completable;
import org.docx4j.services.client.Format;

public interface Convertable {
    Format getFrom();
    Format getTo();
    String getFileInputPath();
    String getFileOutputPath();
    Completable convert(ConvertStrategy strategy);
}
