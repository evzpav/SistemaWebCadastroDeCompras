<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>

<head>

<title>Adicionar Compra</title>

<jsp:include page="js-css-files.jsp" />

</head>

<body>
	<div class="container-fluid">
		<jsp:include page="header.jsp" />


		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Registro de Compra</h1>
			</div>

		</div>

		<div id="alerta"></div>

		<div id="container">

			<div id="content">

				<form action="">

					<div class="row">
						<div class="form-group col-xs-3" id="selectFornecedor">
							<label for="fornecedorSelect">Fornecedor:</label> <select
								class="form-control" name="idFornecedor" id="idFornecedor">

								<c:forEach var="tempFornecedor" items="${FORNECEDORES_LIST}">

									<option id="idFornecedor"
										value="${tempFornecedor.idFornecedor}"><a href="">${tempFornecedor.nomeFornecedor}</a></option>
								</c:forEach>

							</select>

						</div>

						<div class="col-xs-3 divLabelDatePicker">
							<label class="fornLabel">Data da Compra: </label> <input
								type="text" class="form-control datepicker" name="dataCompra"
								id="dataCompra">
						</div>

						
					</div>
					<div class="row">


						<div class="form-group col-xs-3" id="selectProduto">
							<label for="fornecedorSelect">Produto:</label> <select
								class="form-control" name="idProduto" id="idProduto">


								<option></option>


							</select>


						</div>

						<div class="col-xs-3 divLabelQuantidade">
							<label class="fornLabel ">Quantidade: </label> <input type="text"
								class="form-control" name="quantidade" id="quantidade"
								placeholder="100" />
						</div>

						<div class="col-xs-3 divLabelValor">
							<label class="fornLabel ">Valor Unitario: </label> <input
								type="text" class="form-control" name="valorUnitario"
								id="valorUnitario" placeholder="0.00" />
						</div>

						<div id="botaoAdicionarItem" class="col-xs-3 ">
							<button class="btn btn-secondary active" role="button"
								aria-pressed="true">Adicionar Item</button>
						</div>
					</div>

					<table class="table table-striped">
						<thead>
							<tr>

								<th>Nome Produto</th>
								<th>Quantidade</th>
								<th>Valor Unitario</th>
								<th>Valor Total Item</th>
								<th>Action</th>

							</tr>
						<thead>
						<tbody id="table_div">

						</tbody>
					</table>

					<div>
						<label id="valorTotalLabel"
							class="fornLabel col-xs-2 divLabelProduto">Valor Total da
							Compra:</label> <label id="valorTotal"
							class="fornLabel col-xs-2 divLabelProduto"></label>
					</div>

					<div class="col-xs-4 divLabelProduto">
						<button type="submit" class="botaoSalvar btn btn-primary btn-lg">Salvar
							Compra</button>
						<button type="reset"
							class="botaoCancelar btn btn-secondary btn-lg"
							onclick="window.location.href='CompraControllerServlet'">Cancelar</button>
					</div>
			</div>
			</form>

		</div>

		<jsp:include page="footer.jsp" />
	</div>
</body>


