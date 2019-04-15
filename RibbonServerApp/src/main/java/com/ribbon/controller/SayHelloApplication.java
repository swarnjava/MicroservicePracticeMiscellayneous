package com.ribbon.controller;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SayHelloApplication {
	private static Logger log = LoggerFactory.getLogger(SayHelloApplication.class);
	
	@Autowired
	 Environment environment;
	
	
	@RequestMapping(value = "/greeting")
	  public String greet() {
	    log.info("Access /greeting");
	    String serverPort = environment.getProperty("local.server.port");
		 
        System.out.println("Port : " + serverPort);

	    List<String> greetings = Arrays.asList("Hi there", "Greetings", "Salutations");
	    Random rand = new Random();

	    int randomNum = rand.nextInt(greetings.size());
	    return greetings.get(randomNum)+" : "+serverPort;
	  }

	  @RequestMapping(value = "/")
	  public String home() {
	    log.info("Access /");
	    return "Hi!";
	  }

	  public static void main(String[] args) {
	    SpringApplication.run(SayHelloApplication.class, args);
	  }
}
