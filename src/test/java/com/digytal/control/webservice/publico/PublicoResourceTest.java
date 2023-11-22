package com.digytal.control.webservice.publico;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicoResourceTest {
	
	static String TOKEN;
	String cpfCnpj = "76001832000194";
	String novaSenha = "s3nh@Forte2!";
	String login = this.cpfCnpj;
	
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
	
	@Autowired
	EmpresaService empresaService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	MockMvc mockMvc;
	
	CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
	
	@BeforeEach
	void setup() {
		request.setNomeFantasia("ROUPAS BR");
		request.setSobrenomeSocial("ROUPAS BRASIL");
		request.setEmail("brasil.roupas@hotmail.com.br");
		
	}
	
	@Test
	@Order(1)
	void deveRealizarPrimeiroAcessoDaEmpresaComSucesso() throws Exception {
		
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
	@Order(2)
	void deveRealizarPrimeiroAcessoDaEmpresaAPartirDaRequesicaoComSucesso() throws Exception {
		String cpfCnpj = "06684753000140";
		
		CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
		request.setNomeFantasia("PERFUMES BR");
		request.setSobrenomeSocial("PERFUMES BRASIL");
		request.setEmail("brasil.perfumes@hotmail.com.br");
		
		String jsonRequest = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/public/empresa/primeiro-acesso/{cpfCnpj}", cpfCnpj)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest)
				.param("cpfCnpj", cpfCnpj))
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(3)
	void deveAlterarSenhaApartirDaExpiracaoComSucesso() throws Exception {
		
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
		
		assertTrue(passwordOk);
		assertNotEquals(null, entity.getSenha());
		assertNotEquals(null, senhaAlterada.getToken());
		assertEquals(login, senhaAlterada.getUsuario().getLogin());
		
	}
	
	@Test
	@Order(4)
	void deveAlterarSenhaApartirDaExpiracaoAPartirDaRequesicaoComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(login);
		
		Long expiracao = response.getExpiracao();
		
		SenhaAlteracaoRequest request = new SenhaAlteracaoRequest();
		request.setUsuario(response.getUsuario());
		request.setSenhaAtual(response.getToken());
		request.setNovaSenha(novaSenha);
		request.setNovaSenhaConfirmacao(novaSenha);
		
		String jsonRequest = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(patch("/public/alteracao-senha/{expiracao}", expiracao)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(5)
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
		this.TOKEN = sessaoResponse.getToken();
		
		String jsonRequest = objectMapper.writeValueAsString(loginRequest);
		
		mockMvc.perform(post("/public/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotEquals(null, senhaAlterada.getToken());
		assertNotEquals(null, TOKEN);
		assertTrue(response.getUsuario() > 0);
	}
	
	
	@Test
	@Order(6)
	void deveSelecionarEmpresaComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(login);
		
		Integer empresa = response.getUsuario() + 1;
		
		String newTOken = this.empresaService.selecionarEmpresa(empresa, this.TOKEN);
		
		mockMvc.perform(get("/public/empresas/selecao/{empresa}", empresa)
				.header("authorization", "Bearer " + this.TOKEN))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotEquals(null, newTOken);
	}

	@Test
	@Order(7)
	void deveSolicitarNovaSenhaApartirDoIdComSucesso() throws Exception {
		
		CredenciamentoResponse responseExpected = this.usuarioService.solicitarNovaSenha(login);
		Integer id = responseExpected.getUsuario();
		String login = cpfCnpj;
		
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
		
	}

	@Test
	@Order(8)
	void deveSolicitarNovaSenhaApartirDoLoginComSucesso() throws Exception {
		
		String login = this.cpfCnpj;
		
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
	}
}
