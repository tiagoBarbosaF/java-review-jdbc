package br.com.mybank.domain.conta;

import br.com.mybank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.util.Objects;

public class Conta {

    private Integer numero;
    private BigDecimal saldo;
    private Cliente titular;
    private Boolean isActive;

    public Conta(Integer numero, Cliente titular) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = BigDecimal.ZERO;
        this.isActive = true;
    }

    public Conta(Integer numero, Cliente titular, BigDecimal saldo) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }

    public Conta(Integer numero, Cliente titular, BigDecimal saldo, Boolean estaAtiva) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
        this.isActive = estaAtiva;
    }

    public boolean possuiSaldo() {
        return this.saldo.compareTo(BigDecimal.ZERO) != 0;
    }

    public void sacar(BigDecimal valor) {
        this.saldo = this.saldo.subtract(valor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return numero.equals(conta.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Conta{" +
               "numero='" + numero + '\'' +
               ", saldo=" + saldo +
               ", titular=" + titular +
               '}';
    }

    public Integer getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Cliente getTitular() {
        return titular;
    }

    public Boolean getActive() {
        return isActive;
    }
}
