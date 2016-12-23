<!DOCTYPE html>
<html>
<head>
	<title> Editar Produtos</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css"	>
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css"	>
</head>


<body>
	<div id="wrapper">
			<div id="header">
				<h2> Teste programador</h2>
			</div>
	</div>

	<div id="container">
		<h3> Editar Produto </h3>
	</div>	
	
	<form action="ProdutoControllerServlet" method="GET">
		<input type="hidden" name="command" value="UPDATE"/>
	
		<input type="hidden" name="produtoId" value="${PRODUTO_UPDATE.idProduto}"/>
	
		<table> 
			<tbody>
				<tr>
					<td><label>Nome do Produto: </label></td>
					<td><input type="text" name="nomeProduto" value="${PRODUTO_UPDATE.nomeProduto}"/></td>
				</tr>
				<tr>
					<td><label></label></td>
					<td><input type="submit" value="Salvar" class="save" /></td>
				</tr>
				
			</tbody>
				
		</table>
	</form>
	
	<div style="clear: both;"></div>
	
	<p>
		<a href="ProdutoControllerServlet">Voltar para lista de produtos</a>
	</p>
		
</body>
</html>