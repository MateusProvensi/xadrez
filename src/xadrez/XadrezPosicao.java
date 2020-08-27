package xadrez;

import tabuleirojogo.Posicao;

public class XadrezPosicao {

	private char coluna;
	private char linha;
	
	public XadrezPosicao(char coluna, char linha) {
		if (coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new XadrezException("Valor inválido, eles devem serem de a1 até h8");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	public char getColuna() {
		return coluna;
	}

	public char getLinha() {
		return linha;
	}

	protected Posicao paraPosicao() {
		return new Posicao(8 - linha, coluna - 'a');
	}
	
	protected static XadrezPosicao paraPosicao(Posicao posicao) {
		return new XadrezPosicao((char) ('a' - posicao.getColuna()), (char) (8 -  posicao.getLinha()));
	}
	
	
	@Override
	public String toString() {
		return "" + coluna + linha;
	}
	
}
