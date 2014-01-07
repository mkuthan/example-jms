package example.infrastructure.jms.example1;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import example.domain.example1.Example1Listener;

@Configuration
public class Example1Config {

	@Bean
	public Example1Listener example1Listener() {
		return Mockito.mock(Example1Listener.class);
	}
}