package convert;


import io.reactivex.Completable;
import org.docx4j.services.client.Format;

/**
 * It's simple representation of object which is able to convert.
 */
public interface Convertable {
    /**
     * @return source file format
     */
    Format getFrom();

    /**
     * @return output file format
     */
    Format getTo();

    /**
     * @return path to source file
     */
    String getFileInputPath();

    /**
     * @return path to output file
     */
    String getFileOutputPath();

    /**
     * Method used to convert file used special strategy.
     * @see ConvertStrategy
     * @return emmit complete when process succeed or error when something went wrong.
     */
    Completable convert(ConvertStrategy strategy);
}
