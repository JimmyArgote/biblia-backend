package br.com.biblia.apps.dicionario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.biblia.dao.DicionarioDAO;
import br.com.biblia.enums.IdiomaEnum;
import br.com.biblia.model.Dicionario;
import br.com.biblia.model.DicionarioKey;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class DicionarioFacade implements DicionarioApp {

	@Autowired
	private DicionarioDAO dao;
	
	@Override
	public Dicionario save(Dicionario entity) {
		dao.save( entity );
		return entity;
	}

	@Override
	public List<Dicionario> search(IdiomaEnum idioma) {
		return dao.search(idioma);
	}

	@Override
	public Dicionario findOne(DicionarioKey key) {
		return dao.findById( key ).get();
	}

	@Override
	public void deleteById(DicionarioKey key) {
		dao.deleteById( key );
	}

	@Override
	public void createDefaultIfNotExists(Integer codigo, IdiomaEnum idioma) {
		DicionarioKey key = new DicionarioKey(codigo, idioma);
		if ( !dao.existsById(key) ) {
			dao.save( new Dicionario( key, "Não há definição para esta palavra", false) );
		}
	}

}
