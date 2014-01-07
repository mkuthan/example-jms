package example.domain.example1;

import ddd.domain.Publisher;

public interface Example1Publisher extends Publisher<Example1Message> {
	void publish(Example1Message message);
}
