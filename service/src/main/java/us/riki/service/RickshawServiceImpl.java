package us.riki.service;

import us.riki.converter.LocationToPointConverter;
import us.riki.dto.request.LocationDTO;
import us.riki.dto.request.RickshawRegisterEventDTO;
import us.riki.enums.RickshawStatus;
import us.riki.enums.RickshawType;
import us.riki.exception.RickshawIdNotFoundException;
import us.riki.model.Rickshaw;
import us.riki.repository.RickshawRepository;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class RickshawServiceImpl implements RickshawService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private final RickshawRepository rickshawRepository;

    private final LocationToPointConverter locationToPointConverter = new LocationToPointConverter();

    public RickshawServiceImpl(ReactiveRedisTemplate<String, String> reactiveRedisTemplate, RickshawRepository rickshawRepository){
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.rickshawRepository = rickshawRepository;
    }

    @Override
    public Mono<Rickshaw> register(RickshawRegisterEventDTO rickshawRegisterEventDTO) {
        Rickshaw rickshaw = new Rickshaw(rickshawRegisterEventDTO.getRickshawId(), rickshawRegisterEventDTO.getRickshawType(), RickshawStatus.AVAILABLE);
        return Mono.just(rickshawRepository.save(rickshaw));
    }

    @Override
    public Mono<Rickshaw> updateLocation(String rickshawId, LocationDTO locationDTO) {
        Optional<Rickshaw> rickshawOptional = rickshawRepository.findById(rickshawId);
        if (rickshawOptional.isPresent()){
            Rickshaw rickshaw = rickshawOptional.get();
            return reactiveRedisTemplate.opsForGeo().add(rickshaw.getRickshawType().toString(), locationToPointConverter.convert(locationDTO), rickshawId.toString()).flatMap(l -> Mono.just(rickshaw));
        } else {
            throw getRickshawIdNotFoundException(rickshawId);
        }
    }

    @Override
    public Flux<GeoResult<RedisGeoCommands.GeoLocation<String>>> getAvailableRickshaws(RickshawType rickshawType, Double latitude, Double longitude, Double radius) {
        return reactiveRedisTemplate.opsForGeo().radius(rickshawType.toString(), new Circle(new Point(longitude, latitude), new Distance(radius, Metrics.KILOMETERS)));
    }

    @Override
    public Mono<RickshawStatus> getRickshawStatus(String rickshawId) {
        Optional<Rickshaw> rickshawOptional = rickshawRepository.findById(rickshawId);
        if (rickshawOptional.isPresent()){
            Rickshaw rickshaw = rickshawOptional.get();
            return Mono.just(rickshaw.getRickshawStatus());
        } else {
            throw getRickshawIdNotFoundException(rickshawId);
        }
    }

    @Override
    public Mono<Rickshaw> updateRickshawStatus(String rickshawId, RickshawStatus rickshawStatus) {
        Optional<Rickshaw> rickshawOptional = rickshawRepository.findById(rickshawId);
        if (rickshawOptional.isPresent()){
            Rickshaw rickshaw = rickshawOptional.get();
            rickshaw.setRickshawStatus(rickshawStatus);
            return Mono.just(rickshawRepository.save(rickshaw));
        } else {
            throw new RickshawIdNotFoundException(rickshawId);
        }
    }

    @Override
    public RickshawIdNotFoundException getRickshawIdNotFoundException(String rickshawId) {
        return new RickshawIdNotFoundException("Rickshaw id " + rickshawId + " not found.");
    }

}
