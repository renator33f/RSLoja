//configrações loja de livros
app.controller("lojaController", function($scope, $http, $location, $routeParams) {
	
	if ($routeParams.id != null && $routeParams.id != undefined
			&& $routeParams.id != ''){// se estiver consultando o pedido
		// entra pra consultar
		$http.get("pedido/buscarpedido/" + $routeParams.id).success(function(response) {
			$scope.pedido = response;
			
		}).error(function(data, status, headers, config) {
			erro("Error: " + status);
		});
		
	}else { // novo pedido
		$scope.pedido = {};
	}
	
	/// Consultar o Pedido
	$scope.editarPedido = function(id) {
		$location.path('loja/pedidoconsulta/' + id);
	};
	
	
	$scope.listarItemPedidos = function (id) {
		$http.get("itempedido/listar/" + id).success(function(response) {
			$scope.itemPedidosData = response;
		}).error(function(response) {
			erro("Error: " + response);
		});
	};	
	
	
	
	
	
	
	
	
	
	
	$scope.listarPedidos = function () {
		$http.get("pedido/listar").success(function(response) {
			$scope.pedidosData = response;
		}).error(function(response) {
			erro("Error: " + response);
		});
	};	
	
	$scope.removerPedido = function (codPedido) {
		$http.delete("pedido/deletar/"+codPedido).success(function(response) {
			$scope.pedidosData = response;
		}).error(function(response) {
			erro("Error: " + response);
		});
	};
	
	
	if ($routeParams.codigoPedido != null){
		$scope.codigoPedido = $routeParams.codigoPedido;
	}
	
	$scope.finalizarPedido = function () {

		$scope.pedidoObjeto.cliente = $scope.clienteAdiconado;
		$scope.pedidoObjeto.vendedor = $scope.vendedorAdiconado;
		
		$http.post("pedido/finalizar", {"pedido" : $scope.pedidoObjeto,
			"itens" : $scope.itensCarrinho}).success(function(response) {
			$scope.pedidoObjeto = {};
			$scope.itensCarrinho = {};
			
			$location.path("loja/pedidoconfirmado/"+response);
			
			sucesso("Pedido Finalizado!");
		}).error(function(response) {
			erro("Error: " + response);
		});
	};
	
	
	// Buscar e adicionar cliente ao pedido
	$scope.buscarClienteNome = function () {
		$http.get("cliente/buscarnome/" + $scope.filtroCliente).success(function(response) {
			$scope.clientesPesquisa = response;
		}).error(function(response) {
			erro("Error: " + response);
		});
	};
	
	$scope.adicionarClienteCarrinho = function (cliente) {
		$scope.pedidoObjeto.cliente = cliente;
		$scope.clienteAdiconado = cliente;
		$scope.clientesPesquisa = {};
		$scope.filtroCliente = "";
	};
	
	
	// Buscar e adicionar vendedor ao pedido
	$scope.buscarVendedorNome = function () {
		$http.get("vendedor/buscarnome/" + $scope.filtroVendedor).success(function(response) {
			$scope.vendedoresPesquisa = response;
		}).error(function(response) {
			erro("Error: " + response);
		});
	};
	
	$scope.adicionarVendedorCarrinho = function (vendedor) {
		$scope.pedidoObjeto.vendedor = vendedor;
		$scope.vendedorAdiconado = vendedor;
		$scope.vendedoresPesquisa = {};
		$scope.filtroVendedor = "";
	};
	
	
	
	if ($routeParams.itens != null && $routeParams.itens.length > 0){
		
		$http.get("itempedido/processar/"+ $routeParams.itens).success(function(response) {
			
			$scope.itensCarrinho = response;
			$scope.pedidoObjeto = response[0].pedido;
			
		}).error(function(response) {
			erro("Error: " + response);
		});
		
	}else {
		$scope.carrinhoLivro = new Array();		
	}
	
	// Adicionar Livro ao carrinho
	$scope.addLivro = function (livroid) {
		$scope.carrinhoLivro.push(livroid);
		
	};
	
	/*
    if ($routeParams.itens != null && $routeParams.itens.length > 0){
		
		$http.get("itempedido/processarf/"+ $routeParams.itens).success(function(response) {
			
			$scope.itensCarrinho = response;
			$scope.pedidoObjeto = response[0].pedido;
			
		}).error(function(response) {
			erro("Error: " + response);
		});
		
	}else {
		$scope.carrinhoFilme = new Array();		
	}	
	
	// Adicionar Filme ao carrinho
	$scope.addFilme = function (filmeid) {
		$scope.carrinhoFilme.push(filmeid);
		
	};
	*/
	
	
	$scope.recalculo = function (quantidade, livro) {
		var valorTotal = new Number();
		for (var i = 0; i < $scope.itensCarrinho.length; i++){
				var valorLivro = $scope.itensCarrinho[i].livro.valor.replace("R","").replace("$", "").replace(".","").replace(",", ".");
				if ($scope.itensCarrinho[i].livro.id == livro){
					valorTotal += parseFloat(valorLivro * quantidade);
				}else {
					valorTotal += parseFloat(valorLivro * $scope.itensCarrinho[i].quantidade);
				}
				
		}
		 $scope.pedidoObjeto.valorTotal = 'R$' + valorTotal.toString();
	};
	
	
	$scope.removerLivroCarrinho = function (livroid) {
		
		$scope.intensTemp = new Array();
		var valorTotal = new Number();
		for (var i = 0; i < $scope.itensCarrinho.length; i++){
			if ($scope.itensCarrinho[i].livro.id === livroid){
			}else {
				// itens validos
				$scope.intensTemp.push($scope.itensCarrinho[i]);
				
				var valorLivro = $scope.itensCarrinho[i].livro.valor.replace("R","").replace("$", "").replace(".","").replace(",", ".");
				valorTotal += parseFloat(valorTotal) + parseFloat(valorLivro * $scope.itensCarrinho[i].quantidade);
				
			};
		}
		 $scope.pedidoObjeto.valorTotal = 'R$' + valorTotal.toString();
		 $scope.itensCarrinho = $scope.intensTemp;
	};
	
	
	$scope.fecharPedido = function() {
		$location.path('loja/intensLoja/' + $scope.carrinhoLivro);
	};
	
	
	// listar todos os livros
	$scope.listarLivros = function(numeroPagina) {
		$scope.numeroPagina = numeroPagina;
		$http.get("livro/listar/" + numeroPagina).success(function(response) {
			$scope.data = response;
			
			//---------Inicio total página----------
				$http.get("livro/totalPagina").success(function(response) {
					$scope.totalPagina = response;
				}).error(function(response) {
					erro("Error: " + response);
				});
			//---------Fim total página----------
			
		}).error(function(response) {
			erro("Error: " + response);
		});
	};
	
	/*
	// listar todos os filmes
	$scope.listarFilmes = function(numeroPagina) {
		$scope.numeroPagina = numeroPagina;
		$http.get("filme/listar/" + numeroPagina).success(function(response) {
			$scope.data = response;
			
			//---------Inicio total página----------
				$http.get("filme/totalPagina").success(function(response) {
					$scope.totalPagina = response;
				}).error(function(response) {
					erro("Error: " + response);
				});
			//---------Fim total página----------
			
		}).error(function(response) {
			erro("Error: " + response);
		});
	};
	
	
	$scope.proximo = function () {
		if (new Number($scope.numeroPagina) < new Number($scope.totalPagina)) {
		 $scope.listarFilmes(new Number($scope.numeroPagina + 1));
		} 
	};
	
	$scope.anterior = function () {
		if (new Number($scope.numeroPagina) > 1) {
		  $scope.listarFilmes(new Number($scope.numeroPagina - 1));
		}
	}; */
	
	
	$scope.proximo = function () {
		if (new Number($scope.numeroPagina) < new Number($scope.totalPagina)) {
		 $scope.listarLivros(new Number($scope.numeroPagina + 1));
		} 
	};
	
	$scope.anterior = function () {
		if (new Number($scope.numeroPagina) > 1) {
		  $scope.listarLivros(new Number($scope.numeroPagina - 1));
		}
	};
	
});