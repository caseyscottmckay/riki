package us.riki.service;

import us.riki.dto.request.RickshawBookedEventDTO;
import us.riki.dto.request.RickshawBookingAcceptedEventDTO;
import us.riki.dto.request.RickshawBookingCanceledEventDTO;
import us.riki.enums.RickshawBookingStatus;
import us.riki.enums.RickshawType;
import us.riki.model.RickshawBooking;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RickshawBookingService {

    Mono<RickshawBooking> book(RickshawBookedEventDTO rickshawBookedEventDTO);

    Mono<RickshawBooking> cancel(String rickshawBookingId, RickshawBookingCanceledEventDTO canceledEventDTO);

    Mono<RickshawBooking> accept(String rickshawBookingId, RickshawBookingAcceptedEventDTO acceptedEventDTO);

    Flux<GeoResult<RedisGeoCommands.GeoLocation<String>>> getBookings(RickshawType rickshawType,
        Double latitude, Double longitude, Double radius);

    Mono<RickshawBooking> updateBookingStatus(String rickshawBookingId, RickshawBookingStatus rickshawBookingStatus);

//    RickshawBookingIdNotFoundException getRickshawBookingIdNotFoundException(String rickshawBookingId);

  //  String getRickshawTypeBookings(RickshawType rickshawType);
}
