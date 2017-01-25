<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>

<head>

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
					<div class="form-group col-xs-3" id="selectFornecedor">
						<label for="fornecedorSelect">Fornecedor:</label> <select multiple
							class="form-control multiple idFornecedor" name="idFornecedor" id="idFornecedor">

							<option></option>

						</select>

					</div>

					<div class="form-group col-xs-2" id="selectProduto">
						<label for="fornecedorSelect">Produto:</label> <select
							class="form-control" name="idProduto" id="idProduto">


							<option></option>


						</select>


					</div>


					<div class="col-xs-2 divLabelDatePicker">
						<label class="fornLabel">Data Compra Inicial: </label> <input
							type="text" class="form-control datepicker" name="dataInicial" id="dataInicial">
					</div>

					<div class="col-xs-2 divLabelDatePicker">
						<label class="fornLabel">Data Compra Final: </label> <input
							type="text" class="form-control datepicker" name="dataFinal" id="dataFinal">
					</div>


					<div id="botaoFiltrarCompra" class="col-xs-2">

						<button type="submit" class="botaoFiltrar btn btn-secondary"
							role="button" name="command" value="FILTRAR" aria-pressed="true">Filtrar</button>
					</div>


				</div>

				<div class="row">

					<div id="tabelaEGrafico1" class="col-lg-6">
					
						<div id="tabelaTopFornecedores">
							<label> Top 5 Fornecedores: </label>
							<table class="table table-striped table-hover sortable ">
								<thead>
									<tr>
										<th>Nome Fornecedor</th>
										<th>Total em Compras</th>
									</tr>
								<thead>
								<tbody class="top5Table">
									<tr>
									</tr>

								</tbody>
							</table>
						</div>



					</div>
					<div id="graficoFornecedor" class="col-lg-6">
							<div id="percentualFornecedorChart" style="width: 700px; height: 300px;"></div>
							
					</div>
					
				</div>
				
				<div class="row">
					<div id="volumeMensalColumnChart" class="col-lg-6"></div>
					
					<div id="graficoProduto" class="col-lg-6">
						<div id="percentualProdutoChart" style="width: 700px; height: 300px;"></div>
					</div>
				</div>
				
				<div class="row">
					
		
				</div>
			</div>


			<jsp:include page="footer.jsp" />
			<jsp:include page="js-css-files.jsp" />
			
<script type="text/javascript" charset="utf-8">
	

var arrayDataGraphFornecedor = [];
var arrayDataGraphProduto = [];
var arrayDataGraphVolumeMensal = [];
var arrayDataTableTopFornecedores = [];

