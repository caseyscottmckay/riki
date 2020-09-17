package us.riki;


import us.riki.enums.RickshawStatus;
import us.riki.enums.RickshawType;
import us.riki.listener.RickshawBookingAcceptedEventMessageListener;
import us.riki.model.Rickshaw;
import us.riki.repository.RickshawRepository;
import us.riki.service.RickshawService;
import us.riki.util.LocationGenerator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.UUID;

@SpringBootApplication
@Import({RedisConfig.class})
public class ServiceApplication {

    public static void main(String[] args){
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(RickshawRepository rickshawRepository, RickshawService rickshawService) {
        return args -> {
            rickshawRepository.save(new Rickshaw(UUID.randomUUID().toString(), RickshawType.MINI, RickshawStatus.AVAILABLE));
            rickshawRepository.save(new Rickshaw(UUID.randomUUID().toString(), RickshawType.NANO, RickshawStatus.AVAILABLE));
            rickshawRepository.save(new Rickshaw(UUID.randomUUID().toString(), RickshawType.VAN, RickshawStatus.AVAILABLE));
            Iterable<Rickshaw> rickshaws = rickshawRepository.findAll();
            rickshaws.forEach(t -> {
                rickshawService.updateLocation(t.getRickshawId(), LocationGenerator.getLocation(79.865072, 6.927610, 3000)).subscribe();
            });

        };
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, RickshawBookingAcceptedEventMessageListener rickshawBookingAcceptedEventMessageListener){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(rickshawBookingAcceptedEventMessageListener, new PatternTopic(RedisConfig.ACCEPTED_EVENT_CHANNEL));
        return container;
    }

}
