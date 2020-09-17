package us.riki.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.riki.dto.request.RickshawBookingAcceptedEventDTO;
import us.riki.enums.RickshawStatus;
import us.riki.service.RickshawService;
import jdk.internal.jline.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RickshawBookingAcceptedEventMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RickshawBookingAcceptedEventMessageListener.class);

    private RickshawService rickshawService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RickshawBookingAcceptedEventMessageListener(RickshawService rickshawService){
        this.rickshawService= rickshawService;
    }

    @Override
    public void onMessage(Message message, @Nullable byte[] bytes) {
        try {
            RickshawBookingAcceptedEventDTO rickshawBookingAcceptedEventDTO = objectMapper.readValue(new String(message.getBody()), RickshawBookingAcceptedEventDTO.class);
            LOGGER.info("Accepted Event {}", rickshawBookingAcceptedEventDTO);
            rickshawService.updateRickshawStatus(rickshawBookingAcceptedEventDTO.getRickshawId(), RickshawStatus.OCCUPIED);
        } catch (IOException e) {
            LOGGER.error("Error while updating rickshaw status", e);
        }
    }
}
