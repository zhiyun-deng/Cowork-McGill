package midnightoil.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.sql.Time;
import java.sql.Date;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class TimeSlot{
   private Time startTime;

public void setStartTime(Time value) {
    this.startTime = value;
}
public Time getStartTime() {
    return this.startTime;
}
private Time endTime;

public void setEndTime(Time value) {
    this.endTime = value;
}
public Time getEndTime() {
    return this.endTime;
}
private Date startDate;

public void setStartDate(Date value) {
    this.startDate = value;
}
public Date getStartDate() {
    return this.startDate;
}
private Date endDate;

public void setEndDate(Date value) {
    this.endDate = value;
}
public Date getEndDate() {
    return this.endDate;
}
private Integer numPairs;

public void setNumPairs(Integer value) {
    this.numPairs = value;
}
public Integer getNumPairs() {
    return this.numPairs;
}
private Integer numRequests;

public void setNumRequests(Integer value) {
    this.numRequests = value;
}
public Integer getNumRequests() {
    return this.numRequests;
}
private Integer id;

public void setId(Integer value) {
    this.id = value;
}
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
public Integer getId() {
    return this.id;
}
   private Set<Request> request;
   
   @ManyToMany
   public Set<Request> getRequest() {
      return this.request;
   }
   
   public void setRequest(Set<Request> requests) {
      this.request = requests;
   }
   
   private Set<Pairing> pairing;
   
   @ManyToMany
   public Set<Pairing> getPairing() {
      return this.pairing;
   }
   
   public void setPairing(Set<Pairing> pairings) {
      this.pairing = pairings;
   }
   
   private PairingManager pairingManager;
   
   @ManyToOne
   public PairingManager getPairingManager() {
      return this.pairingManager;
   }
   
   public void setPairingManager(PairingManager pairingManager) {
      this.pairingManager = pairingManager;
   }
   
   }
