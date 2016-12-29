<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>

<head>

<title>Cadastro de Fornecedores</title>


<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>
	<a href="ProdutoControllerServlet"> Produtos</a>
	<a href="FornecedorControllerServlet"> Fornecedores</a>
	<div id="wrapper">
		<div id="header">
			<h2>Lista de Fornecedores</h2>
		</div>
	</div>

	<div id="container">

		<div id="content">


		
		<form action="FornecedorControllerServlet" method="GET">
			<input type="hidden" name="command" value="Adicionar Fornecedor"/>
			
			<input type="submit" value="Adicionar Fornecedor"
				onclick="window.location.href='add-fornecedor-form.jsp'"
				class="add-student-button" />
		</form>
			
			
			

			<table>

				<tr>
					<th>ID Fornecedor</th>
					<th>Nome Fornecedor</th>
					<th>Data de Contrato</th>
					<th>Produtos</th>
					<th>Action</th>


				</tr>

				<c:forEach var="tempFornecedor" items="${FORNECEDORES_LIST}">

					<c:url var="tempLink" value="FornecedorControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="idFornecedor"
							value="${tempFornecedor.idFornecedor}" />
					</c:url>

					<c:url var="deleteLink" value="FornecedorControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="idFornecedor"
							value="${tempFornecedor.idFornecedor}" />
					</c:url>

					<tr>
						<td>${tempFornecedor.idFornecedor}</td>
						<td>${tempFornecedor.nomeFornecedor}</td>
						<td><fmt:formatDate value="${tempFornecedor.dataContrato}" pattern="dd/MM/yyyy"/></td>
						
						<td>
							<c:forEach var="tempProduto" items="${tempFornecedor.listagemProdutos}">
									${tempProduto.nomeProduto},
							</c:forEach>	
						</td>	
			
				<!--  		<td>${tempFornecedor.listagemProdutos}</td>-->
						
						
						
						
						<td><a href="${tempLink}">Update</a> | <a
							href="${deleteLink}"
							onclick="if (!(confirm('Tem certeza que quer deletar esse produto')) return true">Delete</a></td>

						<td></td>
					</tr>

				</c:forEach>

			</table>

		</div>

	</div>
</body>


</html>








