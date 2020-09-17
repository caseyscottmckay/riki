package us.riki.model;
import us.riki.enums.RickshawStatus;
import us.riki.enums.RickshawType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@RedisHash("Rickshaw")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rickshaw implements Serializable {

    @Id
    private String rickshawId;

    private RickshawType rickshawType;

    private RickshawStatus rickshawStatus;
}
