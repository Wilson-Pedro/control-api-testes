package com.digytal.control.webservice.publico;

import static com.digytal.control.webservice.statics.LoginUniversal.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.digytal.control.repository.modulo.acesso.OrganizacaoRepository;
import com.digytal.control.repository.modulo.acesso.UsuarioRepository;
import com.digytal.control.repository.modulo.acesso.empresa.AplicacaoRepository;
import com.digytal.control.repository.modulo.acesso.empresa.ContaRepository;
import com.digytal.control.repository.modulo.acesso.empresa.EmpresaRepository;
import com.digytal.control.repository.modulo.acesso.empresa.FormaPagamentoRepository;
import com.digytal.control.service.modulo.acesso.EmpresaService;
import com.digytal.control.service.modulo.acesso.LoginService;
import com.digytal.control.service.modulo.acesso.PrimeiroAcessoService;
import com.digytal.control.service.modulo.acesso.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicoResourceTest {
	
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
	
	@Autowired
	FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	ContaRepository contaRepository;
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	@Autowired
	AplicacaoRepository aplicacaoRepository;
	
	@Autowired
	OrganizacaoRepository organizacaoRepository;
	
	CadastroSimplificadoRequest request = new CadastroSimplificadoRequest();
	
	CredenciamentoResponse configurarAcesso = new CredenciamentoResponse();
	
	@BeforeEach
	void setup() {
		request.setNomeFantasia("ROUPAS BR");
		request.setSobrenomeSocial("ROUPAS BRASIL");
		request.setEmail("brasil.roupas@hotmail.com.br");
	}
	
	@Test
	@Order(1)
	void deveDeletarTodosOsDadosDoBanco() {
		usuarioRepository.deleteAll();
		formaPagamentoRepository.deleteAll();
		contaRepository.deleteAll();
		empresaRepository.deleteAll();
		aplicacaoRepository.deleteAll();
		organizacaoRepository.deleteAll();
	}
	
	@Test
	@Order(2)
	void deveRealizarPrimeiroAcessoDaEmpresaComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.primeiroAcessoService.configurarPrimeiroAcesso(CPF_CNPJ, request);
		
		assertNotNull(response.getExpiracao());
		assertNotNull(response.getUsuario());
		assertNotNull(response.getLogin());
		assertNotNull(response.getNome());
		assertNotNull(response.getToken());
		assertTrue(response.getExpiracao() > 0);
		assertTrue(response.getUsuario() > 0);
		assertEquals(request.getNomeFantasia(), response.getNome());
	}
	
	@Test
	@Order(3)
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
	@Order(4)
	void deveSolicitarNovaSenhaApartirDoLoginComSucesso() throws Exception {
		
		mockMvc.perform(patch("/public/solicitacao-nova-senha/login/{login}", LOGIN))
				.andExpect(status().isOk())
				.andReturn();
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(LOGIN);
		
		assertNotNull(response.getExpiracao());
		assertNotNull(response.getUsuario());
		assertNotNull(response.getLogin());
		assertNotNull(response.getNome());
		assertNotNull(response.getToken());
		assertTrue(response.getExpiracao() > 0);
		assertTrue(response.getUsuario() > 0);
		assertEquals(LOGIN, response.getLogin());
	}
	
	@Test
	@Order(5)
	void deveSolicitarNovaSenhaApartirDoIdComSucesso() throws Exception {
		
		CredenciamentoResponse responseExpected = this.usuarioService.solicitarNovaSenha(LOGIN);
		Integer id = responseExpected.getUsuario();
		
		mockMvc.perform(patch("/public/solicitacao-nova-senha/id/{id}", id))
				.andExpect(status().isOk())
				.andReturn();
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(id);
		
		assertNotNull(response.getExpiracao());
		assertNotNull(response.getUsuario());
		assertNotNull(response.getLogin());
		assertNotNull(response.getNome());
		assertNotNull(response.getToken());
		assertTrue(response.getExpiracao() > 0);
		assertTrue(response.getUsuario() > 0);
		assertEquals(id, response.getUsuario());
		assertEquals(LOGIN, response.getLogin());
		
	}
	
	@Test
	@Order(6)
	void deveAlterarSenhaApartirDaExpiracaoComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(LOGIN);
		
		SenhaAlteracaoRequest request = new SenhaAlteracaoRequest();
		request.setUsuario(response.getUsuario());
		request.setSenhaAtual(response.getToken());
		request.setNovaSenha(SENHA);
		request.setNovaSenhaConfirmacao(SENHA);
		
		Long expiracao = response.getExpiracao();
		
		SessaoResponse senhaAlterada = usuarioService.alterarSenha(expiracao, request);
		UsuarioEntity entity = usuarioRepository.findByLogin(response.getLogin());
		
		boolean passwordOk = encoder.matches(SENHA, entity.getSenha());
		
		assertTrue(passwordOk);
		assertNotNull(entity.getSenha());
		assertNotNull(senhaAlterada.getToken());
		assertEquals(LOGIN, senhaAlterada.getUsuario().getLogin());
		
	}
	
	@Test
	@Order(7)
	void deveAlterarSenhaApartirDaExpiracaoAPartirDaRequesicaoComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(LOGIN);
		
		Long expiracao = response.getExpiracao();
		
		SenhaAlteracaoRequest request = new SenhaAlteracaoRequest();
		request.setUsuario(response.getUsuario());
		request.setSenhaAtual(response.getToken());
		request.setNovaSenha(SENHA);
		request.setNovaSenhaConfirmacao(SENHA);
		
		String jsonRequest = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(patch("/public/alteracao-senha/{expiracao}", expiracao)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	@Order(8)
	void deveRealizarLoginComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(LOGIN);
		
		SenhaAlteracaoRequest request = new SenhaAlteracaoRequest();
		request.setUsuario(response.getUsuario());
		request.setSenhaAtual(response.getToken());
		request.setNovaSenha(SENHA);
		request.setNovaSenhaConfirmacao(SENHA);
		
		Long expiracao = response.getExpiracao();
		
		SessaoResponse senhaAlterada = usuarioService.alterarSenha(expiracao, request);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsuario(response.getLogin());
		loginRequest.setSenha(SENHA);
		
		SessaoResponse sessaoResponse = loginService.autenticar(loginRequest);
		TOKEN = sessaoResponse.getToken();
		
		String jsonRequest = objectMapper.writeValueAsString(loginRequest);
		
		mockMvc.perform(post("/public/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotNull(senhaAlterada.getToken());
		assertNotNull(TOKEN);
		assertTrue(response.getUsuario() > 0);
	}
	
	
	@Test
	@Order(9)
	void deveSelecionarEmpresaComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(LOGIN);
		
		Integer empresa = response.getUsuario() + 1;
		
		String newTOken = this.empresaService.selecionarEmpresa(empresa, TOKEN);
		
		mockMvc.perform(get("/public/empresas/selecao/{empresa}", empresa)
				.header("authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotNull(newTOken);
	}
}