package midnightoil.dao;
import org.springframework.data.repository.CrudRepository;
import midnightoil.model.*;
import java.util.List;
public interface RequestRepository extends CrudRepository<Request, Integer>{
	List<Request> findByPairing(Pairing p);
	List<Request> findByTimeSlot(TimeSlot t);
	List<Request> findByPairedTrue();
	List<Request> findByPairedFalse();
	
}