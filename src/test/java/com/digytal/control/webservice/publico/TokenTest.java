package com.digytal.control.webservice.publico;

import static com.digytal.control.webservice.LoginUniversal.CPF_CNPJ;
import static com.digytal.control.webservice.LoginUniversal.LOGIN;
import static com.digytal.control.webservice.LoginUniversal.SENHA;
import static com.digytal.control.webservice.LoginUniversal.TOKEN;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
class TokenTest {
	
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
		usuarioRepository.deleteAll();
		formaPagamentoRepository.deleteAll();
		contaRepository.deleteAll();
		empresaRepository.deleteAll();
		aplicacaoRepository.deleteAll();
		organizacaoRepository.deleteAll();
		
		request.setNomeFantasia("ROUPAS BR");
		request.setSobrenomeSocial("ROUPAS BRASIL");
		request.setEmail("brasil.roupas@hotmail.com.br");
		
		configurarAcesso = this.primeiroAcessoService.configurarPrimeiroAcesso(CPF_CNPJ, request);
	}
	
	@Test
	void deveSelecionarEmpresaComSucesso() throws Exception {
		
		CredenciamentoResponse response = this.usuarioService.solicitarNovaSenha(LOGIN);
		
		Integer empresa = response.getUsuario() + 1;
		
		String newTOken = this.empresaService.selecionarEmpresa(empresa, TOKEN);
		
		mockMvc.perform(get("/public/empresas/selecao/{empresa}", empresa)
				.header("authorization", "Bearer " + TOKEN))
				.andExpect(status().isOk())
				.andReturn();
		
		assertNotEquals(null, newTOken);
	}

}
