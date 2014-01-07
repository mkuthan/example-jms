package example.infrastructure.jms;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ddd.infrastructure.jms.AbstractJmsPublisher;
import example.domain.example2.Example2Message;
import example.domain.example2.Example2Publisher;

@Component
public class JmsExample2Publisher extends AbstractJmsPublisher<Example2Message> implements Example2Publisher {

	@Autowired
	@Qualifier("example2Topic")
	private Destination destination;

	@Override
	protected Destination getDestination() {
		return destination;
	}

}
