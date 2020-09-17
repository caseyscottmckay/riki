package us.riki.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RickshawBookingCanceledEventDTO {

    private String rickshawBookingId;

    private String reason;

    private LocalDateTime cancelTime;

}
