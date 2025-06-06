package com.ranieriiuri.eclesial_arrecadacoes.domain.event.model;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Peca;
import org.springframework.context.ApplicationEvent;

public class PecaCriadaEvent extends ApplicationEvent {
    private final Peca peca;

    public PecaCriadaEvent(Object source, Peca peca) {
        super(source);
        this.peca = peca;
    }

    public Peca getPeca() {
        return peca;
    }
}