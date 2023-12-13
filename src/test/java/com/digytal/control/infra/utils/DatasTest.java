package com.digytal.control.infra.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DatasTest {

	@Test
	void devePegarOsDiasDoIntervaloEntreAsDatas() {
		
		String dataVencimento = "01/10/2023 10:00:00";
		String dataAtual = "04/10/2023 10:00:00";
		
		long dias = Datas.getDays(dataVencimento, dataAtual);
		
		assertEquals(dias, 3);
	}
	
	@Test
	void devePegarAsHorasDoIntervaloEntreAsDatas() {
		
		String dataVencimento = "01/10/2023 10:00:00";
		String dataAtual = "01/10/2023 15:00:00";
		
		long horas = Datas.getHours(dataVencimento, dataAtual);
		
		assertEquals(horas, 5);
	}
	
	@Test
	void devePegarOsMinutosDoIntervaloEntreAsDatas() {
		
		String dataVencimento = "01/10/2023 10:00:00";
		String dataAtual = "01/10/2023 10:30:00";

		long minutos = Datas.getMinutes(dataVencimento, dataAtual);
		
		assertEquals(minutos, 30);
	}
	
	@Test
	void devePegarOsSegundosDoIntervaloEntreAsDatas() {
		
		String dataVencimento = "01/10/2023 10:00:00";
		String dataAtual = "01/10/2023 10:00:20";
		
		long secundos = Datas.getSeconds(dataVencimento, dataAtual);
		
		assertEquals(secundos, 20);
	}
}
