<!DOCTYPE html>
<html>
<head>
	<title> Editar Produtos</title>
	
<jsp:include page= "css-files.jsp"/>


</head>


<body>


	<div class="container-fluid">
		<jsp:include page= "header.jsp"/>

 <div class="jumbotron jumbotron-fluid">
  <div class="container">
    <h1 class="display-4">Editar Produto</h1>
   </div>
</div>
 

	<form action="ProdutoControllerServlet" method="GET">
		<input type="hidden" name="command" value="UPDATE"/>
	
		<input type="hidden" name="idProduto" value="${PRODUTO_UPDATE.idProduto}"/>
	
		<div class="col-xs-4 divLabelProduto">	
					<label>Nome do Produto: </label>
					<input type="text" class="form-control" name="nomeProduto" value="${PRODUTO_UPDATE.nomeProduto}"/>
				
					<button type="submit" class="botaoSalvar btn btn-primary btn-lg">Salvar</button> 
					<button type="reset" class="botaoCancelar btn btn-secondary btn-lg" onclick="window.location.href='ProdutoControllerServlet'">Cancelar</button>
		</div>		
				
		
	</form>
	
	
	
			<jsp:include page= "footer.jsp"/>
	</div>
</body>
</html>