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
	
	private List<Campo> vizinhos = new ArrayList<>();
	// Poderia usar o BiConsumer, e este recebe 2 parametros e retorna void ao invés de usar o ObserverCampo
	// Poderia usar o Set, pois não tem ordem e evita duplicação de observer
	private List<ObserverCampo> observersCampo = new ArrayList<>();	
	
	public Campo(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
	}
	
	// Logo abaixo, os métodos com visibilidade public
	
	public int getLinha() {
		return this.linha;
	}
	
	public int getColuna() {
		return this.coluna;
	}
	
	public boolean isAberto() {
		return this.aberto;
	}
	
	public boolean isFechado() {
		return !this.isAberto();
	}
	
	public void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(this.isAberto()) {
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
	
	public List<Campo> getVizinhos(){
		return this.vizinhos;
	}
	
	public List<ObserverCampo> getObservers(){
		return this.observersCampo;
	}
	
	public boolean abrir() {
		if(this.isFechado() && this.isNaoMarcado()) {
			this.aberto = true;
			
			if(this.isMinado()) {
				this.notificaObserver(EventoCampo.EXPLODIR);
				return true;
			}
			
			this.setAberto(true);
			
			if(this.vizinhacaSegura()) {
				this.getVizinhos().forEach(vizinho -> vizinho.abrir());	// Abrindo os campos seguros de maneira recursiva
			}
			
			return true;
		}else {			
			return false;			
		}
	}
	
	public boolean adicionaVizinho(Campo vizinho) {
		boolean linhaDiferente = this.getLinha() != vizinho.getLinha();
		boolean colunaDiferente = this.getColuna() != vizinho.getColuna();
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(this.getLinha() - vizinho.getLinha());
		int deltaColuna = Math.abs(this.getColuna() - vizinho.getColuna());
		int deltaGeral = deltaLinha + deltaColuna;
		
		if(deltaGeral == 1 && !diagonal) {	// Vizinho na Horizontal ou Vertical
			this.getVizinhos().add(vizinho);
			return true;
		}else if(deltaGeral == 2 && diagonal) {	// Vizinho na Diagonal
			this.getVizinhos().add(vizinho);
			return true;
		}else {
			return false;
		}
	}
	
	public void alternaMarcacao() {
		if(this.isFechado()) {
			this.marcado = !this.marcado;
			
			if(this.isMarcado()) {
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
		if(this.isNaoMinado()) {
			this.minado = true;	
			return true;
		}else {
			return false;
		}
	}
	
	public int minasNaVizinhaca() {
		return (int) (this.getVizinhos().stream().filter(vizinho -> vizinho.isMinado()).count());
	}
	
	public boolean vizinhacaSegura() {
		return this.getVizinhos().stream().noneMatch(vizinho -> vizinho.isMinado());
	}
	
	public boolean vizinhacaPerigosa() {
		return !this.vizinhacaSegura();
	}
	
	public boolean objetivoAlcancado() {
		boolean desvendado = this.isNaoMinado() && this.isAberto();
		boolean protegido = this.isMinado() && this.isMarcado();
		
		return desvendado || protegido;
	}
	
	public void registraObserverCampo(ObserverCampo observer) {
		this.getObservers().add(observer);
	}
	
	// Logo abaixo, os métodos com visibilidade private
	
	private void notificaObserver(EventoCampo evento) {
		this.getObservers().stream().forEach(observer -> observer.eventoOcorreu(this, evento));
	}
}
