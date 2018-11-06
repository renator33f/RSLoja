package angular.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import angular.dao.DaoImplementacao;
import angular.dao.DaoInterface;
import angular.model.Genero;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/genero")
public class GeneroController extends DaoImplementacao<Genero> implements
		DaoInterface<Genero> {

	public GeneroController(Class<Genero> persistenceClass) {
		super(persistenceClass);
	}
	
	/**
	 * Faz o carregamento da lista de Generos
	 * @return List<Generos> 
	 * @throws Exception
	 */
	@RequestMapping(value = "listar", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String listar() throws Exception {
		return new Gson().toJson(super.lista());
	}

}
