
	

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
					 
					 clearAllCharts();
										 
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
    title: 'Percentual de Volume de Compras por Fornecedor',
    	  width: 	'100%',
          height: 300
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
    title: 'Percentual de Volume de Compras por Produto',
    	  width: 	'100%',
          height: 300
  };

  var chart = new google.visualization.PieChart(document.getElementById('percentualProdutoChart'));

  chart.draw(data, options);
}





google.charts.setOnLoadCallback(volumeMensalColumnChart);

function volumeMensalColumnChart() {

	var data = new google.visualization.DataTable();
		data.addColumn('string', 'Mes');
		  data.addColumn('number', 'Volume de Compras');
	data.addRows(arrayDataGraphVolumeMensal);
	
	
      var options = {
        title: 'Volume de Compras',
        width: 	'100%',
        height: 300,
        hAxis: {
          title: 'Mes',
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

var tabela;

function tabelaTopFornecedores() {
  var data = new google.visualization.DataTable();
  data.addColumn('string', 'Nome Fornecedor');
  data.addColumn('number', 'Volume de Compras');
  data.addRows(arrayDataTableTopFornecedores);
  
  
  var options = {
		    showRowNumber: true, 
		    width: '100%', 
		    height: 300
		  };
  
  
  var formatter = new google.visualization.NumberFormat({groupingSymbol:'.', fractionDigits	
: '0'});
  formatter.format(data, 1);
  var table = new google.visualization.Table(document.getElementById('tabelaTopFornecedores'));
  
  tabela = table;
  table.draw(data, options);
}

function clearAllCharts(){

tabela.clearChart();


}