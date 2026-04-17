package com.worklink.servicio_reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicioReservasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioReservasApplication.class, args);
	}

}
