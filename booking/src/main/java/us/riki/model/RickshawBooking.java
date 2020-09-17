package us.riki.model;



import us.riki.enums.RickshawBookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("RickshawBooking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RickshawBooking {

    @Id
    private String rickshawBookingId;

    private Point Start;

    private LocalDateTime startTime;

    private Point end;

    private LocalDateTime endTime;

    private LocalDateTime bookedTime;

    private LocalDateTime acceptedTime;

    private Long customerId;

    private RickshawBookingStatus bookingStatus;

    private String reasonToCancel;

    private LocalDateTime cancelTime;

    private String rickshawId;
}
