package br.com.lukemedrano.campoMinado.model;

public class EventoResultadoTabuleiro {
    private final boolean resultado;
    
    public EventoResultadoTabuleiro(boolean resultado) {
        this.resultado = resultado;
    }
    
    public boolean isResultado() {
        return this.resultado;
    }
}