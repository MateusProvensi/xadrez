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

		check = (testCheck(oponente(jogadorAtual))) ? true : false;

		if (testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			proximoTurno();
		}

		return (PecaXadrez) capturadaPeca;

	}

	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(origem);
		p.adicionarMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.lugarPeca(p, destino);

		if (pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(destino);
		p.decrementarMovimento();
		tabuleiro.lugarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.lugarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);

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
		novoLugarPeca('e', 1, new Rei(tabuleiro, Cor.WHITE));
		novoLugarPeca('f', 1, new Bispo(tabuleiro, Cor.WHITE));
		novoLugarPeca('g', 1, new Cavalo(tabuleiro, Cor.WHITE));
		novoLugarPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
		novoLugarPeca('a', 2, new Peao(tabuleiro, Cor.WHITE));
		novoLugarPeca('b', 2, new Peao(tabuleiro, Cor.WHITE));
		novoLugarPeca('c', 2, new Peao(tabuleiro, Cor.WHITE));
		novoLugarPeca('d', 2, new Peao(tabuleiro, Cor.WHITE));
		novoLugarPeca('e', 2, new Peao(tabuleiro, Cor.WHITE));
		novoLugarPeca('f', 2, new Peao(tabuleiro, Cor.WHITE));
		novoLugarPeca('g', 2, new Peao(tabuleiro, Cor.WHITE));
		novoLugarPeca('h', 2, new Peao(tabuleiro, Cor.WHITE));
		

		novoLugarPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('b', 8, new Cavalo(tabuleiro, Cor.BLACK));
		novoLugarPeca('c', 8, new Bispo(tabuleiro, Cor.BLACK));
		novoLugarPeca('d', 8, new Rainha(tabuleiro, Cor.BLACK));
		novoLugarPeca('e', 8, new Rei(tabuleiro, Cor.BLACK));
		novoLugarPeca('f', 8, new Bispo(tabuleiro, Cor.BLACK));
		novoLugarPeca('g', 8, new Cavalo(tabuleiro, Cor.BLACK));
		novoLugarPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
		novoLugarPeca('a', 7, new Peao(tabuleiro, Cor.BLACK));
		novoLugarPeca('b', 7, new Peao(tabuleiro, Cor.BLACK));
		novoLugarPeca('c', 7, new Peao(tabuleiro, Cor.BLACK));
		novoLugarPeca('d', 7, new Peao(tabuleiro, Cor.BLACK));
		novoLugarPeca('e', 7, new Peao(tabuleiro, Cor.BLACK));
		novoLugarPeca('f', 7, new Peao(tabuleiro, Cor.BLACK));
		novoLugarPeca('g', 7, new Peao(tabuleiro, Cor.BLACK));
		novoLugarPeca('h', 7, new Peao(tabuleiro, Cor.BLACK));
		
	}

}
