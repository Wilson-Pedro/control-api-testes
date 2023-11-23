package com.digytal.control.webservice.publico;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.digytal.control.infra.model.CredenciamentoResponse;
import com.digytal.control.model.comum.cadastramento.CadastroSimplificadoRequest;
import com.digytal.control.service.modulo.acesso.PrimeiroAcessoService;
import com.digytal.control.service.modulo.acesso.UsuarioService;
import static com.digytal.control.webservice.LoginUniversal.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicTest {
	
	@Autowired
	PrimeiroAcessoService primeiroAcessoService;
	
	@Autowired
	UsuarioService  usuarioService;
	
	@Autowired
	private MockMvc mockMvc;
	
	static CredenciamentoResponse RESPONSE = new CredenciamentoResponse();
	
	@Test
	@Order(1)
	void deveRealizarPrimeiroAcessoDaEmpresaComSucesso() throws Exception {
		
		CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
		request.setNomeFantasia("CALÇADOS BR");
		request.setSobrenomeSocial("CALÇADOS BRAZIL");
		request.setEmail("brazil.calcados@hotmail.com.br");
		
		CredenciamentoResponse response = this.primeiroAcessoService.configurarPrimeiroAcesso(CPF_CNPJ, request);
		
		assertNotEquals(null, response.getExpiracao());
		assertNotEquals(null, response.getUsuario());
		assertNotEquals(null, response.getLogin());
		assertNotEquals(null, response.getNome());
		assertNotEquals(null, response.getToken());
		assertTrue(response.getExpiracao() > 0);
		assertTrue(response.getUsuario() > 0);
		assertEquals(request.getNomeFantasia(), response.getNome());
	}
	
	@Test
	@Order(2)
	void deveSolicitarNovaSenhaApartirDoLoginComSucesso() throws Exception {
		
		mockMvc.perform(patch("/public/solicitacao-nova-senha/login/{login}", LOGIN))
				.andExpect(status().isOk())
				.andReturn();
		
		RESPONSE = this.usuarioService.solicitarNovaSenha(LOGIN);
		
		assertNotEquals(null, RESPONSE.getExpiracao());
		assertNotEquals(null, RESPONSE.getUsuario());
		assertNotEquals(null, RESPONSE.getLogin());
		assertNotEquals(null, RESPONSE.getNome());
		assertNotEquals(null, RESPONSE.getToken());
		assertTrue(RESPONSE.getExpiracao() > 0);
		assertTrue(RESPONSE.getUsuario() > 0);
		assertEquals(LOGIN, RESPONSE.getLogin());
	}
	
	@Test
	@Order(3)
	void deveSolicitarNovaSenhaApartirDoIdComSucesso() throws Exception {
		
		Integer id = RESPONSE.getUsuario();
		
		mockMvc.perform(patch("/public/solicitacao-nova-senha/id/{id}", id))
				.andExpect(status().isOk())
				.andReturn();
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(id);
		
		assertNotEquals(null, response.getExpiracao());
		assertNotEquals(null, response.getUsuario());
		assertNotEquals(null, response.getLogin());
		assertNotEquals(null, response.getNome());
		assertNotEquals(null, response.getToken());
		assertTrue(response.getExpiracao() > 0);
		assertTrue(response.getUsuario() > 0);
		assertEquals(id, response.getUsuario());
		assertEquals(LOGIN, response.getLogin());
		
	}

}
