package example.example1;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;

public class Example1Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String value;

	public Example1Message(String value) {
		this.value = requireNonNull(value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Example1Message other = (Example1Message) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return value;
	}
}