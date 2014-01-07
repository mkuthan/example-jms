package ddd.domain;

public interface Publisher<T> {
	void publish(T message);
}
