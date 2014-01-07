package example.infrastructure.jms;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import ddd.infrastructure.DddConfig;

@Configuration
@ComponentScan
@Import({ DddConfig.class })
@ImportResource({ "classpath:/applicationContext-infrastructure-jms.xml", "classpath:/applicationContext-example1.xml",
		"classpath:/applicationContext-example2.xml" })
public class JmsConfig {
}
