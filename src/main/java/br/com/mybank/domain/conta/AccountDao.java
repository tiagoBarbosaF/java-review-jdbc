package br.com.mybank.domain.conta;

import br.com.mybank.domain.cliente.Cliente;
import br.com.mybank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AccountDao {
    private final Connection connection;

    public AccountDao(Connection connection) {
        this.connection = connection;
    }

    public void save(DadosAberturaConta dadosDaConta) {
        var client = new Cliente(dadosDaConta.dadosCliente());
        var account = new Conta(dadosDaConta.numero(), client);

        String sql = """
                    INSERT INTO account (account_number, balance, client_name, client_cpf, client_email)
                    VALUES(?,?,?,?,?);
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5, dadosDaConta.dadosCliente().email());

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Set<Conta> list() {
        Set<Conta> accounts = new HashSet<>();
        String sql = """
                SELECT * FROM account;
                """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int account_number = resultSet.getInt(1);
                BigDecimal balance = resultSet.getBigDecimal(2);
                String clientName = resultSet.getString(3);
                String clientCpf = resultSet.getString(4);
                String clientEmail = resultSet.getString(5);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(clientName, clientCpf, clientEmail);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                accounts.add(new Conta(account_number, cliente));
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            System.out.println("conexao fechada.");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return accounts;
    }

    public Conta accountCpf(String cpf) {
        Conta conta = null;
        String sql = """
                SELECT *
                FROM account
                WHERE
                client_cpf = ?;
                """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cpf);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int account_number = resultSet.getInt(1);
                BigDecimal balance = resultSet.getBigDecimal(2);
                String clientName = resultSet.getString(3);
                String clientCpf = resultSet.getString(4);
                String clientEmail = resultSet.getString(5);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(clientName, clientCpf, clientEmail);
                Cliente cliente = new Cliente(dadosCadastroCliente);
                conta = new Conta(account_number, cliente);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            return conta;
    }
}
