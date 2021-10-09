package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);


        Assert.assertEquals(1, 1);
        Assert.assertEquals(0.0121, 0.01134, 0.01);
        Assert.assertEquals(Math.PI, 3.14, 0.01);


        int i1 = 5;
        Integer i2 = 5;
        //Assert.assertEquals(i1, i2); da erro pois nao compara tipo primitivo com objeto
        Assert.assertEquals(Integer.valueOf(i1),i2);
        Assert.assertEquals(i1,i2.intValue());


        Assert.assertEquals("bola", "bola");
        Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
        Assert.assertTrue("bola".startsWith("bo"));

        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = u2;
        Usuario u4 = null;

        Assert.assertEquals(u1, u2); //Apenas funcionou pois adicionei o Equals na Classe Usuario

        Assert.assertSame(u3, u2); //Para verificar instancias

        Assert.assertNull(u4);
        Assert.assertNotNull(u3);

    }
}
