package br.com.lukemedrano.campoMinado.model.tabuleiro;

import br.com.lukemedrano.campoMinado.model.EventoPontos;

public interface ObserverPontos {
	void atualiza(EventoPontos evento);
}