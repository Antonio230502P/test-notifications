package com.trainibit.test_notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestNotificationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestNotificationsApplication.class, args);
		System.out.println("Escuchando mensajes del topico users_registered");
	}

}
