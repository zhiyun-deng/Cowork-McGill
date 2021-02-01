package midnightoil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import javax.persistence.OneToMany;

@Entity
public class PairingManager{
   private Integer id;

public void setId(Integer value) {
    this.id = value;
}
@Id
public Integer getId() {
    return this.id;
}
   private Set<Pairing> pairing;
   
   @OneToMany(mappedBy="pairingManager" )
   public Set<Pairing> getPairing() {
      return this.pairing;
   }
   
   public void setPairing(Set<Pairing> pairings) {
      this.pairing = pairings;
   }
   
   private Set<TimeSlot> timeSlot;
   
   @OneToMany(mappedBy="pairingManager" )
   public Set<TimeSlot> getTimeSlot() {
      return this.timeSlot;
   }
   
   public void setTimeSlot(Set<TimeSlot> timeSlots) {
      this.timeSlot = timeSlots;
   }
   
   private Set<Request> request;
   
   @OneToMany(mappedBy="pairingManager" )
   public Set<Request> getRequest() {
      return this.request;
   }
   
   public void setRequest(Set<Request> requests) {
      this.request = requests;
   }
   
   }
