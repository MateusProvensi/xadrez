package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.XadrezException;
import xadrez.XadrezPosicao;

public class Programa {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> capturadas = new ArrayList<>();

		while (!partidaXadrez.getCheckMate()) {
			try {
				UI.limpaTela();
				UI.visualizarPartida(partidaXadrez, capturadas);
				System.out.println();
				System.out.print("Origem: ");
				XadrezPosicao origem = UI.lerPosicaoXadrez(sc);

				boolean[][] movimentosPossiveis = partidaXadrez.movimentosPossiveis(origem);
				UI.limpaTela();
				UI.visualizarTabuleiro(partidaXadrez.getPecas(), movimentosPossiveis);

				System.out.println();
				System.out.print("Destino: ");
				XadrezPosicao destino = UI.lerPosicaoXadrez(sc);

				PecaXadrez pecaCapturada = partidaXadrez.moverPecaXadrez(origem, destino);

				if (pecaCapturada != null) {
					capturadas.add(pecaCapturada);
				}

				if (partidaXadrez.getPromocao() != null) {
					System.out.println("Digite a pe�a para promo��o (B/C/T/Q)");
					String tipo = sc.next().toUpperCase();

					while (!tipo.equals("B") && !tipo.equals("C") && tipo.equals("T") && tipo.equals("Q")) {
						System.out.println("Valor inv�lido, digite novamente.");
						System.out.print("Digite a pe�a para promo��o (B/C/T/Q): ");
						tipo = sc.next().toUpperCase();
						
					}
					partidaXadrez.recolocarPecaPromovida(tipo);
				}

			} catch (XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}

		UI.limpaTela();
		UI.visualizarPartida(partidaXadrez, capturadas);

	}

}
