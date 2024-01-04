package br.com.lukemedrano.campoMinado.model.campo;

import br.com.lukemedrano.campoMinado.model.EventoCampo;

public interface ObserverCampo {
	public void eventoOcorreu(Campo campo, EventoCampo evento);
}