package br.com.mybank.domain.conta;

import br.com.mybank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountDao {
    private Connection connection;

    public AccountDao(Connection connection) {
        this.connection = connection;
    }

    public void saveAccount(DadosAberturaConta dadosDaConta) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
