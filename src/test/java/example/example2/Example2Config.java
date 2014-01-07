package example.example2;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Example2Config {

	@Bean
	public Example2Listener example2Listener1() {
		return Mockito.mock(Example2Listener.class);
	}
	
	@Bean
	public Example2Listener example2Listener2() {
		return Mockito.mock(Example2Listener.class);
	}
}