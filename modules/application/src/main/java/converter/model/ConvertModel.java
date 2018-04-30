package converter.model;

import lombok.*;
import org.docx4j.services.client.Format;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ConvertModel {

    private final Format type;
    private final String filePath;
    private String output;


}
