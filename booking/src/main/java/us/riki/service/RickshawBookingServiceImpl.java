package us.riki.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import us.riki.RedisConfig;
import us.riki.converter.LocationToPointConverter;
import us.riki.dto.request.RickshawBookedEventDTO;
import us.riki.dto.request.RickshawBookingAcceptedEventDTO;
import us.riki.dto.request.RickshawBookingCanceledEventDTO;
import us.riki.enums.RickshawBookingStatus;
import us.riki.enums.RickshawType;
import us.riki.exception.RickshawBookingIdNotFoundException;
import us.riki.model.RickshawBooking;
import us.riki.repository.RickshawBookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class RickshawBookingServiceImpl implements RickshawBookingService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RickshawBookingService.class);

    private RedisTemplate<String, String> redisTemplate;

    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private RickshawBookingRepository rickshawBookingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final LocationToPointConverter locationToPointConverter = new LocationToPointConverter();

    public RickshawBookingServiceImpl(RedisTemplate<String, String> redisTemplate, ReactiveRedisTemplate<String, String> reactiveRedisTemplate, RickshawBookingRepository rickshawBookingRepository){
        this.redisTemplate = redisTemplate;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.rickshawBookingRepository = rickshawBookingRepository;
    }

    @Override
    public Mono<RickshawBooking> book(RickshawBookedEventDTO rickshawBookedEventDTO) {
        RickshawBooking rickshawBooking = new RickshawBooking();
        rickshawBooking.setEnd(locationToPointConverter.convert(rickshawBookedEventDTO.getEnd()));
        rickshawBooking.setStart(locationToPointConverter.convert(rickshawBookedEventDTO.getStart()));
        rickshawBooking.setBookedTime(rickshawBookedEventDTO.getBookedTime());
        rickshawBooking.setCustomerId(rickshawBookedEventDTO.getCustomerId());
        rickshawBooking.setBookingStatus(RickshawBookingStatus.ACTIVE);
        RickshawBooking savedRickshawBooking = rickshawBookingRepository.save(rickshawBooking);
        return reactiveRedisTemplate.opsForGeo().add(getRickshawTypeBookings(rickshawBookedEventDTO.getRickshawType()), rickshawBooking.getStart(), rickshawBooking.getRickshawBookingId()).flatMap(b -> Mono.just(savedRickshawBooking));
    }

    @Override
    public Mono<RickshawBooking> cancel(String rickshawBookingId, RickshawBookingCanceledEventDTO canceledEventDTO) {
        Optional<RickshawBooking> rickshawBookingOptional = rickshawBookingRepository.findById(rickshawBookingId);
        if (rickshawBookingOptional.isPresent()) {
            RickshawBooking rickshawBooking = rickshawBookingOptional.get();
            rickshawBooking.setBookingStatus(RickshawBookingStatus.CANCELLED);
            rickshawBooking.setReasonToCancel(canceledEventDTO.getReason());
            rickshawBooking.setCancelTime(canceledEventDTO.getCancelTime());
            return Mono.just(rickshawBookingRepository.save(rickshawBooking));
        } else {
            throw getRickshawBookingIdNotFoundException(rickshawBookingId);
        }
    }

    @Override
    public Mono<RickshawBooking> accept(String rickshawBookingId, RickshawBookingAcceptedEventDTO acceptedEventDTO) {
        Optional<RickshawBooking> rickshawBookingOptional = rickshawBookingRepository.findById(rickshawBookingId);
        if (rickshawBookingOptional.isPresent()) {
            RickshawBooking rickshawBooking = rickshawBookingOptional.get();
            rickshawBooking.setRickshawId(acceptedEventDTO.getRickshawId());
            rickshawBooking.setAcceptedTime(acceptedEventDTO.getAcceptedTime());
            return Mono.just(rickshawBookingRepository.save(rickshawBooking)).doOnSuccess(t -> {
                try {
                    redisTemplate.convertAndSend(RedisConfig.ACCEPTED_EVENT_CHANNEL, objectMapper.writeValueAsString(acceptedEventDTO));
                } catch (JsonProcessingException e) {
                    LOGGER.error("Error while sending message to Channel {}", RedisConfig.ACCEPTED_EVENT_CHANNEL, e);
                }
            });
        } else {
            throw getRickshawBookingIdNotFoundException(rickshawBookingId);
        }
    }

    @Override
    public Flux<GeoResult<RedisGeoCommands.GeoLocation<String>>> getBookings(RickshawType rickshawType, Double latitude, Double longitude, Double radius) {
        return reactiveRedisTemplate.opsForGeo().radius(getRickshawTypeBookings(rickshawType), new Circle(new Point(longitude, latitude), new Distance(radius, Metrics.KILOMETERS)));
    }

    @Override
    public Mono<RickshawBooking> updateBookingStatus(String rickshawBookingId, RickshawBookingStatus rickshawBookingStatus) {
        Optional<RickshawBooking> rickshawBookingOptional = rickshawBookingRepository.findById(rickshawBookingId);
        if (rickshawBookingOptional.isPresent()) {
            RickshawBooking rickshawBooking = rickshawBookingOptional.get();
            rickshawBooking.setBookingStatus(rickshawBookingStatus);
            return Mono.just(rickshawBookingRepository.save(rickshawBooking));
        } else {
            throw getRickshawBookingIdNotFoundException(rickshawBookingId);
        }
    }

   private RickshawBookingIdNotFoundException getRickshawBookingIdNotFoundException(String rickshawBookingId) {
        return new RickshawBookingIdNotFoundException("Rickshaw Booking Id "+rickshawBookingId+" Not Found");
    }


   private String getRickshawTypeBookings(RickshawType rickshawType) {
        return rickshawType.toString()+"-Bookings";
    }

}
