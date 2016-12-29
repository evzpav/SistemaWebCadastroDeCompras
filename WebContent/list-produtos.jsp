<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	
	<title>Lista de Produtos</title>
	

<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>
	<a href="ProdutoControllerServlet"> Produtos</a>
	<a href="FornecedorControllerServlet"> Fornecedores</a>
	<div id="wrapper">
		<div id="header">
			<h2>Lista de Produtos</h2>
		</div>
	</div>

	<div id="container">
	
		<div id="content">
		
					
			<input type="button" value="Adicionar Produto"
				onclick="window.location.href='add-produto-form.jsp'"
				class="add-student-button"/>
			
			<table>
			
				<tr>
					<th>ID Produto</th>
					<th>Nome Produto</th>
					<th>Action</th>
					
				
				</tr>
				
				<c:forEach var="tempProduto" items="${PRODUTOS_LIST}">
					
					<c:url var="tempLink" value="ProdutoControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="idProduto" value="${tempProduto.idProduto}" />
					</c:url>
					
					<c:url var="deleteLink" value="ProdutoControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="idProduto" value="${tempProduto.idProduto}" />
					</c:url>
									
					<tr>
						<td> ${tempProduto.idProduto} </td>
						<td> ${tempProduto.nomeProduto} </td>
							<td> <a href="${tempLink}">Update</a> 
							| 
							<a href="${deleteLink}" onclick="if (!(confirm('Tem certeza que quer deletar esse produto')) return true">Delete</a></td>
				
							<td> 
						
						</td>
					</tr>
				
				</c:forEach>
				
			</table>
		
		</div>
	
	</div>
</body>


</html>








