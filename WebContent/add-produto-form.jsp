<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Adicionar Produtos</title>

<jsp:include page= "css-files.jsp"/>



</head>

<body>

	<div class="container-fluid">
		<jsp:include page= "header.jsp"/>
		
	 <div class="jumbotron jumbotron-fluid">
  <div class="container">
    <h1 class="display-4">Adicionar Produto</h1>
   </div>
</div>

	<form name="myForm" action="ProdutoControllerServlet" method="POST">
		<input type="hidden" name="command" value="ADD" />

		
					<div class="col-xs-4 divLabelProduto">	
						<label>Nome do Produto: </label>
						<input class="form-control" type="text" name="nomeProduto" placeholder="produto"/>
										
					
						<button type="submit" class="botaoSalvar btn btn-primary btn-lg">Salvar</button> 
						<button type="reset" class="botaoCancelar btn btn-secondary btn-lg" onclick="window.location.href='ProdutoControllerServlet'">Cancelar</button>
					</div>
			
	</form>

	
		<jsp:include page= "footer.jsp"/>
	
	</div>
</body>
</html>