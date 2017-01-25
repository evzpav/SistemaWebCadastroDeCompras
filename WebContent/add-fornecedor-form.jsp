<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>

<jsp:include page="js-css-files.jsp" />


<title>Adicionar Fornecedor</title>



</head>

<body>

	<div class="container-fluid">
		<jsp:include page="header.jsp" />

		<div class="jumbotron jumbotron-fluid">
			<div class="container">
				<h1 class="display-4">Adicionar Fornecedor</h1>
			</div>
		</div>


		<div id="alerta"></div>

		<form id="myForm" name="myForm" action=""
			method="POST">
			<input type="hidden" name="command" value="ADD" />

			<div class="col-xs-4 divLabelFornecedor">
				<label class="fornLabel ">Nome do Fornecedor: </label> <input
					type="text" class="form-control" name="nomeFornecedor"
					id="nomeFornecedor" />
			</div>

			<div class="col-xs-4 divLabelDatePicker">
				<label class="fornLabel">Data do Contrato: </label> <input
					type="text" class="form-control datepicker" name="dataContrato"
					id="dataContrato">
			</div>




			<div class="row">
				<div class="container" id="bigDivCheckbox">
			
					<label class="fornLabel">Produtos:</label>
					<c:forEach var="tempProduto" items="${PRODUTOS_LIST}">
	
	
						<div id="divCheckbox">
							<label class="form-check"> <input type="checkbox"
								class="form-check-input regular-checkbox" name="${tempProduto.idProduto}"
								value="${tempProduto.idProduto}"> <span>${tempProduto.nomeProduto}</span>
							</label>
	
	
						</div>
	
	
					</c:forEach>

				</div>
			</div>

			<div class="col-xs-4 divLabelProduto">
				<button class="botaoSalvar btn btn-primary btn-lg" id="botaoSalvar">Salvar</button>
				<button type="reset" class="botaoCancelar btn btn-secondary btn-lg"
					onclick="window.location.href='FornecedorControllerServlet'">Cancelar</button>
			</div>




		</form>







		<jsp:include page="footer.jsp" />
	</div>

	<script type="text/javascript">
	
	
		$("#botaoSalvar")
				.click(
						function(event) {
							event.preventDefault();

							var error = "";

							if ($('#dataContrato').val() == "") {
								error += "Data em branco. <br>";

							}

							if ($('#nomeFornecedor').val() == "") {
								error += "Nome do fornecedor em branco. <br>";
							}
							 
							
							var produtosCheckbox = [];
							 $('#divCheckbox input:checked').each(function() {
								 produtosCheckbox.push(this.name);
						        });
							 
							 
							if(produtosCheckbox.length === 0){
								 error += "Nenhum produto selecionado. <br>";
							 }
							

							if (error !== "") {
								var alertaErro = '<div class="alert alert-danger" role="alert">  <strong>Erro! </strong><br>' + error + '</div>';
								$('#alerta').html(alertaErro);

							} else {

								var jsonAddFornecedor = {
									nomeFornecedor : $('#nomeFornecedor').val(),
									dataContrato: $('#dataContrato').val(),
									listagemIdProdutos: produtosCheckbox
										
								};
								console.log("json"+ jsonAddFornecedor);
								
								$.ajax({
											type : "POST",
											url : "FornecedorControllerServlet?command=ADD",
											dataType : 'json',
											contentType : 'application/json; charset=utf-8',
											data : JSON.stringify(jsonAddFornecedor),
											success : function(data1) {
												console.log(data1);
												var alertaSucesso = '<div class="alert alert-success" role="alert">  <strong>Sucesso! </strong>'+ data1.msg + '.</div>';
												$('#alerta').html(alertaSucesso);
												window.location.reload();
											},

											error : function(data) {

												var alertaErro = '<div class="alert alert-danger" role="alert">  <strong>Erro! </strong>'+ data.responseJSON.msg + '</div>';
												$('#alerta').html(alertaErro);
											}
										});

							}
						});
	</script>

</body>
</html>