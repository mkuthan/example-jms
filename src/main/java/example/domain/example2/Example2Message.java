package example.domain.example2;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;

public class Example2Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String value;

	public Example2Message(String value) {
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

		Example2Message other = (Example2Message) obj;
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