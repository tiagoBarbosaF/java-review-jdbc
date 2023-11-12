package br.com.mybank.domain.conta;

import br.com.mybank.ConnectionFactory;
import br.com.mybank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class ContaService {

    private final ConnectionFactory connectionFactory;
    private final Set<Conta> contas = new HashSet<>();

    public ContaService() {
        this.connectionFactory = new ConnectionFactory();
    }

    public Set<Conta> listarContasAbertas() {
        return new AccountDao(connectionFactory.getConnection()).list();
    }

    public Conta accountPerNumber(Integer accountNumber) {
        return new AccountDao(connectionFactory.getConnection()).accountPerNumber(accountNumber);
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {
        Connection connection = connectionFactory.getConnection();
        new AccountDao(connection).save(dadosDaConta);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        BigDecimal newValue = conta.getSaldo().subtract(valor);
        updateBalance(newValue, conta);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        BigDecimal newValue = conta.getSaldo().add(valor);
        updateBalance(newValue, conta);
    }

    public void realizarTransferencia(Integer numberConteOrigem, Integer numeroContaDestino, BigDecimal valor) {
        this.realizarSaque(numberConteOrigem, valor);
        this.realizarDeposito(numeroContaDestino, valor);
    }

    private void updateBalance(BigDecimal valor, Conta conta) {
        Connection connection = connectionFactory.getConnection();
        new AccountDao(connection).updateBalance(conta.getNumero(), valor);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contas.remove(conta);
    }

    private Conta buscarContaPorNumero(Integer numero) {
        Connection connection = connectionFactory.getConnection();
        Conta conta = new AccountDao(connection).accountPerNumber(numero);

        if (conta != null) {
            return conta;
        } else {
            throw new RegraDeNegocioException("Account doesn't exists.");
        }
    }
}
