package angular.controller;

import java.util.ArrayList;
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
import angular.model.Vendedor;

@Controller
@RequestMapping(value="/vendedor")
public class VendedorController extends DaoImplementacao<Vendedor> implements 
		DaoInterface<Vendedor> {

	public VendedorController(Class<Vendedor> persistenceClass) { 
		super(persistenceClass); 
	}
	  
	/**
	 * Salva ou Atualiza o Vendedor
	 * @param jsonVendedor
	 * @return ResponseEntity
	 * @throws Exception
	 */
	 @RequestMapping(value="salvar", method= RequestMethod.POST)
	 @ResponseBody
	 public ResponseEntity salvar (@RequestBody String jsonVendedor) throws Exception {
		 Vendedor vendedor = new Gson().fromJson(jsonVendedor, Vendedor.class);
		 
		 if (vendedor != null && vendedor.getAtivo() == null){
			 vendedor.setAtivo(false);
		 }
		 
		 super.salvarOuAtualizar(vendedor);
		 return new ResponseEntity(HttpStatus.CREATED);
		 
	 }
	
	
	 /**
	  * Retorna a lista de vendedor cadastrados
	  * @return JSON String de Vendedor
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
	 * Deleta o vendedor informado
	 * @param codVendedor
	 * @return String vazia como resposta
	 * @throws Exception
	 */
	@RequestMapping(value="deletar/{codVendedor}", method=RequestMethod.DELETE)
	public  @ResponseBody String deletar (@PathVariable("codVendedor") String codVendedor) throws Exception {
		super.deletar(loadObjeto(Long.parseLong(codVendedor)));
		return "";
	}
	
	
	/**
	 * Consulta e retorna o vendedor com o codigo informado
	 * @param codVendedor
	 * @return JSON vendedor pesquisado
	 * @throws Exception
	 */
	@RequestMapping(value="buscarvendedor/{codVendedor}", method=RequestMethod.GET)
	public  @ResponseBody byte[] buscarVendedor (@PathVariable("codVendedor") String codVendedor) throws Exception {
		Vendedor objeto = super.loadObjeto(Long.parseLong(codVendedor));
		if (objeto == null) {
			return "{}".getBytes("UTF-8");
		}
		return new Gson().toJson(objeto).getBytes("UTF-8");
	}
	
	
	/**
	 * Consulta e retorna o vendedor com o nome  informado
	 * @param nomeVendedor
	 * @return JSON vendedor pesquisado
	 * @throws Exception
	 */
	@RequestMapping(value="buscarnome/{nomeVendedor}", method=RequestMethod.GET)
	public  @ResponseBody byte[] buscarNome (@PathVariable("nomeVendedor") String nomeVendedor) throws Exception {
		List<Vendedor> vendedores = new ArrayList<Vendedor>();
		vendedores = super.listaLikeExpression("nome", nomeVendedor);

		if (vendedores == null || vendedores.isEmpty() ) {
			return "{}".getBytes("UTF-8");
		}
		
		return new Gson().toJson(vendedores).getBytes("UTF-8");
	}


}
