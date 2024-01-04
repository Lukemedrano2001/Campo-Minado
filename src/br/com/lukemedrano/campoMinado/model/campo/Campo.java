package br.com.lukemedrano.campoMinado.model.campo;

import java.util.ArrayList;
import java.util.List;
import br.com.lukemedrano.campoMinado.model.EventoCampo;

public class Campo {
	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<Campo>();
	// Poderia usar o BiConsumer, e este recebe 2 parametros e retorna void ao invés de usar o ObserverCampo
	// Poderia usar o Set, pois não tem ordem e evita duplicação de observer
	private List<ObserverCampo> observers = new ArrayList<ObserverCampo>();	
	
	public Campo(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
	}
	
	// Logo abaixo, os métodos com visibilidade public
	
	public boolean isAberto() {
		return this.aberto;
	}
	
	public boolean isFechado() {
		return !this.isAberto();
	}
	
	public void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(this.aberto) {
			this.notificaObserver(EventoCampo.ABRIR);
		}
	}
	
	public boolean isMinado() {
		return this.minado;
	}
	
	public boolean isNaoMinado() {
		return !this.isMinado();
	}
	
	public boolean isMarcado() {
		return this.marcado;
	}
	
	public boolean isNaoMarcado() {
		return !this.isMarcado();
	}
	
	public int getLinha() {
		return this.linha;
	}
	
	public int getColuna() {
		return this.coluna;
	}
	
	public List<Campo> getVizinhos(){
		return this.vizinhos;
	}
	
	public boolean abrir() {
		if(!this.aberto && !this.marcado) {
			this.aberto = true;
			
			if(this.minado) {
				this.notificaObserver(EventoCampo.EXPLODIR);
				return true;
			}
			
			this.setAberto(true);
			
			if(this.vizinhacaSegura()) {
				this.vizinhos.forEach(vizinho -> vizinho.abrir());
			}
			
			return true;
		}else {			
			return false;			
		}
	}
	
	public boolean adicionaVizinho(Campo vizinho) {
		boolean linhaDiferente = this.linha != vizinho.linha;
		boolean colunaDiferente = this.coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(this.linha - vizinho.linha);
		int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
		if(deltaGeral == 1 && !diagonal) {	// Vizinho na Horizontal ou Vertical
			vizinhos.add(vizinho);
			return true;
		}else if(deltaGeral == 2 && diagonal) {	// Vizinho na Diagonal
			vizinhos.add(vizinho);
			return true;
		}else {
			return false;
		}
	}
	
	public void alternaMarcacao() {
		if(!this.aberto) {
			this.marcado = !this.marcado;
			
			if(this.marcado) {
				this.notificaObserver(EventoCampo.MARCAR);
			}else {
				this.notificaObserver(EventoCampo.DESMARCAR);
			}
		}
	}
	
	public void reiniciar() {
		this.aberto = false;
		this.minado = false;
		this.marcado = false;
		
		this.notificaObserver(EventoCampo.REINICIAR);
	}
	
	public boolean minar() {
		if(!this.minado) {
			this.minado = true;	
			return true;
		}else {
			return false;
		}
	}
	
	public int minasNaVizinhaca() {
		return (int) (this.vizinhos.stream().filter(vizinho -> vizinho.minado).count());
	}
	
	public boolean vizinhacaSegura() {
		return this.vizinhos.stream().noneMatch(vizinho -> vizinho.minado);
	}
	
	public boolean objetivoAlcancado() {
		boolean desvendado = !this.minado && this.aberto;
		boolean protegido = this.minado && this.marcado;
		
		return desvendado || protegido;
	}
	
	public void registraObserverCampo(ObserverCampo observer) {
		this.observers.add(observer);
	}
	
	// Logo abaixo, os métodos com visibilidade private
	
	private void notificaObserver(EventoCampo evento) {
		this.observers.stream().forEach(observer -> observer.eventoOcorreu(this, evento));
	}
}