package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleirojogo.Peca;
import tabuleirojogo.Posicao;
import tabuleirojogo.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulneravel;
	private PecaXadrez promocao;

	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.WHITE;
		inicioJogo();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaXadrez getEnPassantVulneravel() {
		return enPassantVulneravel;
	}

	public PecaXadrez getPromocao() {
		return promocao;
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

	public boolean[][] movimentosPossiveis(XadrezPosicao posicaoOrigem) {
		Posicao posicao = posicaoOrigem.paraPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}

	public PecaXadrez moverPecaXadrez(XadrezPosicao posicaoOigem, XadrezPosicao posicaoDestino) {
		Posicao origem = posicaoOigem.paraPosicao();
		Posicao destino = posicaoDestino.paraPosicao();

		validarPosicaoOrigem(origem);
		validarPosicaoDestino(origem, destino);
		Peca capturadaPeca = fazerMovimento(origem, destino);

		if (testCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, capturadaPeca);
			throw new XadrezException("Você não pode se colocoar em check");
		}

		PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(destino);

		promocao = null;
		if (pecaMovida instanceof Peao) {
			if (pecaMovida.getCor() == Cor.WHITE && destino.getLinha() == 0
					|| pecaMovida.getCor() == Cor.BLACK && destino.getLinha() == 7) {
				promocao = (PecaXadrez) tabuleiro.peca(destino);
				promocao = recolocarPecaPromovida("Q");
			}
		}

		check = (testCheck(oponente(jogadorAtual))) ? true : false;

		if (testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}

		if (pecaMovida instanceof Peao
				&& (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			enPassantVulneravel = pecaMovida;
		} else {
			enPassantVulneravel = null;
		}

		return (PecaXadrez) capturadaPeca;

	}

	public PecaXadrez recolocarPecaPromovida(String tipo) {
		if (promocao == null) {
			throw new IllegalStateException("Não há peça para ser removida");
		}
		if (!tipo.equals("B") && !tipo.equals("C") && tipo.equals("T") && tipo.equals("Q")) {
			return promocao;
		}

		Posicao pos = promocao.getXadrezPosicao().paraPosicao();
		Peca p = tabuleiro.removerPeca(pos);
		pecasNoTabuleiro.remove(p);

		PecaXadrez novaPeca = novaPeca(tipo, promocao.getCor());
		tabuleiro.lugarPeca(novaPeca, pos);
		pecasNoTabuleiro.add(novaPeca);

		return novaPeca;

	}

	private PecaXadrez novaPeca(String tipo, Cor cor) {
		if (tipo.equals("B")) {
			return new Bispo(tabuleiro, cor);
		}
		if (tipo.equals("C")) {
			return new Cavalo(tabuleiro, cor);
		}
		if (tipo.equals("T")) {
			return new Torre(tabuleiro, cor);
		} else {
			return new Rainha(tabuleiro, cor);
		}
	}

	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(origem);
		p.adicionarMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.lugarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.lugarPeca(torre, destinoT);
			torre.adicionarMovimento();

		}

		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemT);
			tabuleiro.lugarPeca(torre, destinoT);
			torre.adicionarMovimento();

		}

		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if (p.getCor() == Cor.WHITE) {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removerPeca(posicaoPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}

		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(destino);
		p.decrementarMovimento();
		tabuleiro.lugarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.lugarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}

		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.lugarPeca(torre, origemT);
			torre.decrementarMovimento();

		}

		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);

			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(destinoT);
			tabuleiro.lugarPeca(torre, origemT);
			torre.decrementarMovimento();

		}

		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulneravel) {
				PecaXadrez peao = (PecaXadrez) tabuleiro.removerPeca(destino);
				Posicao posicaoPeao;
				if (p.getCor() == Cor.WHITE) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				} else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}

				tabuleiro.lugarPeca(peao, posicaoPeao);

			}
		}

	}

	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.eUmaPeca(posicao)) {
			throw new XadrezException("Não há peças na posição de origem");
		}

		if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("A peça exolhida não é sua.");
		}

		if (!tabuleiro.peca(posicao).temMovimentosPossiveis()) {
			throw new XadrezException("Não existe movimentos possíveis para a peça escolhida");
		}
	}

	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezException("A peça escolhida não pode se mover para a posição de destino");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}

	private Cor oponente(Cor cor) {
		return (cor == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}

	private PecaXadrez rei(Cor cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca peca : lista) {
			if (peca instanceof Rei) {
				return (PecaXadrez) peca;
			}
		}
		throw new IllegalStateException("Não existe o rei " + cor + "no tabuleiro");
	}

	private boolean testCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getXadrezPosicao().paraPosicao();
		List<Peca> pecasOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());

		for (Peca peca : pecasOponente) {
			boolean[][] matriz = peca.movimentosPossiveis();
			if (matriz[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testeCheckMate(Cor cor) {
		if (!testCheck(cor)) {
			return false;
		}
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca peca : lista) {
			boolean[][] matriz = peca.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (matriz[i][j]) {
						Posicao origem = ((PecaXadrez) peca).getXadrezPosicao().paraPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazerMovimento(origem, destino);
						boolean testeCheck = testCheck(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void novoLugarPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.lugarPeca(peca, new XadrezPosicao(coluna, (char) linha).paraPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void inicioJogo() {
		novoLugarPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('b', 1, new Cavalo(tabuleiro, Cor.WHITE));
		novoLugarPeca('c', 1, new Bispo(tabuleiro, Cor.WHITE));
		novoLugarPeca('d', 1, new Rainha(tabuleiro, Cor.WHITE));
		novoLugarPeca('e', 1, new Rei(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('f', 1, new Bispo(tabuleiro, Cor.WHITE));
		novoLugarPeca('g', 1, new Cavalo(tabuleiro, Cor.WHITE));
		novoLugarPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('a', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('b', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('c', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('d', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('e', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('f', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('g', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novoLugarPeca('h', 2, new Peao(tabuleiro, Cor.WHITE, this));

		novoLugarPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('b', 8, new Cavalo(tabuleiro, Cor.BLACK));
		novoLugarPeca('c', 8, new Bispo(tabuleiro, Cor.BLACK));
		novoLugarPeca('d', 8, new Rainha(tabuleiro, Cor.BLACK));
		novoLugarPeca('e', 8, new Rei(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('f', 8, new Bispo(tabuleiro, Cor.BLACK));
		novoLugarPeca('g', 8, new Cavalo(tabuleiro, Cor.BLACK));
		novoLugarPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('a', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('b', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('c', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('d', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('e', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('f', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('g', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novoLugarPeca('h', 7, new Peao(tabuleiro, Cor.BLACK, this));

	}

}
