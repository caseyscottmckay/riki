package us.riki.repository;

import us.riki.model.RickshawBooking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RickshawBookingRepository extends CrudRepository<RickshawBooking, String> {
}
