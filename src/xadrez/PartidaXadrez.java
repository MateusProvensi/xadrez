package xadrez;

import tabuleirojogo.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		inicioJogo();
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] matriz = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];

		for (int i = 0; i < tabuleiro.getColunas(); i++) {
			for (int j = 0; j < tabuleiro.getLinhas(); j++) {
				matriz[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return matriz;
	}
	
	private void novoLugarPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.lugarPeca(peca, new XadrezPosicao(coluna, (char) linha).paraPosicao());
	}
	
	private void inicioJogo() {
		novoLugarPeca('c', 1, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('c', 2, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('d', 2, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('e', 2, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('e', 1, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('d', 1, new Rei(tabuleiro, Cor.WHITE));

		novoLugarPeca('c', 7, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('c', 8, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('d', 7, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('e', 7, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('e', 8, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('d', 8, new Rei(tabuleiro, Cor.BLACK));
	}

}
