package com.digytal.control.webservice.publico;

import static com.digytal.control.webservice.statics.LoginUniversal.CPF_CNPJ;
import static com.digytal.control.webservice.statics.LoginUniversal.LOGIN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
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
import com.digytal.control.repository.modulo.acesso.OrganizacaoRepository;
import com.digytal.control.repository.modulo.acesso.UsuarioRepository;
import com.digytal.control.repository.modulo.acesso.empresa.AplicacaoRepository;
import com.digytal.control.repository.modulo.acesso.empresa.ContaRepository;
import com.digytal.control.repository.modulo.acesso.empresa.EmpresaRepository;
import com.digytal.control.repository.modulo.acesso.empresa.FormaPagamentoRepository;
import com.digytal.control.service.modulo.acesso.PrimeiroAcessoService;
import com.digytal.control.service.modulo.acesso.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicTest {
	
	@Autowired
	PrimeiroAcessoService primeiroAcessoService;
	
	@Autowired
	UsuarioService  usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
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
	@Order(1)
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
	@Order(2)
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
	@Order(3)
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
}
