package us.riki.dto.response;

import us.riki.enums.RickshawStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RickshawStatusDTO {

    private String rickshawId;

    private RickshawStatus status;
}
