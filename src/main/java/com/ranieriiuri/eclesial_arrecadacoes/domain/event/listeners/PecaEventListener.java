package com.ranieriiuri.eclesial_arrecadacoes.domain.event.listeners;

import com.ranieriiuri.eclesial_arrecadacoes.domain.event.model.PecaCriadaEvent;
import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PecaEventListener {

    @EventListener
    public void handlePecaCriada(PecaCriadaEvent event) {
        Peca peca = event.getPeca();
        System.out.println("Peça criada: " + peca.getNome());
    }
}