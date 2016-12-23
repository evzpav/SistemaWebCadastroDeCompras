package br.com.solvus.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import br.com.solvus.jdbc.Produto;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/ProdutoControllerServlet")
public class ProdutoControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProdutoDbUtil produtoDbUtil;

	@Resource(name = "jdbc/TesteProgramador1Web")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		// create our student db util ... and pass in the conn pool / datasource
		try {
			produtoDbUtil = new ProdutoDbUtil(dataSource);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String theCommand = request.getParameter("command");

			if (theCommand == null) {
				theCommand = "LIST";

			}
			switch (theCommand) {

			case "LIST":
				listProdutos(request, response);
				break;

			case "ADD":
				addProduto(request, response);
				break;

			case "LOAD":
				loadProduto(request, response);
				break;
				
			case "UPDATE":
				 updateProduto(request, response);
				 break;
				 
			 case "DELETE":
				 deleteProduto(request, response);
				 break;
				
			default:
				listProdutos(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}

	 private void deleteProduto(HttpServletRequest request,
	 HttpServletResponse response)
	 throws Exception {
	
	 // read student id from form data
	 String produtoIdString = request.getParameter("produtoId");
	 int produtoId = Integer.parseInt(produtoIdString);
	 
	
		 produtoDbUtil.deleteProduto(produtoId);
	
	
	 // send them back to "list students" page
	 listProdutos(request, response);
	 }
	
	 
	 private void updateProduto(HttpServletRequest request,
	 HttpServletResponse response)
	 throws Exception {
	
	 // read student info from form data
	 String produtoIdString = request.getParameter("produtoId");
	 int produtoId = Integer.parseInt(produtoIdString);
	 String nomeProduto = request.getParameter("nomeProduto");
	 
	
	 
	 // create a new student object
	 Produto produto = new Produto(nomeProduto);
	 produto.setIdProduto(produtoId);
	 // perform update on database
	 produtoDbUtil.updateProduto(produto);
	
	 // send them back to the "list students" page
	 listProdutos(request, response);
	
	 }
	//
	 private void loadProduto(HttpServletRequest request, HttpServletResponse
	 response)
	 throws Exception {
	
	 // read student id from form data
	 String produtoId = request.getParameter("produtoId");
	
	 // get student from database (db util)
	 Produto produto = produtoDbUtil.getProduto(produtoId);
	
	 // place student in the request attribute
	 request.setAttribute("PRODUTO_UPDATE", produto);
	
	 // send to jsp page: update-student-form.jsp
	 RequestDispatcher dispatcher =
	 request.getRequestDispatcher("/update-produto-form.jsp");
	 dispatcher.forward(request, response);
	 }
	
	private void addProduto(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String nomeProduto = request.getParameter("nomeProduto");

		// create a new student object
		Produto produto = new Produto(nomeProduto);

		
		// add the student to the database
		produtoDbUtil.addProduto(produto);

		// send back to main page (the student list)
		listProdutos(request, response);
	}

	private void listProdutos(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from db util
		List<Produto> produtos = produtoDbUtil.getProdutos();

		// add students to the request
		request.setAttribute("PRODUTOS_LIST", produtos);

		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-produtos.jsp");
		dispatcher.forward(request, response);
	}

}
