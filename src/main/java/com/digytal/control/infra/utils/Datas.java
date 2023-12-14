package com.digytal.control.infra.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class Datas {

	public static String intervalo(LocalDateTime dataInicial, LocalDateTime dataFinal) {

		Duration duracao = Duration.between(dataInicial, dataFinal);

		long horas = duracao.toHours();
		long minutos = duracao.toMinutes() % 60;
		long segundos = duracao.toSeconds() % 60;

		String intervalo = horas + ":" + minutos + ":" + segundos;

		return intervalo;
	}

	public static Long getDays(LocalDateTime dataInicial , LocalDateTime dataFinal) {

		Duration duracao = Duration.between(dataInicial, dataFinal);
		return Math.abs(duracao.toDays());
	}

	public static Long getHours(LocalDateTime dataInicial, LocalDateTime dataFinal) {

		Duration duracao = Duration.between(dataInicial, dataFinal);
		return Math.abs(duracao.toHours());
	}

	public static Long getMinutes(LocalDateTime dataInicial, LocalDateTime dataFinal) {

		Duration duracao = Duration.between(dataInicial, dataFinal);
		return Math.abs(duracao.toMinutes());
	}

	public static Long getSeconds(LocalDateTime dataInicial, LocalDateTime dataFinal) {

		Duration duracao = Duration.between(dataInicial, dataFinal);
		return Math.abs(duracao.toSeconds());
	}
	
	public static Long getMiliseconds(LocalDateTime dataInicial, LocalDateTime dataFinal) {

		Duration duracao = Duration.between(dataInicial, dataFinal);
		return Math.abs(duracao.toSeconds() * 1000);
	}
}