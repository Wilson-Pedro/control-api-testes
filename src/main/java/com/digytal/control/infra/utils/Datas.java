package com.digytal.control.infra.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Datas {

	public static String intervalo(String registroEntrada, String registroSaida) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dataHora1 = LocalDateTime.parse(registroEntrada, formatter);
		LocalDateTime dataHora2 = LocalDateTime.parse(registroSaida, formatter);

		Duration duracao = Duration.between(dataHora1, dataHora2);

		long horas = duracao.toHours();
		long minutos = duracao.toMinutes() % 60;
		long segundos = duracao.toSeconds() % 60;

		String intervalo = horas + ":" + minutos + ":" + segundos;

		return intervalo;
	}

	public static String intervaloPersonalidado(String registroEntrada, String registroSaida,
			DateTimeFormatter formatter) {

		LocalDateTime dataHora1 = LocalDateTime.parse(registroEntrada, formatter);
		LocalDateTime dataHora2 = LocalDateTime.parse(registroSaida, formatter);

		Duration duracao = Duration.between(dataHora1, dataHora2);

		long horas = duracao.toHours();
		long minutos = duracao.toMinutes() % 60;
		long segundos = duracao.toSeconds() % 60;

		String intervalo = horas + ":" + minutos + ":" + segundos;

		return intervalo;
	}

	public static Long getDays(String dataVencimento, String dataAtual) {
		
		LocalDateTime data1 = toLocalDateTime(dataVencimento);
		LocalDateTime data2 = toLocalDateTime(dataAtual);

		Duration duracao = Duration.between(data1, data2);
		return Math.abs(duracao.toDays());
	}

	public static Long getHours(String dataVencimento, String dataAtual) {
		
		LocalDateTime data1 = toLocalDateTime(dataVencimento);
		LocalDateTime data2 = toLocalDateTime(dataAtual);

		Duration duracao = Duration.between(data1, data2);
		return Math.abs(duracao.toHours());
	}

	public static Long getMinutes(String dataVencimento, String dataAtual) {
		
		LocalDateTime data1 = toLocalDateTime(dataVencimento);
		LocalDateTime data2 = toLocalDateTime(dataAtual);

		Duration duracao = Duration.between(data1, data2);
		return Math.abs(duracao.toMinutes());
	}

	public static Long getSeconds(String dataVencimento, String dataAtual) {
		
		LocalDateTime data1 = toLocalDateTime(dataVencimento);
		LocalDateTime data2 = toLocalDateTime(dataAtual);

		Duration duracao = Duration.between(data1, data2);
		return Math.abs(duracao.toSeconds());
	}
	
	private static LocalDateTime toLocalDateTime(String data) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(data, formatter);
		
		return localDateTime;
	}
}
