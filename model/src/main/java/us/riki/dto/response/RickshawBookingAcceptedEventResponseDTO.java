package us.riki.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RickshawBookingAcceptedEventResponseDTO {

    private String rickshawBookingId;

    private String rickshawId;

    private LocalDateTime acceptedTime;
}
