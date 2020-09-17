package us.riki.dto.request;


import us.riki.enums.RickshawType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RickshawRegisterEventDTO {

    public String rickshawId = UUID.randomUUID().toString();

    private RickshawType rickshawType;
}
