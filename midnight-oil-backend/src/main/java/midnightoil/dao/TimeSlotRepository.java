package midnightoil.dao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import midnightoil.model.*;
import java.util.List;
import midnightoil.model.TimeSlot;
import java.sql.Time;
import java.sql.Date;

public interface TimeSlotRepository extends CrudRepository<TimeSlot, Integer>{
	@Query(
			  value = "SELECT * FROM time_slot where num_requests % 2 !=0 order by start_date, start_time;", 
			  nativeQuery = true)
	List<TimeSlot> findAllActiveTimeslots();
	List<TimeSlot> findByStartTimeAndStartDate(Time time, Date date);
	List<TimeSlot> findByStartDateAfterAndStartTimeAfter(Date date, Time t);
	List<TimeSlot> findByStartDateBeforeAndStartTimeBefore(Date date, Time t);
	List<TimeSlot> findByStartDateBefore(Date date);
	List<TimeSlot> findByStartDateAfter(Date date);
	List<TimeSlot> findAllByOrderByStartDateAscStartTimeAsc();
	List<TimeSlot> findByStartDateAfterOrStartTimeAfter(Date date, Time t);
	List<TimeSlot> findByStartDateEqualsAndStartTimeBeforeOrStartDateBefore(Date date, Time t, Date date2);
	List<TimeSlot> findByStartDateAfterAndStartTimeAfterAndEndDateBeforeAndEndTimeBefore(Date startDate, Time startTime, Date endDate, Time endTime);
}