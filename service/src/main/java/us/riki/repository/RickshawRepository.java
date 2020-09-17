package us.riki.repository;

import us.riki.model.Rickshaw;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RickshawRepository extends CrudRepository<Rickshaw, String> {

}
