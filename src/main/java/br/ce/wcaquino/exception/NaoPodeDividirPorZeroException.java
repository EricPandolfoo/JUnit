package br.ce.wcaquino.exception;

public class NaoPodeDividirPorZeroException extends RuntimeException {

    public NaoPodeDividirPorZeroException(String message) {
        super(message);
    }
}
