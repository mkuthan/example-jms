package example.infrastructure.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import example.domain.example2.Example2Message;
import example.domain.example2.Example2Publisher;

@Component
public class JmsExample2Publisher implements Example2Publisher {

	@Autowired
	@Qualifier("example2JmsTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void publish(Example2Message message) {
		jmsTemplate.convertAndSend(message);
	}

}
