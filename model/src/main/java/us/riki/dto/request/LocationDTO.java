package us.riki.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    private Double latitute;

    private Double longitute;

    private String name;

}
