package midnightoil.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ManyToMany;

@Entity
public class Request{
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
   
   
   private Set<TimeSlot> timeSlot = new HashSet<>();
   @ManyToMany( cascade = { CascadeType.ALL })
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
   
   private String id;
   public void setId (String value) 
   {
      this.id = value;
      }
   @Id
   @GeneratedValue(generator="uuid2")
   @GenericGenerator(name = "uuid2", strategy = "uuid2")
   public String getId () 
   {
      return this.id;
      }
   private String link;

public void setLink(String value) {
    this.link = value;
}
public String getLink() {
    return this.link;
}
}
