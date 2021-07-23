package az.code.tourapi.utils.representations;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailRepresentation {
    private String subject;
    private String context;
    private Map<String, String> urls;
}
