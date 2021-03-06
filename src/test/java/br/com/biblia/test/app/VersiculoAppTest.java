package br.com.biblia.test.app;

import java.util.List;
import java.util.NoSuchElementException;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblia.Application;
import br.com.biblia.apps.versiculo.VersiculoApp;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.enums.IdiomaEnum;
import br.com.biblia.model.sentenca.Sentenca;
import br.com.biblia.model.versiculo.Expressao;
import br.com.biblia.model.versiculo.ExpressaoDicionario;
import br.com.biblia.model.versiculo.ExpressaoDicionarioKey;
import br.com.biblia.model.versiculo.ExpressaoKey;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;
import br.com.biblia.test.base.VersiculoBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@Transactional
@Rollback
public class VersiculoAppTest extends VersiculoBaseTest {

	@Autowired
	private VersiculoDAO dao;
	
	@Autowired
	private VersiculoApp app;
	
	@Test
	public void testFindOneWhenExists() {
		Versiculo mateus1_1 = getMateus1_1();
		Versiculo versiculoFinded = app.findOne(mateus1_1.getKey());
		Assert.assertNotNull(versiculoFinded);
	}
	
	@Test(expected=NoSuchElementException.class)
	public void shouldDoErrorFindOneInexistent() {
		VersiculoKey key = VersiculoKey
							.builder()
							.id(-1)
							.capituloId(-3)
							.livroId(-5123)
							.build();
		app.findOne(key);
	}
	
	@Test
	public void testCountOcorrenciasTermo() {
		Integer qtd = app.qtdOcorrenciasTermo("trombeta");
		
		Assert.assertNotNull(qtd);
		Assert.assertEquals(new Integer(81), qtd);
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testDeleteByKey() {
	    
		Versiculo mateus1_1 = getMateus1_1();
		app.deleteByKey( mateus1_1.getKey() );
	    Assert.assertNull( dao.findById(mateus1_1.getKey()).get() );
	}
	
	@Test
	public void testGetVersiculoMontado() {
		Versiculo mateus1_1 = getMateus1_1();
		
		VersiculoKey k = mateus1_1.getKey();
		
		List<Expressao> expressoes = Lists.newArrayList();

		expressoes.add( instanceExpressao(1, k, 11, 18, "geração", 1, 2) ); 
		expressoes.add( instanceExpressao(2, k, 19, 27, "de Jesus", 2) ); 
		mateus1_1.setExpressoes(expressoes);
		
		dao.saveAndFlush(mateus1_1);
		
		mateus1_1 = dao.findById(k).get();
		
		System.out.println( mateus1_1.getVersiculoMontado() );
		
		String oldExpected = "¶ Livro da geração de Jesus Cristo, filho de Davi, filho de Abraão.";
		String newExpected = "¶ Livro da <span class=\"texto\" dic=\"1,2\">geração</span> <span class=\"texto\" dic=\"2\">de Jesus</span> Cristo, filho de Davi, filho de Abraão.";
		
		Assert.assertEquals(newExpected, mateus1_1.getVersiculoMontado());
		
	}
	
	@Test
	public void testGetVersiculoMontadoWhenMateus1_2() {
		Versiculo mateus1_2 = getMateus1_2();
		
		VersiculoKey k = mateus1_2.getKey();
		
		List<Expressao> expressoes = Lists.newArrayList();

		expressoes.add( instanceExpressao(1, k, 7, 12, "gerou", 1) ); 
		mateus1_2.setExpressoes(expressoes);
		
		dao.saveAndFlush(mateus1_2);
		
		mateus1_2 = dao.findById(k).get();
		
		System.out.println( mateus1_2.getVersiculoMontado() );
			
		String oldExpected = "Abraão gerou a Isaque; e Isaque gerou a Jacó; e Jacó gerou a Judá e a seus irmãos;";
		String newExpected = "Abraão <span class=\"texto\" dic=\"1\">gerou</span> a Isaque; e Isaque gerou a Jacó; e Jacó gerou a Judá e a seus irmãos;";
		
		Assert.assertEquals(newExpected, mateus1_2.getVersiculoMontado());
		
	}
	
	@Test
	public void testGetVersiculoMontadoWhenMateus5_6() {
		Versiculo mateus5_6 = getMateus5_6();
		
		VersiculoKey k = mateus5_6.getKey();
		
		List<Expressao> expressoes = Lists.newArrayList();

		expressoes.add( instanceExpressao(1, k, 16, 31, "os que têm fome", 3983) );
		expressoes.add( instanceExpressao(2, k, 42, 49, "justiça", 1343) );
		mateus5_6.setExpressoes(expressoes);
		
		dao.saveAndFlush(mateus5_6);
		
		mateus5_6 = dao.findById(k).get();
		
		System.out.println( mateus5_6.getVersiculoMontado() );
		
		String oldExpected = "Bem-aventurados os que têm fome e sede de justiça, porque eles serão fartos;";
		String newExpected = "Bem-aventurados <span class=\"texto\" dic=\"3983\">os que têm fome</span> e sede de <span class=\"texto\" dic=\"1343\">justiça</span>, porque eles serão fartos;";
		
		Assert.assertEquals(newExpected, mateus5_6.getVersiculoMontado());
		
	}

	private Expressao instanceExpressao(Integer expressaoId, VersiculoKey k, Integer start, Integer fim, String texto, Integer... dics) {
		Expressao expressao = Expressao
					.builder()
					.key( ExpressaoKey
								.builder()
								.expressaoId(expressaoId)
								.versiculoId(k.getId())
								.capituloId(k.getCapituloId())
								.livroId(k.getLivroId())
								.versaoId(1)
								.build() )
					.inicio(start)
					.fim(fim)
					.texto(texto)
					.build();
		
		expressao.setDicionarios( Lists.newArrayList() );
		for (Integer dic : dics) {
			expressao.getDicionarios().add( ExpressaoDicionario.builder()
																	  .key( new ExpressaoDicionarioKey(dic, IdiomaEnum.GREGO, expressao.getKey()))
																	  .build() );
		}
		return expressao;
	}
	
}
