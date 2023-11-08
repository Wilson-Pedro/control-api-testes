package com.digytal.control.webservice.publico;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.digytal.control.infra.model.CredenciamentoResponse;
import com.digytal.control.infra.model.LoginRequest;
import com.digytal.control.infra.model.SessaoResponse;
import com.digytal.control.infra.model.usuario.UsuarioResponse;
import com.digytal.control.model.comum.cadastramento.CadastroSimplificadoRequest;
import com.digytal.control.model.modulo.acesso.usuario.UsuarioEntity;
import com.digytal.control.repository.modulo.acesso.UsuarioRepository;
import com.digytal.control.service.modulo.acesso.LoginService;
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
	
	LoginService loginService = mock(LoginService.class);
	
	UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	MockMvc mockMvc;
	
	String cpfCnpj = "46518629000129";
	
	CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
	
	CredenciamentoResponse responseExpected = new CredenciamentoResponse();
	
	UsuarioEntity entity = new UsuarioEntity();
	
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
		
		entity.setExpirado(false);
	}

	
//	@Test
//	void deveRealizarPrimeiroAcessoDaEmpresaComSucesso() throws Exception {
//		
//		final var cpfCnpj = "09495101000155";
//		
//		final var request = this.request;
//		request.setNomeFantasia("CANETAS RS");
//		request.setSobrenomeSocial("CANETAS RAFAEL SILVA");
//		request.setEmail("silva.rafael@hotmail.com.br");
//		
//		final var responseExpected = this.responseExpected;
//		responseExpected.setExpiracao(1698955585275L);
//		responseExpected.setUsuario(21);
//		responseExpected.setLogin(cpfCnpj);
//		responseExpected.setNome(request.getNomeFantasia());
//		responseExpected.setToken("5h7p9k2q");
//		
//		when(this.primeiroAcessoService.configurarPrimeiroAcesso(cpfCnpj, request))
//		.thenReturn(responseExpected);
//		
//		CredenciamentoResponse response = this.primeiroAcessoService.configurarPrimeiroAcesso(cpfCnpj, request);
//		
//		String jsonRequest = objectMapper.writeValueAsString(request);
//		
//		mockMvc.perform(post("/public/empresa/primeiro-acesso/{cpfCnpj}", cpfCnpj)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest)
//				.param("cpfCnpj", cpfCnpj))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
//		verify(this.primeiroAcessoService, times(1)).configurarPrimeiroAcesso(cpfCnpj, request);
//	}
	
//	@Test
//	void deveAlterarSenhaApartirDaExpiracaoComSucesso() throws Exception {
//		
//		SenhaAlteracaoRequest senhaAlterada = new SenhaAlteracaoRequest();
//		senhaAlterada.setUsuario(12);
//		senhaAlterada.setSenhaAtual("a529f572");
//		senhaAlterada.setNovaSenha("sEnh4Str0ng!");
//		senhaAlterada.setNovaSenhaConfirmacao("sEnh4Str0ng!");
//		
//		String jsonRequest = objectMapper.writeValueAsString(senhaAlterada);
//		Long expiracao = 1699385650862L;
//		
//		UsuarioEntity usuarioExpected = this.entity;
//		usuarioExpected.setSenha(senhaAlterada.getNovaSenha());
//		
//		when(usuarioRepository.save(usuarioExpected)).thenReturn(usuarioExpected);
//		
//		mockMvc.perform(patch("/public/alteracao-senha/{expiracao}", expiracao)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		UsuarioEntity usuario = this.usuarioRepository.save(usuarioExpected);
//		
//		assertEquals(senhaAlterada.getNovaSenha(), usuario.getSenha());
//	}
	
	@Test
	void deveRealizarLoginComSucesso() throws Exception {
		
		UsuarioResponse usuarioResponse = new UsuarioResponse();
		usuarioResponse.setId(2);
		usuarioResponse.setLogin("69059312000177");
		usuarioResponse.setNome("CALCADOS SA");
		usuarioResponse.setSobrenome("CALCADOS SOUSA ANDRADE");
		usuarioResponse.setEmail("sousa.andrade@hotmail.com.br");
		usuarioResponse.setBloqueado(false);
		usuarioResponse.setExpirado(false);
		usuarioResponse.setPerfil(null);
		
		SessaoResponse sessionExpected = new SessaoResponse();
		sessionExpected.setInicioSessao(LocalDateTime.now());
		sessionExpected.setFimSessao(sessionExpected.getInicioSessao().plusHours(4));
		sessionExpected.setUsuario(usuarioResponse);
		sessionExpected.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2OTA1OTMxMjAwMDE3NyIsImlhdCI6MTY5OTQ2MzU4NiwiZXhwIjoxNjk5NDc3OTg2LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sInVzdWFyaW8iOjIsImVtcHJlc2EiOjIsIm9yZ2FuaXphY2FvIjoyLCJ2YWxpZG8iOnRydWV9.RRQV_1W02z7otXWISq5nDeWEEsrApzZTiqpkCPgJhcXXX-wwJ1kJopdciFptq4mQRwYfnjDfdo-HWEH7El4E0A");
		
		LoginRequest login = new LoginRequest();
		login.setUsuario("69059312000177");
		login.setSenha("s3nh@F0rt3!");
		
		when(this.loginService.autenticar(login)).thenReturn(sessionExpected);
		
		String jsonRequest = objectMapper.writeValueAsString(login);
		
		mockMvc.perform(post("/public/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		final var session = loginService.autenticar(login);
		
		assertThat(session).usingRecursiveComparison().isEqualTo(sessionExpected);
		verify(loginService, times(1)).autenticar(login);
	}
	
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
