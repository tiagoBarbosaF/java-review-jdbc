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
                    INSERT INTO account (account_number, balance, client_name, client_cpf, client_email, is_active)
                    VALUES(?,?,?,?,?,?);
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5, dadosDaConta.dadosCliente().email());
            preparedStatement.setBoolean(6, true);

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
                SELECT * FROM account WHERE is_active = true;
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

                accounts.add(new Conta(account_number, cliente, balance));
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

    public Conta accountPerNumber(Integer accountNumber) {
        Conta conta = null;
        String sql = """
                SELECT *
                FROM account
                WHERE
                account_number = ?;
                """;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int account_number = resultSet.getInt(1);
                BigDecimal balance = resultSet.getBigDecimal(2);
                String clientName = resultSet.getString(3);
                String clientCpf = resultSet.getString(4);
                String clientEmail = resultSet.getString(5);
                Boolean isActive = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(clientName, clientCpf, clientEmail);
                Cliente cliente = new Cliente(dadosCadastroCliente);
                conta = new Conta(account_number, cliente, balance, isActive);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }

    public void updateBalance(Integer accountNumber, BigDecimal value) {
        PreparedStatement preparedStatement;
        String sql = """
                UPDATE account
                SET balance = ?
                WHERE
                account_number = ?;
                """;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBigDecimal(1, value);
            preparedStatement.setInt(2, accountNumber);

            preparedStatement.execute();
            connection.commit();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteAccount(Integer accountNumber) {
        PreparedStatement preparedStatement;
        String sql = """
                DELETE FROM account
                WHERE account_number = ?;
                """;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountNumber);

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void logicalDelete(Integer accountNumber) {
        PreparedStatement preparedStatement;
        String sql = """
                UPDATE account
                SET is_active = false
                WHERE account_number = ?;
                """;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountNumber);

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
