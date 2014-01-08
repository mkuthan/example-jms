package example.infrastructure.jms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import javax.jms.Destination;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Stopwatch;

import example.domain.example2.Example2Listener;
import example.domain.example2.Example2Message;
import example.domain.example2.Example2Publisher;

@JmsPublisherTest
public class JmsExample2PublisherTest extends AbstractTestNGSpringContextTests {

	private static final Example2Message ANY_MESSAGE = new Example2Message("any value");

	private static final int RECEIVE_TIMEOUT = 5000;

	@Autowired
	private Example2Publisher publisher;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("example2Dlq")
	private Destination dlq;

	@Autowired
	@Qualifier("example2Listener1")
	private Example2Listener listener1;

	@Autowired
	@Qualifier("example2Listener2")
	private Example2Listener listener2;

	private int expectedTotalRedeliveryTime = 3000;

	@Test
	public void listenersShouldHandleMessage() {
		// when
		publisher.publish(ANY_MESSAGE);

		// then
		verify(listener1, timeout(RECEIVE_TIMEOUT).times(1)).handleMessage(eq(ANY_MESSAGE));
		verify(listener2, timeout(RECEIVE_TIMEOUT).times(1)).handleMessage(eq(ANY_MESSAGE));
	}

	@Test(dependsOnMethods = "listenersShouldHandleMessage")
	public void messageShouldBeRedelivered() {
		// given (first & second calls: throws exception, third call: handles message)
		doThrow(new RuntimeException("#1")).doThrow(new RuntimeException("#2")).doNothing().when(listener1)
				.handleMessage(eq(ANY_MESSAGE));
		doThrow(new RuntimeException("#1")).doThrow(new RuntimeException("#2")).doNothing().when(listener2)
				.handleMessage(eq(ANY_MESSAGE));

		Stopwatch stopwatch = Stopwatch.createStarted();

		// when
		publisher.publish(ANY_MESSAGE);

		// then
		verify(listener1, timeout(RECEIVE_TIMEOUT).times(3)).handleMessage(eq(ANY_MESSAGE));
		verify(listener2, timeout(RECEIVE_TIMEOUT).times(3)).handleMessage(eq(ANY_MESSAGE));

		assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isGreaterThan(expectedTotalRedeliveryTime);
	}

	// TODO: fix DLQ configuration for topics
	@Test(dependsOnMethods = "messageShouldBeRedelivered", timeOut = RECEIVE_TIMEOUT, enabled = false)
	public void messageShouldBeInDlqAfterRedeliveries() {
		// given (always throws exception)
		doThrow(new RuntimeException()).when(listener1).handleMessage(eq(ANY_MESSAGE));
		doThrow(new RuntimeException()).when(listener2).handleMessage(eq(ANY_MESSAGE));

		Stopwatch stopwatch = Stopwatch.createStarted();

		// when
		publisher.publish(ANY_MESSAGE);

		Example2Message testMessage1 = (Example2Message) jmsTemplate.receiveAndConvert(dlq);
		assertThat(testMessage1).isEqualTo(ANY_MESSAGE);

		Example2Message testMessage2 = (Example2Message) jmsTemplate.receiveAndConvert(dlq);
		assertThat(testMessage2).isEqualTo(ANY_MESSAGE);

		assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isGreaterThan(expectedTotalRedeliveryTime);
	}

	@BeforeMethod
	void resetListener() {
		reset(listener1, listener2);
	}

	@Configuration
	public static class Config {

		@Bean
		public Example2Listener example2Listener1() {
			return Mockito.mock(Example2Listener.class);
		}

		@Bean
		public Example2Listener example2Listener2() {
			return Mockito.mock(Example2Listener.class);
		}
	}

}
