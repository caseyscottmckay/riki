package us.riki.converter;


import us.riki.dto.request.LocationDTO;
import jdk.internal.jline.internal.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Point;

public class PointToLocationConverter implements Converter<Point, LocationDTO> {

    @Nullable
    @Override
    public LocationDTO convert(Point point){
        if (point == null){
            return null;
        }
        return new LocationDTO(point.getY(), point.getX(), null);
    }
}
