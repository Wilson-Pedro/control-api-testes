package com.digytal.control.infra.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

class DatasTest {

	@Test
	void devePegarOsDiasDoIntervaloEntreAsDatas() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String registroEntrada = "01/10/2023 10:00:00";
		String registroSaida = "04/10/2023 10:00:00";
		
		LocalDateTime dataHora1 = LocalDateTime.parse(registroEntrada, formatter);
		LocalDateTime dataHora2 = LocalDateTime.parse(registroSaida, formatter);
		
		Long dias = Datas.getDays(dataHora1, dataHora2);
		
		assertEquals(dias, 3);
	}
	
	@Test
	void devePegarAsHorasDoIntervaloEntreAsDatas() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String registroEntrada = "01/10/2023 10:00:00";
		String registroSaida = "01/10/2023 15:00:00";
		
		LocalDateTime dataHora1 = LocalDateTime.parse(registroEntrada, formatter);
		LocalDateTime dataHora2 = LocalDateTime.parse(registroSaida, formatter);
		
		long horas = Datas.getHours(dataHora1, dataHora2);
		
		assertEquals(horas, 5);
	}
	
	@Test
	void devePegarOsMinutosDoIntervaloEntreAsDatas() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String registroEntrada = "01/10/2023 10:00:00";
		String registroSaida = "01/10/2023 10:30:00";
		
		LocalDateTime dataHora1 = LocalDateTime.parse(registroEntrada, formatter);
		LocalDateTime dataHora2 = LocalDateTime.parse(registroSaida, formatter);
		
		long minutos = Datas.getMinutes(dataHora1, dataHora2);
		
		assertEquals(minutos, 30);
	}
	
	@Test
	void devePegarOsSegundosDoIntervaloEntreAsDatas() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String registroEntrada = "01/10/2023 10:00:00";
		String registroSaida = "01/10/2023 10:00:20";
		
		LocalDateTime dataHora1 = LocalDateTime.parse(registroEntrada, formatter);
		LocalDateTime dataHora2 = LocalDateTime.parse(registroSaida, formatter);
		
		long segundos = Datas.getSeconds(dataHora1, dataHora2);
		
		assertEquals(segundos, 20);
	}
	
	@Test
	void devePegarOsMilisegundosDoIntervaloEntreAsDatas() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String registroEntrada = "01/10/2023 10:00:00";
		String registroSaida = "01/10/2023 10:00:01";
		
		LocalDateTime dataHora1 = LocalDateTime.parse(registroEntrada, formatter);
		LocalDateTime dataHora2 = LocalDateTime.parse(registroSaida, formatter);
		
		long milisegundos = Datas.getMiliseconds(dataHora1, dataHora2);
		
		assertEquals(milisegundos, 1000);
	}

}