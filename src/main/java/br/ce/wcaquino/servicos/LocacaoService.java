package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.repository.LocacaoRepository;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {

    private LocacaoRepository locacaoRepository;
    private EmailService emailService;
    private SPCService spcService;

    public LocacaoService() {
    }

    public LocacaoService(LocacaoRepository locacaoRepository, EmailService emailService, SPCService spcService) {
        this.locacaoRepository = locacaoRepository;
        this.emailService = emailService;
        this.spcService = spcService;
    }

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {


        if (usuario == null) {
            throw new LocadoraException("Usuario vazio.");
        }

        if (filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme vazio");
        }

        for (Filme filme : filmes) {
            if (filme.getEstoque() == 0) {
                throw new FilmeSemEstoqueException();
            }
        }

        try {
            if(spcService.possuiNegativacao(usuario)) {
                throw  new LocadoraException("Usuário negativado.");
            }
        } catch (LocadoraException e) {
           throw new LocadoraException("Problemas com SPC, tente novamente");
        }


        Locacao locacao = new Locacao();
        locacao.setFilmes(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());

        Double valorTotal = 0d;
        for (int i = 0; i < filmes.size(); i++) {
            Filme filme = filmes.get(i);
            Double valorFilme = filme.getPrecoLocacao();

            switch (i) {
                case 2:
                    valorFilme = valorFilme * 0.75;
                    break;
                case 3:
                    valorFilme = valorFilme * 0.5;
                    break;
                case 4:
                    valorFilme = valorFilme * 0.25;
                    break;
                case 5:
                    valorFilme = valorFilme * 0.;
                    break;
            }

            valorTotal += valorFilme;
        }
        locacao.setValor(valorTotal);

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);
        if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
            dataEntrega = adicionarDias(dataEntrega, 1);
        }
        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        locacaoRepository.salvar(locacao);

        return locacao;
    }

    public void notificarAtrasos() {
        List<Locacao> locacoes = locacaoRepository.obterLocacoesPendentes();
        for (Locacao locacao : locacoes) {
            emailService.notificarAtraso(locacao.getUsuario());
        }
    }

    public void prorrogarLocacao(Locacao locacao, int dias) {
        Locacao novaLocacao = new Locacao();
        novaLocacao.setUsuario(locacao.getUsuario());
        novaLocacao.setFilmes(locacao.getFilmes());
        novaLocacao.setDataLocacao(new Date());
        novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
        novaLocacao.setValor(locacao.getValor());
        locacaoRepository.salvar(novaLocacao);
    }

    public void setLocacaoRepository(LocacaoRepository locacaoRepository) {
        this.locacaoRepository = locacaoRepository;
    }

    public void setSpcService(SPCService spcService) {
        this.spcService = spcService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}