$(document).ready(function() {


	
	$.ajax({
		type : "GET",
		url : "DashboardControllerServlet?command=LIST_FORNECEDORES",
		 dataType: "json",
		success : function(data) {
			
			
			 $.each(data.fornecedores,function(i, fornecedor) {
		                 var select_fornecedor="<option value="+fornecedor.idFornecedor+">"+fornecedor.nomeFornecedor+"</option>";
		                 
		                 $(select_fornecedor).appendTo('#idFornecedor');
		              
		                }); 
			 $.each(data.produtos,function(i, produto) {
                 var select_produto="<option value="+produto.idProduto+">"+produto.nomeProduto+"</option>";
                 
                 $(select_produto).appendTo('#idProduto');
              
                }); 
		}
	});
	
		
		$.ajax({
			type : "GET",
			url : "DashboardControllerServlet?command=LIST_TOP_FORNECEDORES",
			success : function(data) {
				
				var trHTML = '';
			
				 $.each(data,function(i,fornecedor) {
					 trHTML += '<tr><td>' + fornecedor.nomeFornecedor + '</td><td>' + fornecedor.totalEmCompras + '</td></tr>';
			        });
				 
				//	$('.top5Table').html(trHTML);
	              
			}
				 
			});
		
	
				
		$('.botaoFiltrar').click(function () {
			var idFornecedor;
			if($('#idFornecedor').val() == ""){
				idFornecedor = null;
			}else{
				idFornecedor = $('#idFornecedor').val();
			}
			
			var idProduto;
			
			if($('#idProduto').val() === ""){
				idProduto = null;
			}else{
				idProduto = $('#idProduto').val();
			}
			
			var dataInicial;
			
			if($('#dataInicial').val() === ""){
				dataInicial = null;
			}else{
				dataInicial = $('#dataInicial').val();
				
			}
			
			var dataFinal;
			
			if($('#dataFinal').val() === ""){
				dataFinal = null;
			}else{
				dataFinal = $('#dataFinal').val();
				
			}
			
			var jsonFiltro = {
				"arrayIdFornecedoresSelecionados": idFornecedor,
				"idProduto": idProduto,
				"dataInicial": dataInicial,
				"dataFinal": dataFinal
			}
			
						
			$.ajax({
				type : "POST",
				url : "DashboardControllerServlet?command=FILTRAR",
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				data : JSON.stringify(jsonFiltro),
				success : function(data) {
					
					 arrayDataGraphFornecedor = [];
					 arrayDataGraphProduto = [];
					 arrayDataGraphVolumeMensal = [];
					 arrayDataTableTopFornecedores = [];
					 
					 $.each(data.listLinesGraphFornecedorDTO,function(i,listLinesGraphFornecedorDTO) {
		                  arrayDataGraphFornecedor [i] = [listLinesGraphFornecedorDTO.nomeFornecedor, listLinesGraphFornecedorDTO.sumQuantity];
		                 
		                 });
					 $.each(data.listLinesGraphProdutoDTO,function(i,listLinesGraphProdutoDTO) {
						 arrayDataGraphProduto [i] = [listLinesGraphProdutoDTO.nomeProduto, listLinesGraphProdutoDTO.sumQuantity];
		                 
		                 });
					 
					 $.each(data.listLinesGraphVolumeMensalProdutoDTO,function(i,listLinesGraphVolumeMensalProdutoDTO) {
						 arrayDataGraphVolumeMensal [i] = [listLinesGraphVolumeMensalProdutoDTO.mes, listLinesGraphVolumeMensalProdutoDTO.sumQuantity];
		                 
		                 });
					 
					 $.each(data.listLinesTopFornecedoresDTO,function(i,listLinesTopFornecedoresDTO) {
						 arrayDataTableTopFornecedores [i] = [listLinesTopFornecedoresDTO.nomeFornecedor, listLinesTopFornecedoresDTO.totalCompra];
		                 
		                 });
					
					
					 
					 percentualFornecedorChart();             
					 percentualProdutoChart();
					 volumeMensalColumnChart();
					 tabelaTopFornecedores();
				},
				
				error : function(data) {
					alert("filtro error");	
				}
				});
			
			
		});
	




google.charts.load('current', {'packages':['corechart', 'table']});
google.charts.setOnLoadCallback(percentualFornecedorChart);

function percentualFornecedorChart() {

  var data = new google.visualization.DataTable();
   		data.addColumn('string', 'Fornecedor');
   		  data.addColumn('number', 'Volume de Compras');
		data.addRows(arrayDataGraphFornecedor);

  var options = {
    title: 'Percentual de Volume de Compras por Fornecedor'
  };

  var chart = new google.visualization.PieChart(document.getElementById('percentualFornecedorChart'));

  chart.draw(data, options);
}




google.charts.setOnLoadCallback(percentualProdutoChart);

function percentualProdutoChart() {

	 var data = new google.visualization.DataTable();
		data.addColumn('string', 'Produto');
		  data.addColumn('number', 'Volume de Compras');
		data.addRows(arrayDataGraphProduto);



  var options = {
    title: 'Percentual de Volume de Compras por Produto'
  };

  var chart = new google.visualization.PieChart(document.getElementById('percentualProdutoChart'));

  chart.draw(data, options);
}





google.charts.setOnLoadCallback(volumeMensalColumnChart);

function volumeMensalColumnChart() {

	var data = new google.visualization.DataTable();
		data.addColumn('string', 'Mês');
		  data.addColumn('number', 'Volume de Compras');
	data.addRows(arrayDataGraphVolumeMensal);
	
	
      var options = {
        title: 'Volume de Compras',
        width: 630,
        height: 300,
        hAxis: {
          title: 'Mês',
          format: 'MM/YY',
          viewWindow: {
            min: [1, 12, 0],
            max: [1, 12, 0]
          }
        },
        vAxis: {
          title: 'Volume de itens'
        }
      };

      var chart = new google.visualization.ColumnChart(
        document.getElementById('volumeMensalColumnChart'));

      chart.draw(data, options);
    }	
});



google.charts.setOnLoadCallback(tabelaTopFornecedores);

function tabelaTopFornecedores() {
  var data = new google.visualization.DataTable();
  data.addColumn('string', 'Nome Fornecedor');
  data.addColumn('number', 'Volume de Compras');
  data.addRows(arrayDataTableTopFornecedores);

  var table = new google.visualization.Table(document.getElementById('tabelaTopFornecedores'));

  table.draw(data);
}
	
</script>

</body>



</html>








