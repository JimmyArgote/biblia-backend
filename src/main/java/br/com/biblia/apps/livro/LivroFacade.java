package br.com.biblia.apps.livro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.biblia.dao.LivroDAO;
import br.com.biblia.enums.Testamento;
import br.com.biblia.model.livro.Livro;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class LivroFacade implements LivroApp {

	@Autowired
	private LivroDAO dao;

	@Override
	public Livro save(Livro entity) {
		if ( entity.getId() == null ) {
			entity.setId( dao.retrieveNextId() );
		}
		dao.save(entity);
		return entity;
	}

	@Override
	public List<Livro> findAll() {
		return dao.findAll();
	}

	@Override
	public Livro findOne(Integer id) {
		return dao.findById(id).get();
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public List<Livro> searchByTestamento(Testamento testamento) {
		return dao.searchByTestamento(testamento);
	}


}
