package us.riki.controller;

import us.riki.dto.response.RickshawAvailableResponseDTO;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import us.riki.dto.request.LocationDTO;
import us.riki.dto.request.RickshawRegisterEventDTO;
import us.riki.dto.response.RickshawLocationUpdatedEventResponseDTO;
import us.riki.dto.response.RickshawRegisterEventResponseDTO;
import us.riki.dto.response.RickshawStatusDTO;
import us.riki.enums.RickshawStatus;
import us.riki.enums.RickshawType;
import us.riki.service.RickshawService;

@RequestMapping("/rickshaws")
@RestController
public class RickshawController {

    private RickshawService rickshawService;

    public RickshawController(RickshawService rickshawService){
        this.rickshawService = rickshawService;
    }

    @GetMapping
    public Flux<RickshawAvailableResponseDTO> getAvailableRickshaws(@RequestParam("type") RickshawType rickshawType, @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude, @RequestParam(value = "radius", defaultValue = "1") Double radius) {
        Flux<GeoResult<RedisGeoCommands.GeoLocation<String>>> availableRickshawsFlux = rickshawService.getAvailableRickshaws(rickshawType, latitude, longitude, radius);
        return availableRickshawsFlux.map(r -> new RickshawAvailableResponseDTO(r.getContent().getName()));
    }
    @GetMapping("/{rickshawId}/status")
    public Mono<RickshawStatusDTO> getRickshawStatus(@PathVariable("rickshawId") String rickshawId) {
        return rickshawService.getRickshawStatus(rickshawId).map(s -> new RickshawStatusDTO(rickshawId, s));
    }

    @PutMapping("/{rickshawId}/status")
    public Mono<RickshawStatusDTO> updateRickshawStatus(@PathVariable("rickshawId") String rickshawId, @RequestParam("status") RickshawStatus rickshawStatus) {
        return rickshawService.updateRickshawStatus(rickshawId, rickshawStatus).map(t -> new RickshawStatusDTO(t.getRickshawId(), t.getRickshawStatus()));
    }

    @PutMapping("/{rickshawId}/location")
    public Mono<RickshawLocationUpdatedEventResponseDTO> updateLocation(@PathVariable("rickshawId") String rickshawId, @RequestBody LocationDTO locationDTO) {
        return rickshawService.updateLocation(rickshawId, locationDTO).map(t -> new RickshawLocationUpdatedEventResponseDTO(rickshawId));
    }

    @PostMapping
    public Mono<RickshawRegisterEventResponseDTO> register(@RequestBody RickshawRegisterEventDTO rickshawRegisterEventDTO) {
        return rickshawService.register(rickshawRegisterEventDTO).map(t -> new RickshawRegisterEventResponseDTO(t.getRickshawId()));
    }
}
