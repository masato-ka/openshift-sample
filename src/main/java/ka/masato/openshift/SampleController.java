package ka.masato.openshift;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

	@GetMapping(path="/hello")
	public String hello(){
		return "hello";
	}
	
}
