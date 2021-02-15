package midnightoil.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Set;
import javax.persistence.ManyToMany;

@Entity
public class Request{
   private Integer id;

public void setId(Integer value) {
    this.id = value;
}
@Id
public Integer getId() {
    return this.id;
}
private boolean paired = false;

public void setPaired(boolean value) {
    this.paired = value;
}
public boolean isPaired() {
    return this.paired;
}
   private Pairing pairing;
   
   @ManyToOne
   public Pairing getPairing() {
      return this.pairing;
   }
   
   public void setPairing(Pairing pairing) {
      this.pairing = pairing;
   }
   
   private Set<TimeSlot> timeSlot;
   
   @ManyToMany(mappedBy="request" )
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
