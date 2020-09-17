package us.riki;

import us.riki.dto.request.RickshawBookedEventDTO;
import us.riki.enums.RickshawType;
import us.riki.service.RickshawBookingService;
import us.riki.util.LocationGenerator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@Import(RedisConfig.class)
public class BookingApplication {

    public static void main(String[] args){
        SpringApplication.run(BookingApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(RickshawBookingService rickshawBookingService) {
        return args -> {
            for (int i = 0;i<3;i++) {
                rickshawBookingService.book(new RickshawBookedEventDTO(UUID.randomUUID().toString(), LocationGenerator.getLocation(79.865072, 6.927610, 3000), LocationGenerator.getLocation(79.865072, 6.927610, 3000), LocalDateTime.now(), 1l, RickshawType.MINI)).subscribe();
            }
            for (int i = 0;i<3;i++) {
                rickshawBookingService.book(new RickshawBookedEventDTO(UUID.randomUUID().toString(), LocationGenerator.getLocation(79.865072, 6.927610, 3000), LocationGenerator.getLocation(79.865072, 6.927610, 3000), LocalDateTime.now(), 1l, RickshawType.NANO)).subscribe();
            }
            for (int i = 0;i<3;i++) {
                rickshawBookingService.book(new RickshawBookedEventDTO(UUID.randomUUID().toString(), LocationGenerator.getLocation(79.865072, 6.927610, 3000), LocationGenerator.getLocation(79.865072, 6.927610, 3000), LocalDateTime.now(), 1l, RickshawType.VAN)).subscribe();
            }
        };
    }
}
