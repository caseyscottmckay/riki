package us.riki.dto.request;

import us.riki.enums.RickshawType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RickshawBookedEventDTO {

    private String rickshawBookingId = UUID.randomUUID().toString();

    private LocationDTO start;

    private LocationDTO end;

    private LocalDateTime bookedTime;

    private Long customerId;

    private RickshawType rickshawType;

}
