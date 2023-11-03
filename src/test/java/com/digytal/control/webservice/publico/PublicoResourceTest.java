package com.digytal.control.webservice.publico;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.digytal.control.infra.model.CredenciamentoResponse;
import com.digytal.control.model.comum.cadastramento.CadastroSimplificadoRequest;
import com.digytal.control.service.modulo.acesso.PrimeiroAcessoService;
import com.digytal.control.service.modulo.acesso.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PublicoResourceTest {
	
	@InjectMocks
	PublicoResource publicoResource;
	
	UsuarioService  usuarioService = mock(UsuarioService.class);

	PrimeiroAcessoService primeiroAcessoService = mock(PrimeiroAcessoService.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	MockMvc mockMvc;
	
	String cpfCnpj = "46518629000129";
	
	CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
	
	CredenciamentoResponse responseExpected = new CredenciamentoResponse();
	
	@BeforeEach
	void setup() {
		request.setNomeFantasia("BOLSAS BR");
		request.setSobrenomeSocial("BOLSAS BRASIL");
		request.setEmail("brasil.bolsas@hotmail.com.br");
		
		responseExpected.setExpiracao(1698955585275L);
		responseExpected.setUsuario(16);
		responseExpected.setLogin(cpfCnpj);
		responseExpected.setNome(request.getNomeFantasia());
		responseExpected.setToken("a04c5f3a");
	}

	
	@Test
	void deveRealizarPrimeiroAcessoDaEmpresaComSucesso() throws Exception {
		
		final var cpfCnpj = "31648098000111";
		
		final var request = this.request;
		request.setNomeFantasia("CONSOLES BR");
		request.setSobrenomeSocial("CONSOLES BRASIL");
		request.setEmail("brasil.consoles@hotmail.com.br");
		
		final var responseExpected = this.responseExpected;
		responseExpected.setExpiracao(1698955585275L);
		responseExpected.setUsuario(17);
		responseExpected.setLogin(cpfCnpj);
		responseExpected.setNome(request.getNomeFantasia());
		responseExpected.setToken("5h7p9k2q");
		
		when(this.primeiroAcessoService.configurarPrimeiroAcesso(cpfCnpj, request))
		.thenReturn(responseExpected);
		
		CredenciamentoResponse response = this.primeiroAcessoService.configurarPrimeiroAcesso(cpfCnpj, request);
		
		String jsonRequest = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/public/empresa/primeiro-acesso/{cpfCnpj}", cpfCnpj)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.param("cpfCnpj", cpfCnpj))
				.andExpect(status().isOk())
				.andReturn();
		
		assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
		verify(this.primeiroAcessoService, times(1)).configurarPrimeiroAcesso(cpfCnpj, request);
	}
	
//	@Test
//	void deveAlterarSenhaApartirDaExpiracaoComSucesso() throws Exception {
//		
//		SenhaAlteracaoRequest senhaAlterada = new SenhaAlteracaoRequest();
//		senhaAlterada.setUsuario(8);
//		senhaAlterada.setSenhaAtual("asd123");
//		senhaAlterada.setNovaSenha("Str0ngP@ss");
//		senhaAlterada.setNovaSenhaConfirmacao("Str0ngP@ss");
//		
//		String jsonRequest = objectMapper.writeValueAsString(senhaAlterada);
//		Long expiracao = 1698259381769L;
//		
//		mockMvc.perform(patch("/public/alteracao-senha/{expiracao}", expiracao)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andReturn();
//	}
	
	@Test
	void deveSolicitarNovaSenhaApartirDoIdComSucesso() throws Exception {
		
		Integer id = responseExpected.getUsuario();
		String login = cpfCnpj;
		
		when(this.usuarioService.solicitarNovaSenha(login)).thenReturn(responseExpected);
		
		mockMvc.perform(patch("/public/solicitacao-nova-senha/id/{id}", id))
		.andExpect(status().isOk())
		.andReturn();
		
		final var response = this.usuarioService.solicitarNovaSenha(login);
		
		assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
		verify(this.usuarioService, times(1)).solicitarNovaSenha(login);
	}
	
	@Test
	void deveSolicitarNovaSenhaApartirDoLoginComSucesso() throws Exception {
		
		String login = cpfCnpj;
		
		when(this.usuarioService.solicitarNovaSenha(login)).thenReturn(responseExpected);
		
		mockMvc.perform(patch("/public/solicitacao-nova-senha/login/{login}", login))
				.andExpect(status().isOk())
				.andReturn();
		
		final var response = this.usuarioService.solicitarNovaSenha(login);
		
		assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
		verify(this.usuarioService, times(1)).solicitarNovaSenha(login);
	}
}
