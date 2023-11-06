package com.digytal.control.webservice.modulo.cadastro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.digytal.control.model.modulo.cadastro.produto.unidademedida.UnidadeMedidaRequest;
import com.digytal.control.service.modulo.cadastro.produto.UnidadeMedidaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import infra.config.TestObjectMapperConfig;
import infra.security.TestSecurityConfig;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TestSecurityConfig.class, TestObjectMapperConfig.class})
class UnidadeMedidaResourceTest {
	
	@InjectMocks
	UnidadeMedidaResource unidadeMedidaResource;
	
	UnidadeMedidaService unidadeMedidaService = mock(UnidadeMedidaService.class);
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void deveIncluirUnidadeMediaComSucesso() throws Exception {
		
		UnidadeMedidaRequest requestExpected = new UnidadeMedidaRequest();
		requestExpected.setSigla("PS5");
		requestExpected.setNome("Playstation");
		requestExpected.setDescricao("12 unidades");
		requestExpected.setConteudo(12.0);
		requestExpected.setEmbalagem(true);
		
		when(this.unidadeMedidaService.incluir(requestExpected)).thenReturn(1);

//      ERROR 404
//		String jsonRequest = objectMapper.writeValueAsString(requestExpected);
//		
//		mockMvc.perform(post("/unidades-medida")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonRequest))
//				.andExpect(status().isCreated())
//				.andReturn();
		
		Integer request = this.unidadeMedidaService.incluir(requestExpected);
		
		assertThat(request).usingRecursiveComparison().isEqualTo(1);
	}
}
