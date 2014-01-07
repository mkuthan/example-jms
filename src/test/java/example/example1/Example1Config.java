package example.example1;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Example1Config {

	@Bean
	public Example1Listener example1Listener() {
		return Mockito.mock(Example1Listener.class);
	}
}