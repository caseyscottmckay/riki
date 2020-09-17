package us.riki.converter;

import us.riki.dto.request.LocationDTO;
import jdk.internal.jline.internal.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Point;

public class LocationToPointConverter implements Converter<LocationDTO, Point> {

    @Nullable
    @Override
    public Point convert(LocationDTO locationDTO){
        if (locationDTO == null){
            return null;
        }
        return new Point(locationDTO.getLongitute(), locationDTO.getLatitute());
    }
}
