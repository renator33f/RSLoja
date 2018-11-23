package angular.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import angular.model.ItemPedido;
import angular.model.Livro;
import angular.model.Pedido;
import angular.model.Vendedor;

@Controller
@RequestMapping(value="/itempedido")
public class ItemPedidoController extends DaoImplementacao<ItemPedido> implements
		DaoInterface<ItemPedido> {
	
	@Autowired
	private LivroController livroController;
	private ItemPedidoController itemPedidoController;
	/* private FilmeController filmeController; */

	public ItemPedidoController(Class<ItemPedido> persistenceClass) {
		super(persistenceClass);
	}
	
	@RequestMapping(value="processar/{itens}")
	public @ResponseBody String processar(@PathVariable("itens") String itens) throws Exception{
		List<Livro> livros = livroController.lista(itens);
		List<ItemPedido> itemPedidos = new ArrayList<ItemPedido>();
		
		Pedido pedido = new Pedido();
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (Livro livro: livros) {
			String valor = livro.getValor().replace("R", "").replace("$", "").replaceAll("\\.", "").replaceAll("\\,", ".");
			valorTotal = valorTotal.add(new BigDecimal(valor.trim()));
		}		
		
		pedido.setValorTotal("R$" + valorTotal.setScale(2, RoundingMode.HALF_DOWN).toString());
		for (Livro livro: livros) {
			ItemPedido itemPedido  = new ItemPedido();
			itemPedido.setLivro(livro);
			itemPedido.setPedido(pedido);
			itemPedido.setQuantidade(1L);
			itemPedido.setPrecounit(livro.getValor());
			itemPedidos.add(itemPedido);
		}
		
		return new Gson().toJson(itemPedidos);
	}
	
	/*
	@RequestMapping(value="processarf/{itens}")
	public @ResponseBody String processarf(@PathVariable("itens") String itens) throws Exception{
		List<Filme> filmes = filmeController.lista(itens);
		List<ItemPedido> itemPedidos = new ArrayList<ItemPedido>();
		
		Pedido pedido = new Pedido();
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (Filme filme: filmes) {
			String valor = filme.getValor().replace("R", "").replace("$", "").replaceAll("\\.", "").replaceAll("\\,", ".");
			valorTotal = valorTotal.add(new BigDecimal(valor.trim()));
		}		
		
		pedido.setValorTotal("R$" + valorTotal.setScale(2, RoundingMode.HALF_DOWN).toString());
		for (Filme filme: filmes) {
			ItemPedido itemPedido  = new ItemPedido();
			itemPedido.setFilme(filme);
			itemPedido.setPedido(pedido);
			itemPedido.setQuantidade(1L);
			itemPedido.setPrecounit(filme.getValor());
			itemPedidos.add(itemPedido);
		}
		
		return new Gson().toJson(itemPedidos);
	}
	*/
	
	
	
	
	/**
	 * Consulta e retorna os itens do pedido com o codigo informado
	 * @param codPedido
	 * @return JSON pedido pesquisado
	 * @throws Exception
	 */
	@RequestMapping(value = "listar/{codPedido}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String listar() throws Exception {
		return new Gson().toJson(super.lista());
	}	
	
	
	
	
	

}
