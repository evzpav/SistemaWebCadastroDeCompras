<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>

<head>
<jsp:include page= "js-css-files-bootstrap3.jsp" />
<title>Dashboard</title>




</head>

<body>
	<div class="container-fluid">
		<jsp:include page="header.jsp" />


		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Dashboard</h1>
			</div>

		</div>


		<div id="container">

			<div id="content">





				<div class="row">
					<div class="col-lg-3 col-xs-12" id="selectFornecedor">
						<label for="fornecedorSelect">Fornecedor:</label> <select multiple
							class="form-control multiple idFornecedor" name="idFornecedor" id="idFornecedor" size=2 style="height=50%">

							<option></option>

						</select>

					</div>

					<div class="col-lg-3 col-xs-12" id="selectProduto">
						<label for="fornecedorSelect">Produto:</label> <select
							class="form-control" name="idProduto" id="idProduto">


							<option></option>


						</select>


					</div>


					<div class="col-lg-2 col-xs-12 divLabelDatePicker">
						<label class="fornLabel">Data Compra Inicial: </label> <input
							type="text" class="form-control datepicker" name="dataInicial" id="dataInicial">
					</div>

					<div class="col-lg-2 col-xs-12 divLabelDatePicker">
						<label class="fornLabel">Data Compra Final: </label> <input
							type="text" class="form-control datepicker" name="dataFinal" id="dataFinal">
					</div>


					<div id="botaoFiltrarCompra" class="col-lg-2 col-xs-12">

						<button type="submit" class="botaoFiltrar btn btn-secondary"
							role="button" name="command" value="FILTRAR" aria-pressed="true">Filtrar</button>
					</div>


				</div>

				<div class="row">

					<div id="tabelaEGrafico1" class="col-lg-6 col-xs-12">
						<label>Top 5 Fornecedores:</label>
						<div id="tabelaTopFornecedores"></div>



					</div>
					<div id="graficoFornecedor" class="col-lg-6 col-xs-12">
							<div id="percentualFornecedorChart" ></div>
							
					</div>
					
				</div>
				
				<div class="row">
					<div id="volumeMensalColumnChart" class="col-lg-6 col-xs-12"></div>
					
					<div id="graficoProduto" class="col-lg-6 col-xs-12">
						<div id="percentualProdutoChart" ></div>
					</div>
				</div>
				
				<div class="row">
					
		
				</div>
			</div>


			<jsp:include page="footer.jsp" />
			
			
		
			<script src="dashboard.js"></script>

</body>



</html>








