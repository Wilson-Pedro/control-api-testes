package com.digytal.control.service.modulo.financeiro;
import com.digytal.control.infra.business.RegistroNaoLocalizadoException;
import com.digytal.control.infra.business.SaldoInsuficienteException;
import com.digytal.control.infra.commons.validation.Entities;
import com.digytal.control.model.comum.MeioPagamento;
import com.digytal.control.model.modulo.acesso.empresa.aplicacao.AplicacaoTipo;
import com.digytal.control.model.modulo.acesso.empresa.conta.ContaEntity;
import com.digytal.control.model.modulo.acesso.empresa.pagamento.FormaPagamentoEntity;
import com.digytal.control.model.modulo.financeiro.Valor;
import com.digytal.control.model.modulo.financeiro.pagamento.PagamentoEntity;
import com.digytal.control.model.modulo.financeiro.transacao.TransacaoRateioRequest;
import com.digytal.control.model.modulo.financeiro.transacao.TransacaoRequest;
import com.digytal.control.repository.modulo.acesso.empresa.ContaRepository;
import com.digytal.control.repository.modulo.acesso.empresa.FormaPagamentoRepository;
import com.digytal.control.service.comum.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.digytal.control.infra.commons.validation.Attributes.ID;

@Service
@Slf4j
public class PagamentoService extends AbstractService {
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    @Autowired
    private ContaRepository contaRepository;
    public PagamentoEntity criarPagamento(AplicacaoTipo tipo, TransacaoRateioRequest rateio){
        Double valor = rateio.getValorPago();
        MeioPagamento meioPagamento = rateio.getMeioPagamento();
        FormaPagamentoEntity formaPagamento = formaPagamentoRepository.findByEmpresaAndMeioPagamentoAndNumeroParcelas(requestInfo.getEmpresa(),meioPagamento,1);
        if(formaPagamento==null)
            throw new RegistroNaoLocalizadoException(Entities.EMPRESA_CONTA_ENTITY, ID);

        PagamentoEntity entity = new PagamentoEntity();
        entity.setMeioPagamento(meioPagamento);
        atualizarSaldoConta(tipo, meioPagamento, valor, entity);

        return  entity;
    }
    private void atualizarSaldoConta(AplicacaoTipo tipo, MeioPagamento meioPagamento, Double valor, PagamentoEntity entity){
        FormaPagamentoEntity formaPagamento = formaPagamentoRepository.findByEmpresaAndMeioPagamentoAndNumeroParcelas(requestInfo.getEmpresa(),meioPagamento,1);
        if(formaPagamento==null)
            throw new RegistroNaoLocalizadoException(Entities.EMPRESA_CONTA_ENTITY, ID);

        ContaEntity conta = contaRepository.findById(formaPagamento.getConta()).orElseThrow(()-> new RegistroNaoLocalizadoException(Entities.EMPRESA_CONTA_ENTITY, ID));
        if(tipo == AplicacaoTipo.DESPESA &&  valor > conta.getSaldo())
            throw new SaldoInsuficienteException();

        entity.setValor(Valor.of(tipo, valor));
        entity.setConta(conta.getId());
        conta.setSaldo(conta.getSaldo() + entity.getValor().getValorOperacional());

        contaRepository.save(conta);
    }

}
