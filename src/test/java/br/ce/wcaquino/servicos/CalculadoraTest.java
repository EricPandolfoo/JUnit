package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exception.NaoPodeDividirPorZeroException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculadoraTest {

    private Calculadora calculadora;

    @Before
    public void setup() {
        calculadora = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calculadora.somar(a, b);


        //verificacao
        assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
        //cenario
        int a = 5;
        int b = 2;

        //acao
        int resultado = calculadora.subtrair(a, b);

        //verificao
        assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() {
        //cenario
        int a = 15;
        int b = 3;

        //acao
        int resultado = calculadora.dividir(a, b);

        //verificao
        assertEquals(5, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() {
        //cenario
        int a = 15;
        int b = 0;

        //acao
        int resultado = calculadora.dividir(a, b);

        //verificao
        assertEquals(5, resultado);
    }
}
