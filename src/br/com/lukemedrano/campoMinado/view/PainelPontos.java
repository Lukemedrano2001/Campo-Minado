package br.com.lukemedrano.campoMinado.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import br.com.lukemedrano.campoMinado.model.tabuleiro.Tabuleiro;

@SuppressWarnings("serial")
public class PainelPontos extends JPanel {
    @SuppressWarnings("unused")
    private final Tabuleiro tabuleiro;
    private final JTextArea textArea;

    public PainelPontos(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.textArea = new JTextArea();

        configurarJTextArea(textArea);

        tabuleiro.registraObserverPontos(evento -> {
            int vitorias = tabuleiro.getVitorias();
            int derrotas = tabuleiro.getDerrotas();
            int jogos = tabuleiro.getTotalJogos();
            double porcentagemVitorias = tabuleiro.getPorcentagemVitorias();
            double porcentagemDerrotas = tabuleiro.getPorcentagemDerrotas();
            
            atualizarTexto(vitorias, derrotas, porcentagemVitorias, porcentagemDerrotas, jogos);
        });
        
        int vitorias = tabuleiro.getVitorias();
        int derrotas = tabuleiro.getDerrotas();
        int jogos = tabuleiro.getTotalJogos();
        double porcentagemVitorias = tabuleiro.getPorcentagemVitorias();
        double porcentagemDerrotas = tabuleiro.getPorcentagemDerrotas();

        atualizarTexto(vitorias, derrotas, porcentagemVitorias, porcentagemDerrotas, jogos);
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

    private void atualizarTexto(int vitorias, int derrotas, double porcentagemVitorias, double porcentagemDerrotas, int jogos) {
    	StringBuilder builder = new StringBuilder();
        
    	builder.append("Vitórias: " + vitorias)
    		.append(String.format("  Porcentagem: %.2f%%", porcentagemVitorias)).append("\n");
        builder.append("Derrotas: " + derrotas)
        	.append(String.format("  Porcentagem: %.2f%%", porcentagemDerrotas)).append("\n");
        builder.append("Jogos: " + jogos);
        
        textArea.setText(builder.toString());
    }
}
