package br.com.lukemedrano.campoMinado.model.tabuleiro;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import br.com.lukemedrano.campoMinado.model.EventoCampo;
import br.com.lukemedrano.campoMinado.model.EventoPontos;
import br.com.lukemedrano.campoMinado.model.EventoResultadoPontos;
import br.com.lukemedrano.campoMinado.model.EventoResultadoTabuleiro;
import br.com.lukemedrano.campoMinado.model.campo.Campo;
import br.com.lukemedrano.campoMinado.model.campo.ObserverCampo;

public class Tabuleiro implements ObserverCampo, ObserverPontos {
	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private int vitorias;
	private int derrotas;
	private int totalJogos;
	
	private final List<Campo> campos = new ArrayList<Campo>();
	private final List<Consumer<EventoResultadoTabuleiro>> observersTabuleiro = new ArrayList<>();
	private final List<Consumer<EventoResultadoPontos>> observersPontos = new ArrayList<>();
	
	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		this.totalJogos = 0;
		
		this.geraCampos();
		this.associaVizinho();
		this.sorteioMinas();
	}
	
	
	// Logo abaixo, os métodos com visibilidade public
	
	public int getLinhas() {
		return this.linhas;
	}
	
	public int getColunas() {
		return this.colunas;
	}
	
	public int getMinas() {
		return this.minas;
	}
	
	public int getVitorias() {
		return this.vitorias;
	}
	
	public int getDerrotas() {
		return this.derrotas;
	}
	
	public int getTotalJogos() {
		return this.totalJogos;
	}
	
	public List<Campo> getCampos(){
		return this.campos;
	}
	
	public List<Consumer<EventoResultadoTabuleiro>> getObserversTabuleiro(){
		return this.observersTabuleiro;
	}
	
	public List<Consumer<EventoResultadoPontos>> getObserversPontos(){
		return this.observersPontos;
	}
	
	public double getPorcentagemVitorias() {
        if (this.getTotalJogos() == 0) {
            return 0.0;
        }

        return ((double) this.getVitorias() / this.getTotalJogos()) * 100.0;
    }

    public double getPorcentagemDerrotas() {
        if (this.getTotalJogos() == 0) {
            return 0.0;
        }

        return ((double) this.getDerrotas() / this.getTotalJogos()) * 100.0;
    }
	
	public void forEachCampo(Consumer<Campo> funcao) {
		this.getCampos().forEach(funcao);
	}
	
	public void abrir(int linha, int coluna) {
		this.getCampos().parallelStream()
			.filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
			.findFirst()
			.ifPresent(campo -> campo.abrir());
	}
	
	public void alternaMarcacao(int linha, int coluna) {
		this.getCampos().parallelStream()
			.filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
			.findFirst()
			.ifPresent(campo -> campo.alternaMarcacao());
	}
	
	public boolean objetivoAlcancado() {
		return this.getCampos().stream().allMatch(campo -> campo.objetivoAlcancado());
	}
	
	public boolean objetivoNaoAlcancado() {
		return !this.objetivoAlcancado();
	}
	
	public void reiniciaJogo() {
		this.getCampos().stream().forEach(campo -> campo.reiniciar());
		this.sorteioMinas();
	}
	
	public void registraObserverTabuleiro(Consumer<EventoResultadoTabuleiro> observer) {
		this.getObserversTabuleiro().add(observer);
	}
	
	public void registraObserverPontos(Consumer<EventoResultadoPontos> observer) {
        this.getObserversPontos().add(observer);
    }
	
	@Override
	public void atualiza(EventoPontos evento) {
		switch(evento) {
			case VITORIA:
				this.setVitorias();
				this.notificaObserversPontos(true);
				break;
			case DERROTA:
				this.setDerrotas();
				this.notificaObserversPontos(false);
				break;
		}
	}
	
	@Override
	public void eventoOcorreu(Campo campo, EventoCampo evento) {
		if(evento == EventoCampo.EXPLODIR) {
			this.mostrarMinas();
			this.setDerrotas();
			this.notificaObserversTabuleiro(false);
		}else if(this.objetivoAlcancado()) {
			this.setVitorias();
			this.notificaObserversTabuleiro(true);
		}
	}


	// Logo abaixo, os métodos com visibilidade private 
	
	private void geraCampos() {
		for(int linha = 0; linha < this.getLinhas(); linha++) {
			for(int coluna = 0; coluna < this.getColunas(); coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registraObserverCampo(this);
				this.getCampos().add(campo);
			}
		}
	}
	
	private void associaVizinho() {
		for(Campo campo1: this.getCampos()) {
			for(Campo campo2: this.getCampos()) {
				campo1.adicionaVizinho(campo2);
			}
		}
	}
	
	private void sorteioMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = campo -> campo.isMinado();
		
		while(minasArmadas < this.getMinas()) {
			int aleatorio = (int) (Math.random() * this.getCampos().size());
			this.getCampos().get(aleatorio).minar();
			minasArmadas = this.getCampos().stream().filter(minado).count();
		}
	}
	
	private void setVitorias() {
		this.vitorias++;
		this.setTotalJogos();
		this.notificaObserversPontos(true);
	}
	
	private void setDerrotas() {
		this.derrotas++;
		this.setTotalJogos();
		this.notificaObserversPontos(false);
	}
	
	private void setTotalJogos() {
		this.totalJogos++;
	}
	
	private void notificaObserversPontos(boolean resultado) {
	    this.getObserversPontos().stream().forEach(observer -> observer.accept(new EventoResultadoPontos(resultado)));
	}
	
	private void notificaObserversTabuleiro(boolean resultado) {
		this.getObserversTabuleiro().stream().forEach(observer -> observer.accept(new EventoResultadoTabuleiro(resultado)));
	}
	
	private void mostrarMinas() {
		this.getCampos().stream()
			.filter(campo -> campo.isMinado())
			.filter(campo -> campo.isNaoMarcado())
			.forEach(campo -> campo.setAberto(true));
	}
}