package midnightoil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
@SpringBootApplication
public class MidnightOilApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidnightOilApplication.class, args);
	}
 @RequestMapping("/")
  public String greeting(){
    return "Hello world!";
  }

}
