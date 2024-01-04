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
	
	public void forEachCampo(Consumer<Campo> funcao) {
		this.campos.forEach(funcao);
	}
	
	public void abrir(int linha, int coluna) {
		this.campos.parallelStream()
			.filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
			.findFirst()
			.ifPresent(campo -> campo.abrir());
	}
	
	public void alternaMarcacao(int linha, int coluna) {
		this.campos.parallelStream()
			.filter(campo -> campo.getLinha() == linha && campo.getColuna() == coluna)
			.findFirst()
			.ifPresent(campo -> campo.alternaMarcacao());
	}
	
	public boolean objetivoAlcancado() {
		return this.campos.stream().allMatch(campo -> campo.objetivoAlcancado());
	}
	
	public boolean objetivoNaoAlcancado() {
		return !this.objetivoAlcancado();
	}
	
	public void reiniciaJogo() {
		this.campos.stream().forEach(campo -> campo.reiniciar());
		this.sorteioMinas();
	}
	
	public void registraObserverTabuleiro(Consumer<EventoResultadoTabuleiro> observer) {
		this.observersTabuleiro.add(observer);
	}
	
	public void registraObserverPontos(Consumer<EventoResultadoPontos> observer) {
        this.observersPontos.add(observer);
    }
	
	public double getPorcentagemVitorias() {
        if (this.totalJogos == 0) {
            return 0.0;
        }

        return ((double) this.vitorias / this.totalJogos) * 100.0;
    }

    public double getPorcentagemDerrotas() {
        if (this.totalJogos == 0) {
            return 0.0;
        }

        return ((double) this.derrotas / this.totalJogos) * 100.0;
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

	
	// Logo abaixo, os métodos com visibilidade de pacote ou default
	
	void geraCampos() {
		for(int linha = 0; linha < this.linhas; linha++) {
			for(int coluna = 0; coluna < this.colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registraObserverCampo(this);
				campos.add(campo);
			}
		}
	}
	
	void associaVizinho() {
		for(Campo campo1: this.campos) {
			for(Campo campo2: this.campos) {
				campo1.adicionaVizinho(campo2);
			}
		}
	}
	
	void sorteioMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = campo -> campo.isMinado();
		
		while(minasArmadas < this.minas) {
			int aleatorio = (int) (Math.random() * campos.size());
			this.campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		}
	}
	
	void setVitorias() {
		this.vitorias++;
		this.totalJogos++;
		this.notificaObserversPontos(true);
	}
	
	void setDerrotas() {
		this.derrotas++;
		this.totalJogos++;
		this.notificaObserversPontos(false);
	}
	
	// Logo abaixo, os métodos com visibilidade private 
	
	private void notificaObserversTabuleiro(boolean resultado) {
		this.observersTabuleiro.stream().forEach(observer -> observer.accept(new EventoResultadoTabuleiro(resultado)));
	}
	
	private void notificaObserversPontos(boolean resultado) {
	    this.observersPontos.stream().forEach(observer -> observer.accept(new EventoResultadoPontos(resultado)));
	}
	
	private void mostrarMinas() {
		this.campos.stream()
			.filter(campo -> campo.isMinado())
			.filter(campo -> campo.isNaoMarcado())
			.forEach(campo -> campo.setAberto(true));
	}
}