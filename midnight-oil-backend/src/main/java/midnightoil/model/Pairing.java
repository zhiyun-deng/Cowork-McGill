package midnightoil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Pairing{
   private Integer id;

public void setId(Integer value) {
    this.id = value;
}
@Id
public Integer getId() {
    return this.id;
}
private String link;

public void setLink(String value) {
    this.link = value;
}
public String getLink() {
    return this.link;
}
   private Set<Request> request;
   
   @ManyToMany
   public Set<Request> getRequest() {
      return this.request;
   }
   
   public void setRequest(Set<Request> requests) {
      this.request = requests;
   }
   
   private Set<TimeSlot> timeSlot;
   
   @ManyToMany(mappedBy="pairing" )
   public Set<TimeSlot> getTimeSlot() {
      return this.timeSlot;
   }
   
   public void setTimeSlot(Set<TimeSlot> timeSlots) {
      this.timeSlot = timeSlots;
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
