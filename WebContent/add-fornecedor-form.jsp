<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
 <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
   <script>
   $( function() {
	    $( "#datepicker" ).datepicker({"dateFormat": "dd/mm/yy"}).val();
	  
   });
  </script>
  
<title>Adicionar Fornecedor</title>


<link type="text/css" rel="stylesheet" href="css/style.css">
<link type="text/css" rel="stylesheet" href="css/add-item-style.css">








</head>

<body>
	
	
	<div id="wrapper">
		<div id="header">
			<h2>Teste programador</h2>
		</div>
	</div>

	<div id="container">
		<h3>Adicionar Fornecedor</h3>
	</div>

	
	
	<form name="myForm" action="FornecedorControllerServlet" method="POST">
		<input type="hidden" name="command" value="ADD" />
		<table>
			<tbody>
				<tr>
					<td><label class="fornLabel">Nome do Fornecedor: </label></td>
					<td><input type="text" name="nomeFornecedor" /></td>
				</tr>
				<tr>
					<td><label class="fornLabel">Data do Contrato: </label></td>
					<td><input type="text" id="datepicker" name="dataContrato" ></td>
								
					
				</tr>
				
				
				<tr>
					<td><label class="fornLabel">Produtos:</label></td>
				</tr>
				<tr>
					<td></td>
					
					<td>
						<c:forEach var="tempProduto" items="${PRODUTOS_LIST}">
								
								
						<div class="checkboxDiv">
							<label for="regular-checkbox" >
								<input  type="checkbox" name="idProduto" id="regular-checkbox" value="${tempProduto.idProduto}">
								
								<span>	${tempProduto.nomeProduto}</span>
							</label>
								
									
							
						</div>
						
						
						</c:forEach>
					</td>
					
				</tr>
				
				<tr>
					<td></td>
					<td><input type="submit" value="Salvar" class="save" /></td>
					<td><input type="reset" value="Cancelar" class="save" /></td>
				</tr>
			
			</tbody>

		</table>
	</form>


	<div style="clear: both;"></div>

	<p>
		<a href="FornecedorControllerServlet">Voltar para lista de fornecedores</a>
	</p>

</body>
</html>