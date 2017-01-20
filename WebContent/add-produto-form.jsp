<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Adicionar Produtos</title>

<jsp:include page="js-css-files.jsp"  />



</head>

<body>

	<div class="container-fluid">
		<jsp:include page="header.jsp" />

		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Adicionar Produto</h1>
			</div>
		</div>
		
		<div id="alerta"></div>

		<form id="myForm" name="myForm" action="ProdutoControllerServlet" method="POST">
			<input type="hidden" name="command" value="ADD" />


			<div class="col-xs-4 divLabelProduto">
				<label>Nome do Produto: </label> <input id="nomeProduto" class="form-control"
					type="text" name="nomeProduto" placeholder="produto" />


				<button id="botaoSalvar" type="" class="botaoSalvar btn btn-primary btn-lg">Salvar</button>
				<button type="reset" class="botaoCancelar btn btn-secondary btn-lg"
					onclick="window.location.href='ProdutoControllerServlet'">Cancelar</button>
			</div>

		</form>


		<jsp:include page="footer.jsp" />

	</div>
	
	<script type="text/javascript">

	$("#botaoSalvar").click(function(event){
		event.preventDefault();
		
		
				
		var error = "";
	
		if ($('#nomeProduto').val() == "") {
			error += "Nome do produto em branco. <br>";
			var alertaErro = '<div class="alert alert-danger" role="alert">  <strong>Erro! </strong><br>'+error+'</div>';
			$('#alerta').html(alertaErro);
			
		}else{
		
			
			var jsonAddProduto = {nomeProduto : $('#nomeProduto').val()};
			
			
			$.ajax({
				type : "POST",
				url : "ProdutoControllerServlet?command=ADD",
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				data : JSON.stringify(jsonAddProduto),
				success : function(data1) {
					console.log(data1);
					var alertaSucesso = '<div class="alert alert-success" role="alert">  <strong>Sucesso! </strong>'+data1.msg+'.</div>';
						$('#alerta').html(alertaSucesso);
				},
				
				error : function(data) {
				
					var alertaErro = '<div class="alert alert-danger" role="alert">  <strong>Erro! </strong>'+data.responseJSON.msg+'</div>';
					$('#alerta').html(alertaErro);
				}
				});
			
						
		}
	});
	

  </script>

</body>
</html>


