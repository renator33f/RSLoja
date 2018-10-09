package angular.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import angular.model.PedidoBean;

public class Teste {

	
	public static void main(String[] args) {
		String json = "{\"pedido\":{\"valorTotal\":\"R$970.00\",\"data\":\"\"}}";
		
		PedidoBean pedidoBean = new Gson().fromJson(json,
				PedidoBean.class);
	}
}
