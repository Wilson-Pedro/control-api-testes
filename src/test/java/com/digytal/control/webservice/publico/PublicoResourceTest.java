package com.digytal.control.webservice.publico;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.digytal.control.infra.model.CredenciamentoResponse;
import com.digytal.control.infra.model.LoginRequest;
import com.digytal.control.infra.model.SessaoResponse;
import com.digytal.control.infra.model.usuario.UsuarioResponse;
import com.digytal.control.model.comum.cadastramento.CadastroSimplificadoRequest;
import com.digytal.control.model.modulo.acesso.usuario.SenhaAlteracaoRequest;
import com.digytal.control.model.modulo.acesso.usuario.UsuarioEntity;
import com.digytal.control.repository.modulo.acesso.UsuarioRepository;
import com.digytal.control.service.modulo.acesso.EmpresaService;
import com.digytal.control.service.modulo.acesso.LoginService;
import com.digytal.control.service.modulo.acesso.PrimeiroAcessoService;
import com.digytal.control.service.modulo.acesso.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PublicoResourceTest {
	
	String cpfCnpj = "52340615000160";
	String novaSenha = "s3nh@Forte2!";
	
	@InjectMocks
	PublicoResource publicoResource;
	
	@Autowired
	UsuarioService  usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
    PasswordEncoder encoder;

	@Autowired
	PrimeiroAcessoService primeiroAcessoService;
	
	@Autowired
	LoginService loginService;
	
	EmpresaService empresaService = mock(EmpresaService.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	MockMvc mockMvc;
	
	CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
	
	@BeforeEach
	void setup() {
		request.setNomeFantasia("SUCOS MT");
		request.setSobrenomeSocial("SUCOS MARIA TEREZA");
		request.setEmail("maria.tereza@hotmail.com.br");
		
	}
	
	@Test
	void deveRealizarPrimeiroAcessoDaEmpresaComSucesso() throws Exception {
		
		String cpfCnpj = "36605972000157";
		
		CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
		request.setNomeFantasia("RADIOS BR");
		request.setSobrenomeSocial("RADIOS BRUNO RIBEIRO");
		request.setEmail("ribeiro.bruno@hotmail.com.br");
		
//		String jsonRequest = objectMapper.writeValueAsString(request);
//		
//		mockMvc.perform(post("/public/empresa/primeiro-acesso/{cpfCnpj}", cpfCnpj)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest)
//				.param("cpfCnpj", cpfCnpj))
//				.andExpect(status().isOk())
//				.andReturn();
		
		CredenciamentoResponse response = this.primeiroAcessoService.configurarPrimeiroAcesso(cpfCnpj, request);
		
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
	void deveAlterarSenhaApartirDaExpiracaoComSucesso() throws Exception {
		
		String login = this.cpfCnpj;
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(login);
		
		SenhaAlteracaoRequest request = new SenhaAlteracaoRequest();
		request.setUsuario(response.getUsuario());
		request.setSenhaAtual(response.getToken());
		request.setNovaSenha(novaSenha);
		request.setNovaSenhaConfirmacao(novaSenha);
		
		Long expiracao = response.getExpiracao();
		
		SessaoResponse senhaAlterada = usuarioService.alterarSenha(expiracao, request);
		UsuarioEntity entity = usuarioRepository.findByLogin(response.getLogin());
		
		boolean passwordOk = encoder.matches(novaSenha, entity.getSenha());
		
//		String jsonRequest = objectMapper.writeValueAsString(request);
//		
//		mockMvc.perform(patch("/public/alteracao-senha/{expiracao}", expiracao)
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isOk())
//				.andReturn();
		
		assertTrue(passwordOk);
		assertNotEquals(null, entity.getSenha());
		assertNotEquals(null, senhaAlterada.getToken());
		assertEquals(login, senhaAlterada.getUsuario().getLogin());
		
	}
	
	@Test
	void deveRealizarLoginComSucesso() throws Exception {
		
		String login = this.cpfCnpj;
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(login);
		
		SenhaAlteracaoRequest request = new SenhaAlteracaoRequest();
		request.setUsuario(response.getUsuario());
		request.setSenhaAtual(response.getToken());
		request.setNovaSenha(novaSenha);
		request.setNovaSenhaConfirmacao(novaSenha);
		
		Long expiracao = response.getExpiracao();
		
		SessaoResponse senhaAlterada = usuarioService.alterarSenha(expiracao, request);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsuario(response.getLogin());
		loginRequest.setSenha(novaSenha);
		
		SessaoResponse sessaoResponse = loginService.autenticar(loginRequest);
		
		String jsonRequest = objectMapper.writeValueAsString(loginRequest);
		
		mockMvc.perform(post("/public/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotEquals(null, senhaAlterada.getToken());
		assertTrue(response.getUsuario() > 0);
	}
	
	
//	@Test
//	void deveSelecionarEmpresaComSucesso() throws Exception {
//		
//		Integer empresa = 2;
//		String tokenExpected = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2OTA1OTMxMjAwMDE3NyIsImlhdCI6MTY5OTcwNDg2NCwiZXhwIjoxNjk5NzE5MjY0LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sInVzdWFyaW8iOjIsImVtcHJlc2EiOjIsIm9yZ2FuaXphY2FvIjoyLCJ2YWxpZG8iOnRydWV9.uZ9XSGTcvCeefoQAiewEU-XBaI2Pva8IFRq5pztcL66iNLwziGJLOFAzRwF2OSegr9seEJF-zDXPCcp9-LxC4A";
//		
//		when(this.empresaService.selecionarEmpresa(empresa, tokenExpected)).thenReturn(tokenExpected);
//		
//		mockMvc.perform(get("/public/empresas/selecao/{empresa}", empresa)
//				.header("authorization", "Bearer " + tokenExpected))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		String token = this.empresaService.selecionarEmpresa(empresa, tokenExpected);
//		
//		assertEquals(token, tokenExpected);
//		verify(empresaService, times(1)).selecionarEmpresa(empresa, token);
//	}

	@Test
	void deveSolicitarNovaSenhaApartirDoIdComSucesso() throws Exception {
		
		Integer id = 134;
		String login = cpfCnpj;
		
		//when(this.usuarioService.solicitarNovaSenha(login)).thenReturn(responseExpected);
		
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
		assertEquals(login, response.getLogin());
		
//		assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
//		verify(this.usuarioService, times(1)).solicitarNovaSenha(login);
	}

	@Test
	void deveSolicitarNovaSenhaApartirDoLoginComSucesso() throws Exception {
		
		String login = this.cpfCnpj;
		
		//when(this.usuarioService.solicitarNovaSenha(login)).thenReturn(response);
		
		mockMvc.perform(patch("/public/solicitacao-nova-senha/login/{login}", login))
				.andExpect(status().isOk())
				.andReturn();
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(login);
		
		assertNotEquals(null, response.getExpiracao());
		assertNotEquals(null, response.getUsuario());
		assertNotEquals(null, response.getLogin());
		assertNotEquals(null, response.getNome());
		assertNotEquals(null, response.getToken());
		assertTrue(response.getExpiracao() > 0);
		assertTrue(response.getUsuario() > 0);
		assertEquals(login, response.getLogin());
		
		//assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
		//verify(this.usuarioService, times(1)).solicitarNovaSenha(login);
	}
}
