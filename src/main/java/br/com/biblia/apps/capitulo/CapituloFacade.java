package br.com.biblia.apps.capitulo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.biblia.dao.CapituloDAO;
import br.com.biblia.model.Capitulo;
import br.com.biblia.model.CapituloKey;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class CapituloFacade implements CapituloApp {

	@Autowired
	private CapituloDAO dao;
	
	@Override
	public Capitulo save(Capitulo entity) {
		dao.save(entity);
		return entity;
	}

	@Override
	public List<Capitulo> findAll() {
		return dao.findAll();
	}

	@Override
	public Capitulo findOne(CapituloKey key) {
		return dao.findById(key).get();
	}

	@Override
	public void deleteById(CapituloKey key) {
		dao.deleteByKey( key.getId(), key.getLivroId() );
	}

	@Override
	public List<Capitulo> searchByLivro(Integer livroId) {
		return dao.searchByLivro( livroId );
	}

	@Override
	public void save(Integer livroId, Integer capitulos, Integer versaoId) {
		List<Capitulo> list = new ArrayList<>();
		for (int i = 1; i <= capitulos; i++) {
			list.add( new Capitulo( new CapituloKey(i, livroId, versaoId) ) );
		}
		dao.saveAll( list );
	}

	@Override
	public void deleteByLivro(Integer livroId) {
		dao.deleteByLivro(livroId);	
	}

}
