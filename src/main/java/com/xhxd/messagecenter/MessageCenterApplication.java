package com.xhxd.messagecenter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationPid;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class MessageCenterApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder()
				.sources(MessageCenterApplication.class)
				.main(MessageCenterApplication.class)
				.run(args);
		log.info("----MessageCenterApplication Start PID={}----", new ApplicationPid().toString());
		context.registerShutdownHook();
	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(MessageCenterApplication.class);
	}
}