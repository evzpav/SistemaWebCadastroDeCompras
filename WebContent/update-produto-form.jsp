<!DOCTYPE html>
<html>
<head>
	<title> Editar Produtos</title>
	
<jsp:include page= "js-css-files.jsp" />


</head>


<body>


	<div class="container-fluid">
		<jsp:include page= "header.jsp"/>

 <div class="jumbotron jumbotron-fluid">
  <div class="container">
    <h1 class="display-4">Editar Produto</h1>
   </div>
</div>
 
 <div id="alerta"></div>

	<form action="" method="GET">
		<input type="hidden" name="command" value="UPDATE"/>
	
	<input type="hidden" id="idProduto" name="idProduto" value="${PRODUTO_UPDATE.idProduto}"/>
	
		<div class="col-xs-4 divLabelProduto">	
					<label>Nome do Produto: </label>
					<input type="text" class="form-control" id="nomeProduto" name="nomeProduto" value="${PRODUTO_UPDATE.nomeProduto}"/>
				
					<button type="submit" class="botaoSalvar btn btn-primary btn-lg">Salvar</button> 
					<button type="reset" class="botaoCancelar btn btn-secondary btn-lg" onclick="window.location.href='ProdutoControllerServlet'">Cancelar</button>
		</div>		
				
		
	</form>
	
	
	
			<jsp:include page= "footer.jsp"/>
	</div>
	
	
	<script type="text/javascript">

	$(".botaoSalvar").click(function(event){
		event.preventDefault();
		
		var error = "";
	
		if ($('#nomeProduto').val() == "") {
			error += "Nome do produto em branco. <br>";
			var alertaErro = '<div class="alert alert-danger" role="alert">  <strong>Erro! </strong><br>'+error+'</div>';
			$('#alerta').html(alertaErro);
			
		}else{
		
			
			var jsonUpdateProduto = {
					nomeProduto : $('#nomeProduto').val(),
					idProduto : $('#idProduto').val()
			};
			
			}
			
			
			$.ajax({
				type : "POST",
				url : "ProdutoControllerServlet?command=UPDATE",
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				data : JSON.stringify(jsonUpdateProduto),
				success : function(data1) {
					console.log(data1);
					var alertaSucesso = '<div class="alert alert-success" role="alert">  <strong>Sucesso! </strong>'+data1.msg+'.</div>';
						$('#alerta').html(alertaSucesso);
						
						window.open ('ProdutoControllerServlet','_self',false)
				},
					
				error : function(data) {
				
					var alertaErro = '<div class="alert alert-danger" role="alert">  <strong>Erro! </strong>'+data.responseJSON.msg+'</div>';
					$('#alerta').html(alertaErro);
				}
				});
			
		
	});
	

  </script>
	
</body>
</html>