<script type="text/javascript">
	var arrayDeObjetos = [];
	var cont = 0;
	$idFornecedorSelecionado = $('#idFornecedor');

	$idFornecedorSelecionado.change(function() {

		$.ajax({
			type : "GET",
			url : "GetProdutosServlet?idFornecedorSelecionado="
					+ $('#idFornecedor').val(),
			success : function(data) {
				$('#idProduto').html(data);
			}
		});
	});

	$adicionarItem = $('#botaoAdicionarItem');

	$adicionarItem.click(function(event) {

		event.preventDefault();
		
		var idProduto = $("#idProduto option:selected").val();
		var nomeProduto = $("#idProduto option:selected").text();
		var quantidade = $("#quantidade").val();
		var valorUnitario = $("#valorUnitario").val();
		var valorTotalItem = valorUnitario * quantidade;

		var error = "";
		
		
		if($.isNumeric(quantidade) == false){
			error += "Quantidade invalida. <br>";
		}
		
		if($.isNumeric(valorUnitario) == false){
			error += "Valor Unitario invalido. <br>";
		}
			
		
		
		 if (error != "") {
             var alertaErroItem = '<div class="alert alert-danger" role="alert"><p><strong>Erro(s) ao adicionar item:</strong></p>' + error + '</div>';
             $("#alerta").html(alertaErroItem);
		 }else{
			  $("#alerta").html("");
			  
			var objeto = {
				"id" : cont++,
				"idProduto" : idProduto,
				"nomeProduto" : nomeProduto,
				"quantidade" : quantidade,
				"valorUnitario" : valorUnitario,
				"valorTotalItem" : valorTotalItem
			}

		 
		arrayDeObjetos.push(objeto);
		refreshTable();
	}
	});

	
	function clearFields(){
		$("#quantidade").val('');
		$("#valorUnitario").val('');
		$("#valorTotal").val('');
		$("#dataCompra").val('');
		arrayDeObjetos.length = 0;
				
	}
			
	var refreshTable = function() {

		$("#table_div").html('');

		var valorTotalCompra = 0;

		for (var i = 0; i < arrayDeObjetos.length; i++) {

			var objeto = arrayDeObjetos[i];

			$("#table_div").append("<tr>");
			$("#table_div").append(
					"<td class='nomeProd'>" + objeto.nomeProduto + "</td>");
			$("#table_div").append(
					"<td class='quant'>" + objeto.quantidade + "</td>");
			$("#table_div").append(
					"<td class='valorUnit'>"
							+ accounting.formatMoney(objeto.valorUnitario,
									"R$", 2, ".", ",") + "</td>");
			$("#table_div").append(
					"<td class='valorTotalItem'>"
							+ accounting.formatMoney(objeto.valorTotalItem,
									"R$", 2, ".", ",") + "</td>");
			$("#table_div")
					.append(
							'<td> <input type="button" atributo-deleta="'+objeto.id+'"  id="botaoDeleteItem" class="btn-secondary " value="Delete" /> </td>');
			$("#table_div").append("</tr>");

			valorTotalCompra += objeto.valorTotalItem;
		}

		$("#valorTotal").html(
				accounting.formatMoney(valorTotalCompra, "R$", 2, ".", ","));

	}

	$(document).on('click', '#botaoDeleteItem', function() {
		var atributoParaDeletar = $(this).attr('atributo-deleta');

		for (var i = 0; i < arrayDeObjetos.length; i++) {

			var objeto = arrayDeObjetos[i];

			if (objeto.id == atributoParaDeletar) {

				arrayDeObjetos.splice(i, 1);
				refreshTable();
			}

		}

	});

	$idFornecedorSelecionado.trigger('change');

	$('.botaoSalvar')
			.click(
					function(event) {
						event.preventDefault();
							
						var error = "";
						
						if($('#dataCompra').val() == "" ){
							error += "Data em branco. <br>";
						}
						
						if(arrayDeObjetos.length == 0){
							error += "Nenhum item adicionado a compra. <br>";
						}
						
						if (error != "") {
					         var alertaErroItem = '<div class="alert alert-danger" role="alert"><p><strong>Erro(s) ao salvar compra:</strong></p>' + error + '</div>';
					         $("#alerta").html(alertaErroItem);
					         					         
						} else{
						
						
						var jsonDaTabela = {

							itens : arrayDeObjetos,
							dataCompra : $('#dataCompra').val(),
							idFornecedor : $('#idFornecedor').val()
						}
					
						
						$.ajax({
									type : "POST",
									url : "CompraControllerServlet?command=ADD",
									dataType : 'json',
									contentType : 'application/json; charset=utf-8',
									data : JSON.stringify(jsonDaTabela),
									success : function(data1) {
										
									var alertaSucesso = '<div class="alert alert-success" role="alert">  <strong>Sucesso! </strong>'+data1.msg+'.</div>';
										$('#alerta').html(alertaSucesso);
										clearFields();
										refreshTable();
									},
									
									error : function(data) {
										
									var alertaErro = '<div class="alert alert-warning" role="alert">  <strong>Warning! </strong>'+data.responseJSON.msg+'.</div>';
									$('#alerta').html(alertaErro);
									}
									});

						}
						});
	
		
	

</script>

</html>








