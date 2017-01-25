<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 
<!DOCTYPE html>
<html>

<head>

<title>Cadastro de Compras</title>




</head>

<body>
	<div class="container-fluid">
			<jsp:include page= "header.jsp"/>
	

		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Lista de Compras</h1>
			</div>

		</div>


		<div id="container">

			<div id="content">



				<form action="CompraControllerServlet" method="GET">
					
					
					<button href="/teste-programador1-web/add-compra-form.jsp" type="submit"
						class="botaoAdicionar btn btn-primary btn-lg active" role="button" name="command" value="IR_PARA_ADICIONAR_COMPRA"
						aria-pressed="true">Adicionar Compra</button>
				

				</form >
				
			<form action="CompraControllerServlet" method="GET">
				<input type="hidden" name="command" value="FILTRAR" />
				
				<div class="row">
					<div class="form-group col-xs-3" id="selectFornecedor">
					  <label for="fornecedorSelect">Fornecedor:</label>
						 <select class="form-control" name="idFornecedor" id="idFornecedor" >
						  	 	
						  		<c:forEach var="fornecedor" items="${FORNECEDORES_LIST}">
						  			
						  			<option id="idFornecedor" value="${fornecedor.idFornecedor}" >	
						  					<a href="" >${fornecedor.nomeFornecedor}</a>
						   	 		</option>
						   		</c:forEach>
							 
						</select>
						
					</div>
					
					<div class="col-xs-3 divLabelDatePicker">		
						<label class="fornLabel">Data Inicial: </label>
						<input type="text" class="form-control datepicker" name="dataInicial">
					</div>				
					
					<div class="col-xs-3 divLabelDatePicker">		
						<label class="fornLabel">Data Final: </label>
						<input type="text" class="form-control datepicker" name="dataFinal" >
					</div>	
					
					
						<div id="botaoFiltrarCompra" class="col-xs-3">
						
							<button type="submit" class="botaoFiltrar btn btn-secondary" role="button" name="command" value="FILTRAR" aria-pressed="true">Filtrar</button>
						</div>
					
					
				</div>
			</form>

				<table class="table table-striped table-hover sortable">
					<thead>
						<tr>
							<th data-defaultsort="disabled">ID Compra</th>
							<th>Nome Fornecedor</th>
							<th>Data da Compra</th>
							<th>Total da Venda</th>
							<th></th>
							<th data-defaultsort="disabled">Produtos</th>
							<th data-defaultsort="disabled">Action</th>

						</tr>
					<thead>
					<tbody>
						<c:forEach var="tempCompra" items="${COMPRAS_LIST}">

							<c:url var="tempLink" value="CompraControllerServlet">
								<c:param name="command" value="LOAD" />
								<c:param name="idCompra" value="${tempCompra.idCompra}" />
							</c:url>

							<c:url var="deleteLink" value="CompraControllerServlet">
								<c:param name="command" value="DELETE" />
								<c:param name="idCompra" value="${tempCompra.idCompra}" />
							
							</c:url>
							
							<c:url var="fornecedorLink" value="CompraControllerServlet">
								<c:param name="idFornecedor" value="${tempCompra.fornecedor.idFornecedor}" />
							</c:url>
							
							<tr>
								<td>${tempCompra.idCompra}</td>
								<td>${tempCompra.fornecedor.nomeFornecedor}</td>
								<td><fmt:formatDate value="${tempCompra.dataCompra}"
										pattern="dd/MM/yyyy" /></td>

								<td><fmt:setLocale value="pt_BR"/><fmt:formatNumber value="${tempCompra.valorTotal}" type="currency"/><td>
								
		
								<td>	
									<c:forEach var="tempItemDeCompra" items="${tempCompra.listaDeItemDeCompra}" varStatus="indice">
										${tempItemDeCompra.produto.nomeProduto}
										<c:if test="${fn:length(tempCompra.listaDeItemDeCompra) - 1 != indice.index}">, </c:if>
									</c:forEach>
								</td>
								
								<td><a href="${tempLink}">Update</a> |
								 <a	href="${deleteLink}">Delete</a></td>

								
							</tr>

						</c:forEach>
					</tbody>
				</table>

			</div>

		</div>
	

		<jsp:include page= "footer.jsp"/>
		
	</div>
	<jsp:include page= "js-css-files.jsp" />
</body>


</html>








