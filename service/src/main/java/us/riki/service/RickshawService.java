package us.riki.service;

import us.riki.dto.request.LocationDTO;
import us.riki.dto.request.RickshawRegisterEventDTO;
import us.riki.enums.RickshawStatus;
import us.riki.enums.RickshawType;
import us.riki.exception.RickshawIdNotFoundException;
import us.riki.model.Rickshaw;
import us.riki.repository.RickshawRepository;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RickshawService {
    public Mono<Rickshaw> register(RickshawRegisterEventDTO rickshawRegisterEventDTO);

    Mono<Rickshaw> updateLocation(String rickshawId, LocationDTO locationDTO);

    Flux<GeoResult<RedisGeoCommands.GeoLocation<String>>> getAvailableRickshaws(RickshawType rickshawType,
        Double latitude, Double longitude, Double radius);

    Mono<RickshawStatus> getRickshawStatus(String rickshawId);

    Mono<Rickshaw> updateRickshawStatus(String rickshawId, RickshawStatus rickshawStatus);

    RickshawIdNotFoundException getRickshawIdNotFoundException(String rickshawId);

}
