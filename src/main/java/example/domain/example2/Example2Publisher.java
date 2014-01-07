package example.domain.example2;

import ddd.domain.Publisher;

public interface Example2Publisher extends Publisher<Example2Message> {
	void publish(Example2Message message);
}
