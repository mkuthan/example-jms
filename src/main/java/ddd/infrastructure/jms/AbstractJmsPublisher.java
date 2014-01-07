package ddd.infrastructure.jms;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import ddd.domain.Publisher;

public abstract class AbstractJmsPublisher<T> implements Publisher<T> {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void publish(T message) {
		jmsTemplate.convertAndSend(getDestination(), message);
	}

	protected abstract Destination getDestination();

}
