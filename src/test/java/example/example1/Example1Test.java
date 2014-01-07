package example.example1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Stopwatch;

@ContextConfiguration(locations = { "classpath:/testContext.xml" })
public class Example1Test extends AbstractTestNGSpringContextTests {

	private static final Example1Message ANY_MESSAGE = new Example1Message("any value");

	private static final int RECEIVE_TIMEOUT = 5000;

	@Autowired
	@Qualifier("example1JmsTemplate")
	private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("example1DlqJmsTemplate")
	private JmsTemplate dlqJmsTemplate;

	@Autowired
	@Qualifier("example1Listener")
	private Example1Listener listener;

	private int expectedTotalRedeliveryTime = 3000;

	@Test
	public void listenerShouldHandleMessage() {
		// when
		jmsTemplate.convertAndSend(ANY_MESSAGE);

		// then
		verify(listener, timeout(RECEIVE_TIMEOUT)).handleMessage(eq(ANY_MESSAGE));
	}

	@Test(dependsOnMethods = "listenerShouldHandleMessage")
	public void messageShouldBeRedelivered() {
		// given (first & second calls: throws exception, third call: handles message)
		doThrow(new RuntimeException("#1")).doThrow(new RuntimeException("#2")).doNothing().when(listener)
				.handleMessage(eq(ANY_MESSAGE));

		Stopwatch stopwatch = Stopwatch.createStarted();

		// when
		jmsTemplate.convertAndSend(ANY_MESSAGE);

		// then
		verify(listener, timeout(RECEIVE_TIMEOUT).times(3)).handleMessage(eq(ANY_MESSAGE));

		assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isGreaterThan(expectedTotalRedeliveryTime);
	}

	@Test(dependsOnMethods = "messageShouldBeRedelivered", timeOut = RECEIVE_TIMEOUT)
	public void messageShouldBeInDlqAfterRedeliveries() {
		// given (always throws exception)
		doThrow(new RuntimeException()).when(listener).handleMessage(eq(ANY_MESSAGE));

		Stopwatch stopwatch = Stopwatch.createStarted();

		// when
		jmsTemplate.convertAndSend(ANY_MESSAGE);

		Example1Message testMessage = (Example1Message) dlqJmsTemplate.receiveAndConvert();
		assertThat(testMessage).isEqualTo(ANY_MESSAGE);

		assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)).isGreaterThan(expectedTotalRedeliveryTime);
	}

	@BeforeMethod
	void resetListener() {
		reset(listener);
	}

}
