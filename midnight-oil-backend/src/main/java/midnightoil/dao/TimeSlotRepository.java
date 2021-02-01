package midnightoil.dao;
import org.springframework.data.repository.CrudRepository;
import midnightoil.model.*;
import java.util.List;
import midnightoil.model.TimeSlot;
import java.sql.Time;
import java.sql.Date;

public interface TimeSlotRepository extends CrudRepository<TimeSlot, Integer>{
	List<TimeSlot> findByStartTimeAndStartDate(Time time, Date date);
	List<TimeSlot> findByStartDateAfterAndStartTimeAfter(Date date, Time t);
}