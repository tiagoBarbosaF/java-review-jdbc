package br.com.mybank;

import br.com.mybank.domain.RegraDeNegocioException;
import br.com.mybank.domain.cliente.DadosCadastroCliente;
import br.com.mybank.domain.conta.Conta;
import br.com.mybank.domain.conta.ContaService;
import br.com.mybank.domain.conta.DadosAberturaConta;

import java.util.Scanner;

public class MyBankApplication {

    private static final ContaService service = new ContaService();
    private static final Scanner teclado = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) {
        var opcao = exibirMenu();
        while (opcao != 0) {
            try {
                switch (opcao) {
                    case 1:
                        listarContas();
                        break;
                    case 2:
                        listarContaPorNumero();
                        break;
                    case 3:
                        abrirConta();
                        break;
                    case 4:
                        encerrarConta();
                        break;
                    case 5:
                        consultarSaldo();
                        break;
                    case 6:
                        realizarSaque();
                        break;
                    case 7:
                        realizarDeposito();
                        break;
                    case 8:
                        realizarTransferencia();
                        break;
                }
            } catch (RegraDeNegocioException e) {
                System.out.println("Erro: " + e.getMessage());
                System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu");
                teclado.next();
            }
            opcao = exibirMenu();
        }

        System.out.println("Finalizando a aplicação.");
    }


    private static int exibirMenu() {
        System.out.println("""
                BYTEBANK - ESCOLHA UMA OPÇÃO:
                1 - Listar contas abertas
                2 - Listar conta por número
                3 - Abertura de conta
                4 - Encerramento de conta
                5 - Consultar saldo de uma conta
                6 - Realizar saque em uma conta
                7 - Realizar depósito em uma conta
                8 - Realizar transferência
                0 - Sair
                """);
        return teclado.nextInt();
    }

    private static void listarContas() {
        System.out.println("Contas cadastradas:");
        var contas = service.listarContasAbertas();
        contas.forEach(System.out::println);

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }

    private static void listarContaPorNumero() {
        System.out.print("Digite o número do CPF que deseja consultar: ");
        Integer number = teclado.nextInt();
        Conta conta = service.accountPerNumber(number);

        System.out.println(conta);

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }

    private static void abrirConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o nome do cliente:");
        var nome = teclado.next();

        System.out.println("Digite o cpf do cliente:");
        var cpf = teclado.next();

        System.out.println("Digite o email do cliente:");
        var email = teclado.next();

        service.abrir(new DadosAberturaConta(numeroDaConta, new DadosCadastroCliente(nome, cpf, email)));

        System.out.println("Conta aberta com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }

    private static void encerrarConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        service.encerrarLogico(numeroDaConta);

        System.out.println("Conta encerrada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }

    private static void consultarSaldo() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();
        var saldo = service.consultarSaldo(numeroDaConta);
        System.out.println("Saldo da conta: " + saldo);

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }

    private static void realizarSaque() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do saque:");
        var valor = teclado.nextBigDecimal();

        service.realizarSaque(numeroDaConta, valor);
        System.out.println("Saque realizado com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }

    private static void realizarDeposito() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do depósito:");
        var valor = teclado.nextBigDecimal();

        service.realizarDeposito(numeroDaConta, valor);

        System.out.println("Depósito realizado com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }

    private static void realizarTransferencia() {
        System.out.println("Digite o número da conta de origem:");
        var numeroContaOrigem = teclado.nextInt();

        System.out.println("Digite o número da conta de destino:");
        var numeroContaDestino = teclado.nextInt();

        System.out.println("Digite valor a ser transferido:");
        var valor = teclado.nextBigDecimal();

        service.realizarTransferencia(numeroContaOrigem, numeroContaDestino, valor);

        System.out.println("Transferência realizada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        teclado.next();
    }
}
