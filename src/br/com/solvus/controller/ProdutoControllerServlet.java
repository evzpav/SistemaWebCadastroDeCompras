package br.com.solvus.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import br.com.solvus.dao.ProdutoDbUtil;
import br.com.solvus.model.Produto;
import br.com.solvus.util.HttpUtil;
import br.com.solvus.util.ValidationError;

@WebServlet("/ProdutoControllerServlet")
public class ProdutoControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProdutoDbUtil produtoDbUtil;


	@Resource(name = "jdbc/TesteProgramador1Web")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

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
				
			case "IR_PARA_ADD_PRODUTO":
				showAddProduto(request, response);

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

	private void showAddProduto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-produto-form.jsp");

		dispatcher.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String theCommand = request.getParameter("command");

			switch (theCommand) {

			case "ADD":

				addProduto(request, response);
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

	private void deleteProduto(HttpServletRequest request, HttpServletResponse response) throws Exception {


		String jsonDeleteProduto = HttpUtil.getBody(request);

		Gson gson = new Gson();

		System.out.println("json delete produto: " + jsonDeleteProduto);

		Produto produtoDoJson = gson.fromJson(jsonDeleteProduto, Produto.class);

		int idProduto = produtoDoJson.getIdProduto();

		ValidationError validation = validateDeleteProduto(idProduto);

		if (validation.isValid()) {
			produtoDbUtil.deleteProduto(idProduto);
			HttpUtil.setStatusSuccess(response);
			
		} else {
			HttpUtil.setStatusError(response);
		}
		
		System.out.println("mensagem: "+validation.getMsg());
		
		HttpUtil.sendJsonToJsp(response, validation);

	}

	private ValidationError validateDeleteProduto(int idProduto) throws SQLException {

		ValidationError validation = new ValidationError();
		if (!hasRelationshipFornecedor(idProduto)) {
			validation.setValid(true);
			validation.setMsg("Produto deletado com sucesso!");
		} else {
			validation.setValid(false);
			validation.setMsg("Produto já está registrado em pelo menos um fornecedor.");
		}
		return validation;
	}

	private void updateProduto(HttpServletRequest request, HttpServletResponse response) throws Exception {

//		String produtoIdString = request.getParameter("idProduto");
//		int produtoId = Integer.parseInt(produtoIdString);
//		String nomeProduto = request.getParameter("nomeProduto");
//
//		validateProdutoInput(nomeProduto);
//
//		Produto produto = new Produto(nomeProduto);
//		produto.setIdProduto(produtoId);
//		produtoDbUtil.updateProduto(produto);
//
//		listProdutos(request, response);

		
		String jsonUpdateProduto = HttpUtil.getBody(request);

		Gson gson = new Gson();

		System.out.println("json update produto " + jsonUpdateProduto);

		Produto produtoDoJson = gson.fromJson(jsonUpdateProduto, Produto.class);

		ValidationError validation = validateProdutoInput(produtoDoJson.getNomeProduto());

		if (validation.isValid()) {
			Produto produto = new Produto(produtoDoJson.getNomeProduto());
			produto.setIdProduto(produtoDoJson.getIdProduto());
			produtoDbUtil.updateProduto(produto);
			validation.setMsg("Produto atualizado com sucesso");
			HttpUtil.setStatusSuccess(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());
		} else {
			HttpUtil.setStatusError(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());
		}
		
		
	}

	private void loadProduto(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String idProdutoString = request.getParameter("idProduto");

		int idProduto = Integer.parseInt(idProdutoString);
		Produto produto = produtoDbUtil.getProduto(idProduto);

		request.setAttribute("PRODUTO_UPDATE", produto);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-produto-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addProduto(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String jsonAddProduto = HttpUtil.getBody(request);

		Gson gson = new Gson();

		System.out.println("json add produto " + jsonAddProduto);

		Produto produtoDoJson = gson.fromJson(jsonAddProduto, Produto.class);

		ValidationError validation = validateProdutoInput(produtoDoJson.getNomeProduto());

		if (validation.isValid()) {
			Produto produto = new Produto(produtoDoJson.getNomeProduto());
			produtoDbUtil.addProduto(produto);
			validation.setMsg("Produto salvo com sucesso");
			HttpUtil.setStatusSuccess(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());
		} else {
			HttpUtil.setStatusError(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());
		}
		
	}

	public ValidationError validateProdutoInput(String inputName) throws SQLException {

		ValidationError validation = new ValidationError();

		isProdutoDuplicate(inputName, validation);
		isNameEmpty(inputName, validation);
		
		return validation;
	}

	private void isProdutoDuplicate(String nomeProduto, ValidationError validation) throws SQLException {

		boolean nomeDuplicado = produtoDbUtil.checkIfDuplicate(nomeProduto);
		if (nomeDuplicado) {
			validation.setValid(false);
			validation.setMsg("O nome já existe.");

		}
	}

	public void isNameEmpty(String inputName, ValidationError validation) {
		if (inputName.isEmpty()) {
			validation.setValid(false);
			validation.setMsg("Preencha um nome válido.");

		}
	}

	private void listProdutos(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Produto> produtos = produtoDbUtil.getProdutos();

		request.setAttribute("PRODUTOS_LIST", produtos);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-produtos.jsp");

		dispatcher.forward(request, response);

	}

	private boolean hasRelationshipFornecedor(int idProduto) throws SQLException {

		return produtoDbUtil.hasRelationshipFornecedor(idProduto);

	}

}
