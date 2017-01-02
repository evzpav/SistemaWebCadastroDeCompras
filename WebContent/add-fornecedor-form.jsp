<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>

<jsp:include page= "css-files.jsp"/>


<title>Adicionar Fornecedor</title>



</head>

<body>
	
	<div class="container-fluid">
			<jsp:include page= "header.jsp"/>
		
	 <div class="jumbotron jumbotron-fluid">
  <div class="container">
    <h1 class="display-4">Adicionar Fornecedor</h1>
   </div>
</div>


	
	
	<form name="myForm" action="FornecedorControllerServlet" method="POST">
		<input type="hidden" name="command" value="ADD" />
			
			<div class="col-xs-4 divLabelFornecedor">	
					<label class="fornLabel ">Nome do Fornecedor: </label>
					<input type="text" class="form-control" name="nomeFornecedor" />
			</div>
			
			<div class="col-xs-4 divLabelDatePicker">		
					<label class="fornLabel">Data do Contrato: </label>
					<input type="text" class="form-control" id="datepicker" name="dataContrato" >
			</div>						
					
				
					
			
					
				<div class="container" id="bigDivCheckbox">
					<label class="fornLabel">Produtos:</label>
						<c:forEach var="tempProduto" items="${PRODUTOS_LIST}">
								
					
						<div id="divCheckbox">
							<label class="form-check" >
								<input  type="checkbox" class="form-check-input" name="idProduto" id="regular-checkbox" value="${tempProduto.idProduto}">
								
								<span>	${tempProduto.nomeProduto}</span>
							</label>
											
						
						</div>
						
							
						</c:forEach>
					
				</div>		
					
				
					<div class="col-xs-4 divLabelProduto">	
						<button type="submit" class="botaoSalvar btn btn-primary btn-lg">Salvar</button>
						<button type="reset" class="botaoCancelar btn btn-secondary btn-lg" onclick="window.location.href='FornecedorControllerServlet'">Cancelar</button>
					</div>	
			
			

		
	</form>


	

	
	
	
		<jsp:include page= "footer.jsp"/>
	</div>
	
</body>
</html>