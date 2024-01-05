package br.com.lukemedrano.campoMinado.view;

import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import br.com.lukemedrano.campoMinado.model.tabuleiro.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		tabuleiro.forEachCampo(campo -> add(new BotaoCampo(campo)));
		
		tabuleiro.registraObserverTabuleiro(evento -> {
			SwingUtilities.invokeLater(() -> { 
				if(evento.isResultado()) {
					JOptionPane.showMessageDialog(this, "Vitória");
				}else {
					JOptionPane.showMessageDialog(this, "Derrota");
				}
				
				tabuleiro.reiniciaJogo();
			});
		});
	}
}