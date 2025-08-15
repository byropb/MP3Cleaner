package com.spring.mp3;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class Mp3CleanerApplication {
	private static final Logger logger = LoggerFactory.getLogger(Mp3CleanerApplication.class);

	public static void main(String[] args) {
		// SpringApplication.run(Mp3CleanerJspApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(Mp3CleanerApplication.class, args);
		Runtime rt = Runtime.getRuntime();
		// String url = "http://localhost:8082/mp3cleaner"; <-- current URL configured in application.properties as port + context-path
		try {
			String mp3Context = context.getEnvironment().getProperty("server.servlet.context-path");
			String myPort = context.getEnvironment().getProperty("server.port");
			String url = "http://localhost:".concat(myPort).concat(mp3Context);
			//open default browser with app url
			rt.exec("rundll32 url.dll,FileProtocolHandler ".concat(url));
			logger.info("my springboot version: ".concat(SpringVersion.getVersion()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
