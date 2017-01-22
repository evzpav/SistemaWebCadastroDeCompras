<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 
<!DOCTYPE html>
<html>

<head>

<title>Dashboard</title>

<jsp:include page= "js-css-files.jsp" />


</head>

<body>
	<div class="container-fluid">
			<jsp:include page= "header.jsp"/>
	

		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Dashboard</h1>
			</div>

		</div>


		<div id="container">

			<div id="content">



							
					
				<div class="row">
					<div class="form-group col-xs-3" id="selectFornecedor">
					  <label for="fornecedorSelect">Fornecedor:</label>
						 <select multiple class="form-control" name="idFornecedor" id="idFornecedor" >
						  	 	
						  		<c:forEach var="fornecedor" items="${FORNECEDORES_LIST}">
						  			
						  			<option id="idFornecedor" value="${fornecedor.idFornecedor}" >	
						  					<a href="">${fornecedor.nomeFornecedor}</a>
						   	 		</option>
						   		</c:forEach>
							 
						</select>
						
					</div>
					
					<div class="form-group col-xs-2" id="selectProduto">
							<label for="fornecedorSelect">Produto:</label> <select
								class="form-control" name="idProduto" id="idProduto">


								<option></option>


							</select>


					</div>
					
					
					<div class="col-xs-2 divLabelDatePicker">		
						<label class="fornLabel">Data Compra Inicial: </label>
						<input type="text" class="form-control datepicker" name="dataInicial">
					</div>				
					
					<div class="col-xs-2 divLabelDatePicker">		
						<label class="fornLabel">Data Compra Final: </label>
						<input type="text" class="form-control datepicker" name="dataFinal" >
					</div>	
					
					
						<div id="botaoFiltrarCompra" class="col-xs-2">
						
							<button type="submit" class="botaoFiltrar btn btn-secondary" role="button" name="command" value="FILTRAR" aria-pressed="true">Filtrar</button>
						</div>
			
					
				</div>
				
					<div class="row">
						
						<div id="tabelaEGrafico1" class="col-lg-6">
							<div id="tabelaApenas" >
									<label> Top 5 Fornecedores: </label>
									<table class="table table-striped table-hover sortable ">
										<thead>
											<tr>
												<th>Nome Fornecedor</th>
												<th>Total em Compras</th>
											</tr>
										<thead>
										<tbody>
												<tr>											
												</tr>
					
										</tbody>
									</table>
							</div>
							
							
							
						</div>
						<div id="apenasGrafico" class="col-lg-6">
						</div>
					</div>
		
		
	</div>
		
	

		<jsp:include page= "footer.jsp"/>
	
</body>

<script type="text/javascript">
     
    </script>

</html>








