package xadrez.pecas;

import tabuleirojogo.Posicao;
import tabuleirojogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	private PartidaXadrez partidaXadrez;

	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
		super(tabuleiro, cor);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public boolean[][] movimentosPossiveis() {

		boolean[][] matriz = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao peca = new Posicao(0, 0);

		if (getCor() == Cor.WHITE) {
			peca.setValores(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}

			peca.setValores(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());

			if (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca) && getTabuleiro().posicaoExiste(p2)
					&& !getTabuleiro().eUmaPeca(p2) && getContadorMovimentos() == 0) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}

			peca.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}

			peca.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}

			if (posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(esquerda) && temPecaOponente(esquerda)
						&& getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassantVulneravel()) {
					matriz[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(direita) && temPecaOponente(direita)
						&& getTabuleiro().peca(direita) == partidaXadrez.getEnPassantVulneravel()) {
					matriz[direita.getLinha() - 1][direita.getColuna()] = true;
				}
			}

		} else {
			peca.setValores(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}

			peca.setValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());

			if (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca) && getTabuleiro().posicaoExiste(p2)
					&& !getTabuleiro().eUmaPeca(p2) && getContadorMovimentos() == 0) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}

			peca.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}

			peca.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
				matriz[peca.getLinha()][peca.getColuna()] = true;
			}
			
			if (posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(esquerda) && temPecaOponente(esquerda)
						&& getTabuleiro().peca(esquerda) == partidaXadrez.getEnPassantVulneravel()) {
					matriz[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExiste(direita) && temPecaOponente(direita)
						&& getTabuleiro().peca(direita) == partidaXadrez.getEnPassantVulneravel()) {
					matriz[direita.getLinha() + 1][direita.getColuna()] = true;
				}
			}
		}

		return matriz;
	}

	@Override
	public String toString() {
		return "P";
	}
}
