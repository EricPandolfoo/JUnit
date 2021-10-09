package builder;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Date;

import static builder.FilmeBuilder.umFilme;

public class LocacaoBuilder {

    private Locacao locacao;

    public LocacaoBuilder() {
    }

    public static LocacaoBuilder umaLocacao() {
        LocacaoBuilder builder = new LocacaoBuilder();
        builder.locacao = new Locacao();

        builder.locacao.setFilmes(Arrays.asList(umFilme().agora()));
        builder.locacao.setValor(5.0);
        builder.locacao.setDataLocacao(new Date());
        builder.locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(2));
        builder.locacao.setUsuario(UsuarioBuilder.umUsuario().agora());
        return builder;
    }

    public LocacaoBuilder comUsuario(Usuario usuario) {
        locacao.setUsuario(usuario);
        return this;
    }

    public Locacao agora() {
        return locacao;
    }
}
