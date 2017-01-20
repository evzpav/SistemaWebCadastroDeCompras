<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
<!DOCTYPE html>
<html>

<head>

<title>Cadastro de Fornecedores</title>

<jsp:include page= "js-css-files.jsp" />

</head>

<body>
	<div class="container-fluid">
			<jsp:include page= "header.jsp"/>
	

		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Lista de Fornecedores</h1>
			</div>

		</div>
		
	<div id="alerta"></div>

		<div id="container">

			<div id="content">



				<form action="" method="GET">
					
					
					<button href="/teste-programador1-web/add-fornecedor-form.jsp" type="submit"
						class="botaoAdicionar btn btn-primary btn-lg active" role="button" name="command" value="IR_PARA_ADICIONAR_FORNECEDOR"
						aria-pressed="true">Adicionar Fornecedor</button>
				

				</form>




				<table class="table table-striped sortable">
					<thead>
						<tr>
							<th data-defaultsort="disabled">ID Fornecedor</th>
							<th>Nome Fornecedor</th>
							<th>Data de Contrato</th>
							<th data-defaultsort="disabled">Produtos</th>
							<th data-defaultsort="disabled">Action</th>

						</tr>
					<thead>
					<tbody>
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
								<td><fmt:formatDate value="${tempFornecedor.dataContrato}"
										pattern="dd/MM/yyyy" /></td>

								<td><c:forEach var="tempProduto" items="${tempFornecedor.listagemProdutos}" varStatus= "indice">
														${tempProduto.nomeProduto}
														<c:if test="${fn:length(tempFornecedor.listagemProdutos) - 1 != indice.index}">, </c:if>
																								
									</c:forEach></td>


								<td><a href="${tempLink}">Update</a> |
								 <a	href="#" class="linkDelete" data-idFornecedor="${tempFornecedor.idFornecedor}">Delete</a></td>

								<td></td>
							</tr>

						</c:forEach>
					</tbody>
				</table>

			</div>

		</div>
	

		<jsp:include page= "footer.jsp"/>
	</div>
	
	
	<script type="text/javascript">
				
		$(".linkDelete").click(function(event){
			event.preventDefault();
		
			
			
			var jsonDeleteFornecedor = {
					idFornecedor : $(this).attr("data-idFornecedor")
			};
			
			console.log(jsonDeleteFornecedor);
				
			$.ajax({
					type : "POST",
					url : "FornecedorControllerServlet?command=DELETE",
					dataType : 'json',
					contentType : 'application/json; charset=utf-8',
					data : JSON.stringify(jsonDeleteFornecedor),
					success : function(data1) {
						
						console.log("deu sucesso");
						var alertaSucesso = '<div class="alert alert-success" role="alert">  <strong>Sucesso! </strong>'+data1.msg+'</div>';
							$('#alerta').html(alertaSucesso);
							window.location.reload();
					},
					
					error : function(data) {
						
						console.log("deu erro");
						var alertaErro = '<div class="alert alert-danger" role="alert">  <strong>Erro! </strong>'+data.responseJSON.msg+'</div>';
							$('#alerta').html(alertaErro);
					}
			});
		
		
		});
		
	
	
	</script>
</body>


</html>








