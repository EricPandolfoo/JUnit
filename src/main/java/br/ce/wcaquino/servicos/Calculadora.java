package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exception.NaoPodeDividirPorZeroException;

public class Calculadora {

    public int somar(int a, int b) {
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public int dividir(int a, int b) {
       if (b == 0) {
           throw new NaoPodeDividirPorZeroException("Nao pode dividir por zero.");
       } else
        return a / b;
    }
}
