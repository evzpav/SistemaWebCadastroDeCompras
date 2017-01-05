package br.com.solvus.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import br.com.solvus.jdbc.Fornecedor;
import br.com.solvus.jdbc.Produto;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/FornecedorControllerServlet")
public class FornecedorControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FornecedorDbUtil fornecedorDbUtil;
	private ProdutoDbUtil produtoDbUtil;

	@Resource(name = "jdbc/TesteProgramador1Web")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		// create our student db util ... and pass in the conn pool / datasource
		try {
			fornecedorDbUtil = new FornecedorDbUtil(dataSource);
			produtoDbUtil = new ProdutoDbUtil(dataSource);
			// produtoControllerServlet = new ProdutoControllerServlet();
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String theCommand = request.getParameter("command");
						
			System.out.println("theCommand"+theCommand);
		
			
			if (theCommand == null) {
				theCommand = "LIST";

			}
			switch (theCommand) {

			case "LIST":

				listFornecedores(request, response);
				break;

			case "IR_PARA_ADICIONAR_FORNECEDOR":
				listProdutosFornecedor(request, response);
				break;
			

			case "LOAD":
				loadFornecedor(request, response);
				
				break;

			case "UPDATE":
				updateFornecedor(request, response);
				break;

			case "DELETE":
				deleteFornecedor(request, response);
				break;

			default:
				listFornecedores(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}

	// private void loadProduto(HttpServletRequest request, HttpServletResponse
	// response) throws Exception {
	//
	// String produtoId = request.getParameter("idProduto");
	// Produto produto = produtoDbUtil.getProduto(produtoId);
	//
	// }

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");

			// route to the appropriate method
			switch (theCommand) {

			case "ADD":

				addFornecedor(request, response);
				break;

			default:

				listFornecedores(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}

	private void deleteFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student id from form data
		String fornecedorIdString = request.getParameter("idFornecedor");
		int idFornecedor = Integer.parseInt(fornecedorIdString);

		fornecedorDbUtil.deleteRelationship(idFornecedor);
		fornecedorDbUtil.deleteFornecedor(idFornecedor);

		// send them back to "list students" page
		listFornecedores(request, response);
	}

	private void updateFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String nomeFornecedor = request.getParameter("nomeFornecedor");
		String dataContratoString = request.getParameter("dataContrato");
		
		String fornecedorIdString = request.getParameter("idFornecedor");
		int idFornecedor = Integer.parseInt(fornecedorIdString);
				
		Date dataContrato = convertStringToDate(dataContratoString);

		List<Produto> listagemProdutos = new ArrayList<Produto>();

		String[] produtoIdString = request.getParameterValues("idProduto");
		for (String tempProdutoString : produtoIdString) {
			int tempIdProduto =  Integer.parseInt(tempProdutoString);
			Produto produto = produtoDbUtil.getProduto(tempIdProduto);
			listagemProdutos.add(produto);
			
		}
		
		Fornecedor fornecedor = new Fornecedor(nomeFornecedor, dataContrato);
		fornecedor.setListagemProdutos(listagemProdutos);
		fornecedor.setIdFornecedor(idFornecedor);
		
		fornecedorDbUtil.deleteRelationship(fornecedor.getIdFornecedor());
				
		fornecedorDbUtil.updateFornecedor(fornecedor);

		

		listFornecedores(request, response);

	}
	
	

	//
	private void loadFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student id from form data
		String idFornecedorString = request.getParameter("idFornecedor");

		int idFornecedor = Integer.parseInt(idFornecedorString);
		
		Fornecedor fornecedor = fornecedorDbUtil.getFornecedor(idFornecedor);

		List<Produto> produtos = produtoDbUtil.getProdutos();
		
		for (Produto produto : produtos) {
			
			for (Produto produtoFornecedor : fornecedor.getListagemProdutos()){
				
				if (produto.getIdProduto() == produtoFornecedor.getIdProduto()){
					produto.setChecked(true);
				}
				
			}
			
		}
			

		// add students to the request
		request.setAttribute("PRODUTOS_LIST", produtos);
		
		
		
			// place student in the request attribute
		request.setAttribute("FORNECEDOR_UPDATE", fornecedor);

		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-fornecedor-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String nomeFornecedor = request.getParameter("nomeFornecedor");
		String dataContratoString = request.getParameter("dataContrato");
		System.out.println("string:" + dataContratoString);
		Date dataContrato = convertStringToDate(dataContratoString);

		List<Produto> listagemProdutos = new ArrayList<Produto>();

		String[] produtoIdString = request.getParameterValues("idProduto");
		for (String tempProdutoString : produtoIdString) {
				int tempIdProduto =  Integer.parseInt(tempProdutoString);
			Produto produto = produtoDbUtil.getProduto(tempIdProduto);
			listagemProdutos.add(produto);
		}

		Fornecedor fornecedor = new Fornecedor(nomeFornecedor, dataContrato);
		fornecedor.setListagemProdutos(listagemProdutos);

		fornecedorDbUtil.addFornecedor(fornecedor);

		// send back to main page (the student list)
		// SEND AS REDIRECT to avoid multiple-browser reload issue
		response.sendRedirect(request.getContextPath() + "/FornecedorControllerServlet?command=LIST");
	}

	private void listFornecedores(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from db util
		List<Fornecedor> fornecedores = fornecedorDbUtil.getFornecedores();

		// add students to the request
		request.setAttribute("FORNECEDORES_LIST", fornecedores);

		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-fornecedores.jsp");
		dispatcher.forward(request, response);
	}

	private void listProdutosFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from db util
		List<Produto> produtos = produtoDbUtil.getProdutos();

		// add students to the request
		request.setAttribute("PRODUTOS_LIST", produtos);

		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-fornecedor-form.jsp");

		dispatcher.forward(request, response);

	}
	
	

	// private ValidationError validateDataEntry(String nomeProduto) throws
	// SQLException {
	// ValidationError validation = new ValidationError();
	//
	// if (nomeProduto.isEmpty()) {
	// validation.setValid(false);
	// validation.setMsg("O nome está em branco");
	// }
	//
	// boolean isProdutoDuplicado =
	// (produtoDbUtil.checkIfDuplicate(nomeProduto));
	// if (isProdutoDuplicado) {
	// validation.setValid(false);
	// validation.setMsg("Produto duplicado");
	// }
	//
	// return validation;
	// }

	// private void validateToAddForm(HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	//
	// String nomeProduto = request.getParameter("nomeProduto");
	//
	// ValidationError validationToForm = validateDataEntry(nomeProduto);
	//
	// request.setAttribute("VALIDATION", validationToForm);
	//
	// RequestDispatcher dispatcher =
	// request.getRequestDispatcher("/add-produto-form.jsp");
	// dispatcher.forward(request, response);
	// }

	public Date convertStringToDate(String inputStringDate) {
		Date convertedDate = null;

		try {
			DateFormat formatter = null;

			formatter = new SimpleDateFormat("dd/MM/yyyy");
			convertedDate = (Date) formatter.parse(inputStringDate);
		} catch (ParseException parse) {
			convertedDate = null;

		}

		return convertedDate;
	}
}