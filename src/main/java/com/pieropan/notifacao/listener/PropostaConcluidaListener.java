package com.pieropan.notifacao.listener;

import com.pieropan.notifacao.constante.MensagemConstante;
import com.pieropan.notifacao.domain.Proposta;
import com.pieropan.notifacao.service.NotificacaoSnsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PropostaConcluidaListener {

    @Autowired
    private NotificacaoSnsService notificacaoSnsService;

    @RabbitListener(queues = "${rabbitmq.queue.proposta.concluida}")
    public void propostaPendente(Proposta proposta) {
        String nome = proposta.getUsuario().getNome();

        String mensagem = proposta.getAprovada()
                ? String.format(MensagemConstante.PROPOSTA_APROVADA, nome)
                : (Objects.nonNull(proposta.getObservacao())
                ? String.format(MensagemConstante.PROPOSTA_NEGADA_POR_STRATEGY, nome, proposta.getObservacao())
                : String.format(MensagemConstante.PROPOSTA_NEGADA, nome));

        notificacaoSnsService.notificar(proposta.getUsuario().getTelefone(), mensagem);
    }
}