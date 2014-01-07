package example.infrastructure.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import example.domain.example1.Example1Message;
import example.domain.example1.Example1Publisher;

@Component
public class JmsExample1Publisher implements Example1Publisher {

	@Autowired
	@Qualifier("example1JmsTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void publish(Example1Message message) {
		jmsTemplate.convertAndSend(message);
	}

}
