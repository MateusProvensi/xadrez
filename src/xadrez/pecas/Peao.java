package xadrez.pecas;

import tabuleirojogo.Posicao;
import tabuleirojogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	public Peao(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
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
		}

		return matriz;
	}

	@Override
	public String toString() {
		return "P";
	}
}
