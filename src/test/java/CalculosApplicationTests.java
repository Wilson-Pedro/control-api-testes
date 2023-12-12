import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.digytal.control.infra.utils.Calculos;


class CalculosApplicationTests {

	BigDecimal numB = new BigDecimal("10.5432");
	Double num1 = 10.0;
	Double num2 = 5.0;

	@Test
	void deveSomarDoisValoresComSucesso() {
		Double resultado = Calculos.somar(num1, num2);
		
		assertNotNull(resultado);
		assertEquals(15.0, resultado);
	}
	
	@Test
	void deveSubtrairDoisValoresComSucesso() {
		Double resultado = Calculos.subtrair(num1, num2);
		
		assertNotNull(resultado);
		assertEquals(5.0, resultado);
	}
	
	@Test
	void deveMultiplicarDoisValoresComSucesso() {
		Double resultado = Calculos.multiplicar(num1, num2);
		
		assertNotNull(resultado);
		assertEquals(50.0, resultado);
	}
	
	@Test
	void deveDividirDoisValoresComSucesso() {
		Double resultado = Calculos.dividir(num1, num2);
		
		assertNotNull(resultado);
		assertEquals(2.0, resultado);
	}

	@Test
	void deveCalcularPorcentagemComDoubleComSucesso() {
		Double valor = 200.0;
		Double aliquota = 10.0;
		Double resultado = Calculos.calcularPorcentagem(valor, aliquota);
		
		assertNotNull(resultado);
		assertEquals(20.0, resultado);
	}
	
	@Test
	void deveCalcularPorcentagemComBigDecimalComSucesso() {
		BigDecimal valor = new BigDecimal("200.0");
		BigDecimal aliquota = BigDecimal.TEN;
		BigDecimal resultado = Calculos.calcularPorcentagem(valor, aliquota);
		BigDecimal resultadoEsperado = new BigDecimal("20.00");
		
		assertNotNull(resultado);
		assertEquals(resultadoEsperado, resultado);
	}
	
	@Test
	void deveCompararIgualdadeComSucesso() {
		num1 = 10.0;
		num2 = 10.0;
		boolean resultado = Calculos.compararIgualdade(num1, num2);
		
		assertTrue(resultado);
		assertEquals(num1, num2);
	}
	
	@Test
	void naoDeveCompararIgualdadeComSucesso() {
		num1 = 12.0;
		num2 = 10.0;
		boolean resultado = Calculos.compararIgualdade(num1, num2);
		
		assertFalse(resultado);
		assertNotEquals(num1, num2);
	}
	
	@Test
	void deveCompararMaiorQueComSucesso() {
		num1 = 12.0;
		num2 = 10.0;
		boolean resultado = Calculos.compararMaiorQue(num1, num2);
		
		assertTrue(resultado);
		assertTrue(num1 > num2);
	}
	
	@Test
	void deveCompararMenorQueComSucesso() {
		num1 = 12.0;
		num2 = 10.0;
		boolean resultado = Calculos.compararMenorQue(num2, num1);
		
		assertTrue(resultado);
		assertTrue(num2 < num1);
	}
	
	@Test
	void deveCompararMaiorQueZeroComSucesso() {
		num1 = 12.0;
		boolean resultado = Calculos.compararMaiorQueZero(num1);
		
		assertTrue(resultado);
		assertTrue(num1 > 0.0);
	}
	
	@Test
	void deveCompararMenorQueZeroComSucesso() {
		num1 = -1.0;
		boolean resultado = Calculos.compararMaiorQueZero(num1);
		
		assertFalse(resultado);
		assertTrue(num1 < 0.0);
	}
	
	@Test
	void deveCompararIgualOuMeorQueZeroComSucesso() {
		num1 = -1.0;
		boolean resultado = Calculos.compararIgualMenorZero(num1);
		
		assertTrue(resultado);
		assertTrue(num1 < 0.0);
	}
	
	@Test
	void deveAplicarEscalaComDoubleComSucesso() {
		num1 = 10.5432;
		Double resultado = Calculos.aplicarEscala(num1);
		
		assertNotNull(resultado);
		assertEquals(10.54, resultado);
	}
	
