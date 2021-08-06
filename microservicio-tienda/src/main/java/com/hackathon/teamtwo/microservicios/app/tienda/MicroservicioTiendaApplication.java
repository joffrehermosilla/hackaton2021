package com.hackathon.teamtwo.microservicios.app.tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.hackathon.teamtwo.microservicios.commons.usuarios.models.entity"})
public class MicroservicioTiendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioTiendaApplication.class, args);
	}

}
