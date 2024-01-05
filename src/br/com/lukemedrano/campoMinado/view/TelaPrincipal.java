package br.com.lukemedrano.campoMinado.view;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import br.com.lukemedrano.campoMinado.model.tabuleiro.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {
    private final Tabuleiro tabuleiro;

    public TelaPrincipal() {
        this.tabuleiro = new Tabuleiro(25, 25, 90);
        PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro);
        PainelPontos painelPontos = new PainelPontos(tabuleiro);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelTabuleiro, painelPontos);
        splitPane.setDividerLocation(925);

        add(splitPane);

        setTitle("Campo Minado");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TelaPrincipal();
    }
}