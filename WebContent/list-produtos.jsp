<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>

<jsp:include page= "css-files.jsp"/>

<title>Lista de Produtos</title>



</head>

<body>

	<div class="container-fluid">

	<jsp:include page= "header.jsp"/>


 
 <div class="jumbotron jumbotron-fluid">
  <div class="container">
    <h1 class="display-4">Lista de Produtos</h1>
   </div>
</div>
 
		<div id="container">

			<div id="content">



				<a href="/teste-programador1-web/add-produto-form.jsp" class="botaoAdicionar btn btn-primary btn-lg active" role="button" aria-pressed="true">Adicionar Produto</a>


				<table class="table table-striped">
					<thead>
						<tr>
					
							<th>ID Produto</th>
							<th>Nome do Produto</th>
							<th>Action</th>
						
						</tr>	
					</thead>
					<tbody>
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
							<td>${tempProduto.idProduto}</td>
							<td>${tempProduto.nomeProduto}</td>
							<td><a href="${tempLink}">Update</a> | <a href="${deleteLink}">Delete</a></td>

							<td></td>
						</tr>

					</c:forEach>
					</tbody>
				</table>

			</div>

		
	
	<jsp:include page= "footer.jsp"/>
	</div>
</body>


</html>








