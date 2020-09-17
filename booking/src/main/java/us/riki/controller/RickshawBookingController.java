package us.riki.controller;

import us.riki.dto.response.RickshawBookingCanceledEventResponseDTO;
import us.riki.dto.request.RickshawBookedEventDTO;
import us.riki.dto.request.RickshawBookingAcceptedEventDTO;
import us.riki.dto.request.RickshawBookingCanceledEventDTO;
import us.riki.dto.response.RickshawBookedEventResponseDTO;
import us.riki.dto.response.RickshawBookingAcceptedEventResponseDTO;
import us.riki.dto.response.RickshawBookingResponseDTO;
import us.riki.enums.RickshawType;
import us.riki.service.RickshawBookingService;
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

@RequestMapping("/rickshawbookings")
@RestController
public class RickshawBookingController {

    private final RickshawBookingService rickshawBookingService;

    public RickshawBookingController(RickshawBookingService rickshawBookingService) {
        this.rickshawBookingService = rickshawBookingService;
    }

    @PostMapping
    public Mono<RickshawBookedEventResponseDTO> book(@RequestBody RickshawBookedEventDTO rickshawBookedEventDTO) {
        return rickshawBookingService.book(rickshawBookedEventDTO).map(t -> new RickshawBookedEventResponseDTO(t.getRickshawBookingId()));
    }

    @PutMapping("/{rickshawBookingId}/cancel")
    public Mono<RickshawBookingCanceledEventResponseDTO> cancel(@PathVariable("rickshawBookingId") String rickshawBookingId, @RequestBody RickshawBookingCanceledEventDTO rickshawBookingCanceledEventDTO) {
        return rickshawBookingService.cancel(rickshawBookingId, rickshawBookingCanceledEventDTO).map(t -> new RickshawBookingCanceledEventResponseDTO(t.getRickshawBookingId()));
    }

    @PutMapping("/{rickshawBookingId}/accept")
    public Mono<RickshawBookingAcceptedEventResponseDTO> accept(@PathVariable("rickshawBookingId") String rickshawBookingId, @RequestBody RickshawBookingAcceptedEventDTO rickshawBookingAcceptedEventDTO) {
        return rickshawBookingService.accept(rickshawBookingId, rickshawBookingAcceptedEventDTO).map(t -> new RickshawBookingAcceptedEventResponseDTO(t.getRickshawBookingId(), t.getRickshawId(), t.getAcceptedTime()));
    }

    @GetMapping
    public Flux<RickshawBookingResponseDTO> getBookings(@RequestParam("type") RickshawType rickshawType, @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude, @RequestParam(value = "radius", defaultValue = "1") Double radius) {
        return rickshawBookingService.getBookings(rickshawType, latitude, longitude, radius).map(r -> new RickshawBookingResponseDTO(r.getContent().getName()));
    }

}
