package br.com.lukemedrano.campoMinado.view;

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
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBackground(new Color(160, 160, 160));
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
		
		addMouseListener(this);
		campo.registraObserverCampo(this);
	}
	
	public Campo getCampo() {
		return this.campo;
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
		if(this.getCampo().isMinado()) {
			setBackground(new Color(189, 66, 68));
			return;
		}
		
		setBackground(new Color(180, 180, 180));
		setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
		
		switch(this.campo.minasNaVizinhaca()) {
			case 1:
				setForeground(new Color(0, 150, 0));
				break;
			case 2:
				setForeground(new Color(0, 0, 255));
				break;
			case 3:
				setForeground(new Color(255, 255, 0));
				break;
			case 4:
			case 5:
			case 6:
				setForeground(new Color(255, 0, 0));
				break;
			default:
				setForeground(new Color(255, 80, 160));
		}
		
		String valor = this.getCampo().vizinhacaPerigosa() ? this.getCampo().minasNaVizinhaca() + "" : "";
		setText(valor);
	}
	
	private void aplicaEstiloMarcar() {
		setBackground(new Color(8, 179, 247));
		setForeground(new Color(0, 0, 0));
		setText("M");
	}

	private void aplicaEstiloExplodir() {
		setBackground(new Color(189, 66, 68));
		setForeground(new Color(255, 255, 255));
		setText("X");
	}
	
	private void aplicaEstiloPadrao() {
		setBackground(new Color(160, 160, 160));
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	@Override
	public void mousePressed(MouseEvent evento) {
		if(evento.getButton() == 1) {
			this.getCampo().abrir();
		}else if(evento.getButton() == 3) {
			this.getCampo().alternaMarcacao();
		}
	}

	public void mouseClicked(MouseEvent evento) {}
	public void mouseReleased(MouseEvent evento) {}
	public void mouseEntered(MouseEvent evento) {}
	public void mouseExited(MouseEvent evento) {}
}
