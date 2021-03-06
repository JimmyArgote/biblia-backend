package br.com.biblia.test.dao;

import java.util.List;

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
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.enums.IdiomaEnum;
import br.com.biblia.model.versiculo.Expressao;
import br.com.biblia.model.versiculo.ExpressaoKey;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;
import br.com.biblia.test.base.VersiculoBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@Transactional
@Rollback
public class VersiculoDAOTest extends VersiculoBaseTest {

	@Autowired
	private VersiculoDAO dao;
	
	@Test
	public void testSaveVersiculo() {
		VersiculoKey key = VersiculoKey.builder()
							.capituloId(1)
							.id(1)
							.livroId(1)
							.versaoId(1)
							.build();
		Versiculo entity = Versiculo.builder()
							.idioma(IdiomaEnum.GREGO)
							.numero(1)
							.texto("textqwewq")
							.key(key)
							.build();
		dao.save(entity);
		
	}
	
	@Test
	public void testFindAllAfterDeleteAll() {
		dao.deleteAllInBatch();
		List<Versiculo> list = dao.findAll();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.isEmpty());
	}
	
	@Test
	public void testGetOne() {
		Versiculo versiculoToSave = dao.findAll().get(0);
		VersiculoKey key = versiculoToSave.getKey();
		versiculoToSave.getExpressoes().add( instanceExpressao(1, key, 1, 2, "a", "bla") );
		dao.saveAndFlush( versiculoToSave );
		Versiculo one = dao.getOne( key );
		Assert.assertNotNull(one);
		Assert.assertNotNull( one.getTexto() );
		Assert.assertNotNull( one.getExpressoes() );
		Assert.assertFalse( one.getExpressoes().isEmpty() );
	}
	
	private Expressao instanceExpressao(Integer expressaoId, VersiculoKey k, Integer start, Integer fim, String texto, String descricao) {
		return Expressao
					.builder()
					.key( ExpressaoKey
								.builder()
								.expressaoId(expressaoId)
								.versiculoId(k.getId())
								.capituloId(k.getCapituloId())
								.livroId(k.getLivroId())
								.versaoId(k.getVersaoId())
								.build() )
					.inicio(start)
					.fim(fim)
					.texto(texto)
					.build();
	}
	
}
