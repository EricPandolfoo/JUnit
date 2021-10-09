package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.repository.LocacaoRepository;
import br.ce.wcaquino.utils.DataUtils;
import matchers.DiaSemanaMatcher;
import matchers.MatchersProprios;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static builder.FilmeBuilder.umFilme;
import static builder.FilmeBuilder.umFilmeSemEstoque;
import static builder.LocacaoBuilder.umaLocacao;
import static builder.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private LocacaoRepository repository;
    private EmailService emailService;
    private LocacaoService service;
    private SPCService spc;

    @Before
    public void setup() {
        service = new LocacaoService();

        repository = mock(LocacaoRepository.class);
        service.setLocacaoRepository(repository);

        spc = mock(SPCService.class);
        service.setSpcService(spc);

        emailService = mock(EmailService.class);
        service.setEmailService(emailService);
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        //acao
        service.alugarFilme(usuario, filmes);
    }


    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        //acao
        try {
            service.alugarFilme(null, filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio."));
        }
        System.out.println("Forma robusta");
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);

        System.out.println("Forma nova");
    }

    @Test
    public void deveAlugarComDataCorreta() {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Locacao locacao = null;
        try {
            locacao = service.alugarFilme(usuario, filmes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //verificacao
        assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    }


    @Test
    public void dataRetornoLocacaoCorreta() {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Locacao locacao = null;
        try {
            locacao = service.alugarFilme(usuario, filmes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //verificacao
        assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes =
                Arrays.asList(
                        new Filme("Matrix 1", 2, 4.0),
                        new Filme("Matrix 2", 2, 4.0),
                        new Filme("Matrix 3", 2, 4.0)
                );

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes =
                Arrays.asList(
                        new Filme("Matrix 1", 2, 4.0),
                        new Filme("Matrix 2", 2, 4.0),
                        new Filme("Matrix 3", 2, 4.0),
                        new Filme("Matrix 4", 2, 4.0)
                );

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes =
                Arrays.asList(
                        new Filme("Matrix 1", 2, 4.0),
                        new Filme("Matrix 2", 2, 4.0),
                        new Filme("Matrix 3", 2, 4.0),
                        new Filme("Matrix 4", 2, 4.0),
                        new Filme("Matrix 5", 2, 4.0)
                );

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes =
                Arrays.asList(
                        new Filme("Matrix 1", 2, 4.0),
                        new Filme("Matrix 2", 2, 4.0),
                        new Filme("Matrix 3", 2, 4.0),
                        new Filme("Matrix 4", 2, 4.0),
                        new Filme("Matrix 5", 2, 4.0),
                        new Filme("Matrix 6", 2, 4.0)
                );

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(14.0));
    }


    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Locacao retorno = service.alugarFilme(usuario, filmes);

        assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
        assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(usuario)).thenReturn(true);

        exception.expect(LocadoraException.class);
        exception.expectMessage("Usuário negativado.");

        service.alugarFilme(usuario, filmes);

        verify(spc).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {
        Usuario usuario = umUsuario().agora();

        List<Locacao> locacaos = Arrays.asList(
                umaLocacao()
                        .comUsuario(usuario)
                        .agora());

        when(repository.obterLocacoesPendentes()).thenReturn(locacaos);

        service.notificarAtrasos();

        verify(emailService).notificarAtraso(usuario);
    }
}
