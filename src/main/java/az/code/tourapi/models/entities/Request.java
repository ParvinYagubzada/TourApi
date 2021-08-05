package az.code.tourapi.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "requests")
public class Request {
    @Id
    private String uuid;
    private String language;
    private String tourType;
    private String addressTo;
    private String addressFrom;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate travelStartDate;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate travelEndDate;
    private String travellerCount;
    private Integer budget;

    @Column(name = "status")
    private boolean active;
    @CreationTimestamp
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime creationTime;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @ToString.Exclude
    private LocalDateTime expirationTime;

    @ToString.Include(name = "expirationTime")
    private String formatScheduledStartDate() {
        return expirationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss"));
    }
}
