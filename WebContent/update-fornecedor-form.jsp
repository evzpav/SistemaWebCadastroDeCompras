<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!DOCTYPE html>
<html>
<head>
	<title> Editar Fornecedor</title>
	
<jsp:include page= "css-files.jsp"/>


  
</head>


<body>


	<div class="container-fluid">
			<jsp:include page= "header.jsp"/>

 <div class="jumbotron jumbotron-fluid">
  <div class="container">
    <h1 class="display-4">Editar Fornecedor</h1>
   </div>
</div>
 

	<form action="FornecedorControllerServlet" method="GET">
		<input type="hidden" name="command" value="UPDATE"/>
	
		<input type="hidden" name="idFornecedor" value="${FORNECEDOR_UPDATE.idFornecedor}"/>
	
		<div class="col-xs-4 divLabelFornecedor">	
					<label class="fornLabel ">Nome do Fornecedor: </label>
					<input type="text" class="form-control" name="nomeFornecedor" value="${FORNECEDOR_UPDATE.nomeFornecedor}"/>
			</div>
			
			<div class="col-xs-4 divLabelDatePicker">		
					<label class="fornLabel">Data do Contrato: </label>
					<input type="text" class="form-control" id="datepicker" name="dataContrato" value="<fmt:formatDate value="${FORNECEDOR_UPDATE.dataContrato}"
										pattern="dd/MM/yyyy" />" >
						
			</div>						
					
				
					
			
					
				<div class="container" id="bigDivCheckbox">
					<label class="fornLabel">Produtos:</label>
					
					
						<c:forEach var="tempProduto" items="${PRODUTOS_LIST}">
															
									<div id="divCheckbox">
										<label class="form-check" >
										
										<c:if test="${tempProduto.checked}">
											<input  type="checkbox" class="form-check-input" name="idProduto" id="regular-checkbox" value="${tempProduto.idProduto}"  checked>
										</c:if>	
										
										<c:if test="${tempProduto.checked == false}">
											<input  type="checkbox" class="form-check-input" name="idProduto" id="regular-checkbox" value="${tempProduto.idProduto}"  >
										</c:if>	
										
														
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