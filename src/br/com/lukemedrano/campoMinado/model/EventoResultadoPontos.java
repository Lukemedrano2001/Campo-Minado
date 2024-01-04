package br.com.lukemedrano.campoMinado.model;

public class EventoResultadoPontos {
	private final boolean resultado;
	
	public EventoResultadoPontos(boolean resultado){
		this.resultado = resultado;
	}
	
	public boolean vitoriaOuDerrota() {
		return this.resultado;
	}
}