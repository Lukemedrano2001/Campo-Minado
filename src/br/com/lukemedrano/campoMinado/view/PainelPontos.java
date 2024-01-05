package br.com.lukemedrano.campoMinado.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import br.com.lukemedrano.campoMinado.model.tabuleiro.Tabuleiro;

@SuppressWarnings("serial")
public class PainelPontos extends JPanel {
    private final Tabuleiro tabuleiro;
    private final JTextArea textArea;

    public PainelPontos(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.textArea = new JTextArea();

        this.configurarJTextArea(textArea);

        // Inicialmente mostra o tabuleiro
        this.exibeTextoInicial();
        
        // Atualiza o tabuleiro a cada partida
        tabuleiro.registraObserverPontos(evento -> this.atualizarTextoComEventos());
    }

    private void configurarJTextArea(JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(textArea);
    }

    private void atualizarTextoComEventos() {
        this.atualizarTexto();
    }

    private void exibeTextoInicial() {
        this.atualizarTexto();
    }
    
    private void atualizarTexto() {
        StringBuilder builder = new StringBuilder();
        
<<<<<<< HEAD
        int vitorias = tabuleiro.getVitorias();
        int derrotas = tabuleiro.getDerrotas();
        int jogos = tabuleiro.getTotalJogos();
        double porcentagemVitorias = tabuleiro.getPorcentagemVitorias();
        double porcentagemDerrotas = tabuleiro.getPorcentagemDerrotas();

        builder.append("Vitórias: " + vitorias)
                .append(String.format("  Porcentagem: %.2f%%", porcentagemVitorias)).append("\n\n");
        builder.append("Derrotas: " + derrotas)
                .append(String.format("  Porcentagem: %.2f%%", porcentagemDerrotas)).append("\n\n");
=======
    	builder.append("Vitórias: " + vitorias)
    		.append(String.format("  Porcentagem: %.2f%%", porcentagemVitorias)).append("\n\n");
        builder.append("Derrotas: " + derrotas)
        	.append(String.format("  Porcentagem: %.2f%%", porcentagemDerrotas)).append("\n\n");
>>>>>>> 2fb087238ed3db3a0fcefe6faedb7c81ea30e3cd
        builder.append("Jogos: " + jogos);

        textArea.setText(builder.toString());
    }
}