	@Test
	void deveAplicarEscalaTresComDoubleComTresSucesso() {
		num1 = 10.5432;
		Double resultado = Calculos.aplicarEscala(3, num1);
		
		assertNotNull(resultado);
		assertEquals(10.543, resultado);
	}
	
	@Test
	void deveAplicarEscalaQuadroComDoubleComSucesso() {
		num1 = 10.5432;
		Double resultado = Calculos.aplicarEscala4(num1);
		
		assertNotNull(resultado);
		assertEquals(10.5432, resultado);
	}
	
	//
	@Test
	void deveAplicarEscalaComBigDecimalComSucesso() {
		BigDecimal resultado = Calculos.aplicarEscala(numB);
		BigDecimal resultadoEsperado = new BigDecimal("10.54");
		
		assertNotNull(resultado);
		assertEquals(resultadoEsperado, resultado);
	}
	
	@Test
	void deveAplicarEscalaTresComBigDecimalComTresSucesso() {
		BigDecimal resultado = Calculos.aplicarEscala(3, numB);
		BigDecimal resultadoEsperado = new BigDecimal("10.543");
		
		assertNotNull(resultado);
		assertEquals(resultadoEsperado, resultado);
	}
	
	@Test
	void deveAplicarEscalaQuadroComBigDecimalComSucesso() {
		BigDecimal resultado = Calculos.aplicarEscala4(numB);
		BigDecimal resultadoEsperado = new BigDecimal("10.5432");
		
		assertNotNull(resultado);
		assertEquals(resultadoEsperado, resultado);
	}
	
	@Test
	void seONumeroNaoForNuloDeveRetornarEleMesmo() {
		num1 = 20.0;
		num2 = 10.0;
		Double resultado = Calculos.seNuloOuZero(num1, num2);
		
		assertNotNull(resultado);
		assertNotEquals(10.0, num1);
		assertEquals(20.0, num1);
	}
	
	@Test
	void seONumeroForNuloDeveRetornarOutroValor() {
		num1 = 0.0;
		num2 = 10.0;
		Double resultado = Calculos.seNuloOuZero(num1, num2);
		
		assertNotNull(resultado);
		assertNotEquals(10.0, num1);
		assertEquals(10.0, num2);
	}
	
	@Test
	void seONumeroForNuloDeveZerarOValorDouble() {
		num1 = 0.0;
		Double resultado = Calculos.seNuloZera(num1);
		
		assertEquals(0.0, resultado);
	}
	
	@Test
	void seONumeroNaoForNuloNaoDeveZerarOValorDouble() {
		num1 = 10.0;
		Double resultado = Calculos.seNuloZera(num1);
		
		assertEquals(10.0, resultado);
	}
	
	@Test
	void seONumeroNaoForNuloDeveRetornarEleMesmoBigDecimal() {
		BigDecimal vinte = new BigDecimal("20.0");
		BigDecimal dez = BigDecimal.TEN;
		BigDecimal resultado = Calculos.seNuloOuZero(dez, vinte);
		BigDecimal resultadoEsperado = new BigDecimal("10");
		
		assertNotEquals(vinte, dez);
		assertEquals(resultadoEsperado, resultado);
	}
	
	@Test
	void seONumeroForNuloDeveRetornarOutroValorBigDecimal() {
		BigDecimal vinte = new BigDecimal("20.0");
		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal resultado = Calculos.seNuloOuZero(zero, vinte);
		BigDecimal resultadoEsperado = new BigDecimal("20.0");
		
		assertNotEquals(vinte, zero);
		assertEquals(resultadoEsperado, resultado);
	}
	
	@Test
	void deveNegativarOValorComSucesso() {
		num1 = 10.0;
		Double resultado = Calculos.negativar(10.0, true);
		
		assertNotEquals(10.0, resultado);
		assertTrue(resultado < 0);
		assertEquals(-10.0, resultado);
	}
	
	@Test
	void naoDeveNegativarOValorComSucesso() {
		num1 = 10.0;
		Double resultado = Calculos.negativar(10.0, false);
		
		assertNotEquals(-10.0, resultado);
		assertFalse(resultado < 0);
		assertEquals(10.0, resultado);
	}

}
