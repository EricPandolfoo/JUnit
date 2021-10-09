package br.ce.wcaquino.repository;

import br.ce.wcaquino.entidades.Locacao;

import java.util.List;

public interface LocacaoRepository {

    public void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();

}
