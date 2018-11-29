package angular.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import angular.dao.DaoImplementacao;
import angular.dao.DaoInterface;
import angular.model.Autor;

@Controller
@RequestMapping(value="/autor")
public class AutorController extends DaoImplementacao<Autor> implements 
		DaoInterface<Autor> {

	public AutorController(Class<Autor> persistenceClass) { 
		super(persistenceClass); 
	}
	  
	/**
	 * Salva ou Atualiza o Autor
	 * @param jsonAutor
	 * @return ResponseEntity
	 * @throws Exception
	 */
	 @RequestMapping(value="salvar", method= RequestMethod.POST)
	 @ResponseBody
	 public ResponseEntity salvar (@RequestBody String jsonAutor) throws Exception {
		 Autor autor = new Gson().fromJson(jsonAutor, Autor.class);
		 
		 if (autor != null && autor.getId() == null){
			 autor.setId(null);
		 }
		 
		 super.salvarOuAtualizar(autor);
		 return new ResponseEntity(HttpStatus.CREATED);
		 
	 }
	
	
	 /**
	  * Retorna a lista de Autor cadastrados
	  * @return JSON String de Autores
	  * @throws Exception
	  */
	@RequestMapping(value="listar/{numeroPagina}", method=RequestMethod.GET, headers = "Accept=application/json") 
	@ResponseBody
	public byte[] listar(@PathVariable("numeroPagina") String numeroPagina) throws Exception {
		return new Gson().toJson(super.consultaPaginada(numeroPagina)).getBytes("UTF-8");
	}
	
	
	@RequestMapping(value="totalPagina", method=RequestMethod.GET, headers = "Accept=application/json") 
	@ResponseBody
	public String totalPagina() throws Exception {
		return ""+super.quantidadePagina(); 
	}
	 
	/**
	 * Deleta o Autor informado
	 * @param codAutor
	 * @return String vazia como resposta
	 * @throws Exception
	 */
	@RequestMapping(value="deletar/{codAutor}", method=RequestMethod.DELETE)
	public  @ResponseBody String deletar (@PathVariable("codAutor") String codAutor) throws Exception {
		super.deletar(loadObjeto(Long.parseLong(codAutor)));
		return "";
	}
	
	
	/**
	 * Consulta e retorna o Autor com o codigo informado
	 * @param codAutor
	 * @return JSON Autor pesquisado
	 * @throws Exception
	 */
	@RequestMapping(value="buscarautor/{codAutor}", method=RequestMethod.GET)
	public  @ResponseBody byte[] buscarAutor (@PathVariable("codAutor") String codAutor) throws Exception {
		Autor objeto = super.loadObjeto(Long.parseLong(codAutor));
		if (objeto == null) {
			return "{}".getBytes("UTF-8");
		}
		return new Gson().toJson(objeto).getBytes("UTF-8");
	}
	
	
	/**
	 * Consulta e retorna o Autor com o nome  informado
	 * @param nomeAutor
	 * @return JSON Autor pesquisado
	 * @throws Exception
	 */
	@RequestMapping(value="buscarnome/{nomeAutor}", method=RequestMethod.GET)
	public  @ResponseBody byte[] buscarNome (@PathVariable("nomeAutor") String nomeAutor) throws Exception {
		List<Autor> autores = new ArrayList<Autor>();
		autores = super.listaLikeExpression("nome", nomeAutor);

		if (autores == null || autores.isEmpty() ) {
			return "{}".getBytes("UTF-8");
		}
		
		return new Gson().toJson(autores).getBytes("UTF-8");
	}
	
	
	@RequestMapping(value = "listartodos", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String listartodos()
			throws Exception {
		return new Gson().toJson(super.lista());
	}


}

