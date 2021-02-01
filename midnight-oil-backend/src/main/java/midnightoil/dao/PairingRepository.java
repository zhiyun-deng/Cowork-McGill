package midnightoil.dao;
import org.springframework.data.repository.CrudRepository;
import midnightoil.model.*;

import java.util.List;
public interface PairingRepository extends CrudRepository<Pairing, Integer>{
	List<Request> findByTimeSlot(TimeSlot t);
	
}
