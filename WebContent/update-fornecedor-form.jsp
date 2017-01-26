<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html>
<head>
<jsp:include page= "js-css-files-bootstrap3.jsp" />
	<title> Editar Fornecedor</title>
	



  
</head>


<body>


	<div class="container-fluid">
			<jsp:include page= "header.jsp"/>

 <div class="jumbotron jumbotron-fluid">
  <div class="container">
    <h1 class="display-4">Editar Fornecedor</h1>
   </div>
</div>
 
<div id="alerta"></div>

	
		<div class="row">
			<form action="" method="GET">
				<input type="hidden" name="command" value="UPDATE"/>
			
				<input type="hidden" name="idFornecedor" id="idFornecedor" value="${FORNECEDOR_UPDATE.idFornecedor}"/>
			
				<div class="col-lg-4 col-xs-12 divLabelFornecedor">	
							<label class="fornLabel ">Nome do Fornecedor: </label>
							<input type="text" class="form-control" name="nomeFornecedor" id="nomeFornecedor" value="${FORNECEDOR_UPDATE.nomeFornecedor}"/>
					</div>
					
					<div class="col-lg-4 col-xs-12  divLabelDatePicker">		
							<label class="fornLabel">Data do Contrato: </label>
							<input type="text" class="form-control datepicker" id="dataContrato" name="dataContrato" value="<fmt:formatDate value="${FORNECEDOR_UPDATE.dataContrato}"
												pattern="dd/MM/yyyy" />" >
								
					</div>						
					
				
		</div>				
			
			<div class="row">
				<div class="container" id="bigDivCheckbox">
					<label class="fornLabel">Produtos:</label>
					
					
						<c:forEach var="tempProduto" items="${PRODUTOS_LIST}">
															
									<div id="divCheckbox">
										<label class="form-check" >
										
										<c:if test="${tempProduto.checked}">
											<input  type="checkbox" class="form-check-input" name="${tempProduto.idProduto}" id="regular-checkbox" value="${tempProduto.idProduto}"  checked>
										</c:if>	
										
										<c:if test="${tempProduto.checked == false}">
											<input  type="checkbox" class="form-check-input" name="${tempProduto.idProduto}" id="regular-checkbox" value="${tempProduto.idProduto}"  >
										</c:if>	
										
														
											<span>	${tempProduto.nomeProduto}</span>
										</label>				
									</div>
														
						</c:forEach>
						
		
						
										
				</div>		
			</div>	
				
					<div class="col-xs-4 divLabelProduto">	
						<button type="submit" class="botaoSalvar btn btn-primary btn-lg">Salvar</button>
						<button type="reset" class="botaoCancelar btn btn-secondary btn-lg" onclick="window.location.href='FornecedorControllerServlet'">Cancelar</button>
					</div>	
			
			
				
		
	</form>
	
	
	
			<jsp:include page= "footer.jsp"/>
	</div>

	<script type="text/javascript">
	

	
		$(".botaoSalvar")
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

								var jsonUpdateFornecedor = {
									nomeFornecedor : $('#nomeFornecedor').val(),
									dataContrato: $('#dataContrato').val(),
									listagemIdProdutos: produtosCheckbox,
									idFornecedor: $('#idFornecedor').val()
								};
								console.log("json"+ jsonUpdateFornecedor);
								
								$.ajax({
											type : "POST",
											url : "FornecedorControllerServlet?command=UPDATE",
											dataType : 'json',
											contentType : 'application/json; charset=utf-8',
											data : JSON.stringify(jsonUpdateFornecedor),
											success : function(data1) {
												console.log(data1);
												var alertaSucesso = '<div class="alert alert-success" role="alert">  <strong>Sucesso! </strong>'+ data1.msg + '.</div>';
												$('#alerta').html(alertaSucesso);
												window.open ('FornecedorControllerServlet','_self',false)
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