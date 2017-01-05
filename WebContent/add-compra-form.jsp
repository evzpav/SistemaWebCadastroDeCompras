<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>

<head>

<title>Adicionar Compra</title>

<jsp:include page="css-files.jsp" />

</head>

<body>
	<div class="container-fluid">
		<jsp:include page="header.jsp" />


		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Registro de Compra</h1>
			</div>

		</div>


		<div id="container">

			<div id="content">





				<form action="CompraControllerServlet" method="GET">
					<input type="hidden" name="command" value="ADD_COMPRA" />

					<div class="row">
						<div class="form-group col-xs-3" id="selectFornecedor">
							<label for="fornecedorSelect">Fornecedor:</label> <select
								class="form-control" name="idFornecedor" id="idFornecedor">

								<c:forEach var="tempFornecedor" items="${FORNECEDORES_LIST}">

									<option value="${tempFornecedor.idFornecedor}"><a href="">${tempFornecedor.nomeFornecedor}</a>
									</option>
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
								class="form-control" name="quantidade" id="quantidade" />
						</div>

						<div class="col-xs-3 divLabelValor">
							<label class="fornLabel ">Valor Unitário: </label> <input
								type="text" class="form-control" name="valorUnitario"
								id="valorUnitario" />
						</div>


						<button class="botaoAdicionar btn btn-secondary active"
							role="button" id="botaoAdicionarItem" aria-pressed="true">Adicionar
							Item</button>

					</div>




					<table class="table table-striped">
						<thead>
							<tr>

								<th>Nome Produto</th>
								<th>Quantidade</th>
								<th>Valor Unitário</th>
								<th></th>
								<th>Action</th>

							</tr>
						<thead>
						<tbody id="table_div">





						</tbody>
					</table>
					
					<div>
						<label id="valorTotalLabel" class="fornLabel col-xs-2 divLabelProduto">Valor Total da Compra:</label>
						<label id="valorTotal" class="fornLabel col-xs-2 divLabelProduto">0</label>
					</div>
					
					<div class="col-xs-4 divLabelProduto">
						<button type="submit" class="botaoSalvar btn btn-primary btn-lg">Salvar
							Compra</button>
						<button type="reset"
							class="botaoCancelar btn btn-secondary btn-lg"
							onclick="window.location.href='CompraControllerServlet'">Cancelar</button>
					</div>

				</form>
			</div>

		</div>


		<jsp:include page="footer.jsp" />
	</div>
</body>


<script type="text/javascript">
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
		var nomeProduto = $("#idProduto option:selected").text();
		var quantidade = $("#quantidade").val();
		var valorUnitario = $("#valorUnitario").val();
		var data1 = [ nomeProduto, quantidade, valorUnitario ];
		var valorTotalItem = quantidade * valorUnitario;

		var valorTotalCompra = 0;
		
		$("#valorTotal").each(function() {

		    var value = $(this).text();
		
		   var value2 = accounting.unformat(value,",");
		   
		      if(!isNaN(value2) && value2.length != 0) {
		    	valorTotalCompra = valorTotalCompra + value2 + valorTotalItem;
		   
		    	
		    }
		});
		

		$("#table_div").append("<tr>");

		$.each(data1, function(index, value2) {
			
			if(index==2){
			$("#table_div").append("<td>" + accounting.formatMoney(value2, "R$", 2, ".", ",") + "</td>");
			}else
			{$("#table_div").append("<td>" + value2 + "</td>");}
		});

		$("#table_div").append("<button id='botaoDelete'>Delete</button>");

		$("#table_div").append("</tr>");

		$("#valorTotal").text(accounting.formatMoney(valorTotalCompra, "R$", 2, ".", ","));
		
	

	});
	
	
		
	
	
	
	
	
</script>

</html>








