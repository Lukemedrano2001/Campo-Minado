package br.com.lukemedrano.campoMinado.model;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.lukemedrano.campoMinado.model.EventoCampo;
import br.com.lukemedrano.campoMinado.model.campo.Campo;
import br.com.lukemedrano.campoMinado.model.campo.ObserverCampo;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements ObserverCampo, MouseListener {
	private final Color BG_PADRAO_CINZA = new Color(160, 160, 160);
	private final Color BG_ABERTO = new Color(180, 180, 180);
	private final Color BG_MARCADO = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	
	private final Color BORDA = new Color(60, 60, 60);
	private final Color TEXTO_VERDE = new Color(0, 150, 0);
	private final Color TEXTO_AZUL = new Color(0, 0, 255);
	private final Color TEXTO_AMARELO = new Color(255, 255, 0);
	private final Color TEXTO_VERMELHO = new Color(255, 0, 0);
	private final Color TEXTO_ROSA = new Color(255, 80, 160);
	
	
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBackground(BG_PADRAO_CINZA);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
		
		addMouseListener(this);
		campo.registraObserverCampo(this);
	}

	@Override
	public void eventoOcorreu(Campo campo, EventoCampo evento) {
		switch(evento) {
		case ABRIR:
			this.aplicaEstiloAbrir();
			break;
		case MARCAR:
			this.aplicaEstiloMarcar();
			break;
		case EXPLODIR:
			this.aplicaEstiloExplodir();
			break;
		default:
			this.aplicaEstiloPadrao();
		}
		
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void aplicaEstiloAbrir() {
		if(this.campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		
		setBackground(BG_ABERTO);
		setBorder(BorderFactory.createLineBorder(BORDA));
		
		switch(this.campo.minasNaVizinhaca()) {
			case 1:
				setForeground(TEXTO_VERDE);
				break;
			case 2:
				setForeground(TEXTO_AZUL);
				break;
			case 3:
				setForeground(TEXTO_AMARELO);
				break;
			case 4:
			case 5:
			case 6:
				setForeground(TEXTO_VERMELHO);
				break;
			default:
				setForeground(TEXTO_ROSA);
		}
		
		String valor = !this.campo.vizinhacaSegura() ? this.campo.minasNaVizinhaca() + "" : "";
		setText(valor);
	}
	
	private void aplicaEstiloMarcar() {
		setBackground(BG_MARCADO);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicaEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");
	}
	
	private void aplicaEstiloPadrao() {
		setBackground(BG_PADRAO_CINZA);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	@Override
	public void mousePressed(MouseEvent evento) {
		if(evento.getButton() == 1) {
			campo.abrir();
		}else if(evento.getButton() == 3) {
			campo.alternaMarcacao();
		}
	}

	public void mouseClicked(MouseEvent evento) {}
	public void mouseReleased(MouseEvent evento) {}
	public void mouseEntered(MouseEvent evento) {}
	public void mouseExited(MouseEvent evento) {}
}