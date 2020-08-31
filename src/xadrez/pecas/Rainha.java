package xadrez.pecas;

import tabuleirojogo.Posicao;
import tabuleirojogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rainha extends PecaXadrez {

	public Rainha(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "Q";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] matriz = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao peca = new Posicao(0, 0);
		
		peca.setValores(posicao.getLinha() - 1, posicao.getColuna());
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setLinha(peca.getLinha() - 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}
		
		
		peca.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setColuna(peca.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}

		
		peca.setValores(posicao.getLinha(), posicao.getColuna() + 1);
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setColuna(peca.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}
		
		peca.setValores(posicao.getLinha() + 1, posicao.getColuna());
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setLinha(peca.getLinha() + 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}
		
		
		
		peca.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setValores(peca.getLinha() - 1, peca.getColuna()- 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}
		
		
		peca.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setValores(peca.getLinha() - 1, peca.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}

		
		peca.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setValores(peca.getLinha() + 1,peca.getColuna() + 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}
		
		peca.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
		
		while (getTabuleiro().posicaoExiste(peca) && !getTabuleiro().eUmaPeca(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
			peca.setValores(peca.getLinha() + 1, peca.getColuna() - 1);
		}
		if (getTabuleiro().posicaoExiste(peca) && temPecaOponente(peca)) {
			matriz[peca.getLinha()][peca.getColuna()] = true;
		}
		
		
		
		return matriz;
		
		
		
	}

}
