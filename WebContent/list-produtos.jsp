<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>

<jsp:include page= "js-css-files.jsp" />

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

 <div id="alerta"> </div>
 
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

					

						<tr>
							<td >${tempProduto.idProduto}</td>
							<td>${tempProduto.nomeProduto}</td>
							<td><a href="${tempLink}">Update</a> | <a href="#" class="linkDelete" data-idProduto="${tempProduto.idProduto}">Delete</a></td>

							<td></td>
						</tr>

					</c:forEach>
					</tbody>
				</table>

			</div>

		
	
	<jsp:include page= "footer.jsp"/>
	</div>
	</div>
	
	<script type="text/javascript">
				
		$(".linkDelete").click(function(event){
			event.preventDefault();
		
			
			
			var jsonDeleteProduto = {
					idProduto : $(this).attr("data-idProduto")
			};
			
		
				
			$.ajax({
					type : "POST",
					url : "ProdutoControllerServlet?command=DELETE",
					dataType : 'json',
					contentType : 'application/json; charset=utf-8',
					data : JSON.stringify(jsonDeleteProduto),
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








