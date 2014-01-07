package example.infrastructure.jms;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ddd.infrastructure.jms.AbstractJmsPublisher;
import example.domain.example1.Example1Message;
import example.domain.example1.Example1Publisher;

@Component
public class JmsExample1Publisher extends AbstractJmsPublisher<Example1Message> implements Example1Publisher {

	@Autowired
	@Qualifier("example1Queue")
	private Destination destination;

	@Override
	protected Destination getDestination() {
		return destination;
	}

}
