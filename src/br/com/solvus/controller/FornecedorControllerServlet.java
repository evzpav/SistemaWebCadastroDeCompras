package br.com.solvus.controller;

import java.io.IOException;
import java.sql.SQLException;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.solvus.dao.FornecedorDbUtil;
import br.com.solvus.dao.ProdutoDbUtil;
import br.com.solvus.model.Fornecedor;
import br.com.solvus.model.Produto;
import br.com.solvus.util.HttpUtil;
import br.com.solvus.util.ValidationError;

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

		try {
			fornecedorDbUtil = new FornecedorDbUtil(dataSource);
			produtoDbUtil = new ProdutoDbUtil(dataSource);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String theCommand = request.getParameter("command");

			System.out.println("theCommand" + theCommand);

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String theCommand = request.getParameter("command");

			switch (theCommand) {

			case "ADD":

				addFornecedor(request, response);
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

	private void deleteFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String jsonDeleteFornecedor = HttpUtil.getBody(request);

		Gson gson = new Gson();

		System.out.println("json delete fornecedor: " + jsonDeleteFornecedor);

		Fornecedor fornecedorDoJson = gson.fromJson(jsonDeleteFornecedor, Fornecedor.class);

		int idFornecedor = fornecedorDoJson.getIdFornecedor();

		ValidationError validation = validateDeleteFornecedor(idFornecedor);

		if (validation.isValid()) {

			fornecedorDbUtil.deleteRelationship(idFornecedor);
			fornecedorDbUtil.deleteFornecedor(idFornecedor);
			HttpUtil.setStatusSuccess(response);

		} else {
			HttpUtil.setStatusError(response);
		}

		System.out.println("mensagem: " + validation.getMsg());

		HttpUtil.sendJsonToJsp(response, validation);

	}

	private ValidationError validateDeleteFornecedor(int idFornecedor) throws SQLException {
		ValidationError validation = new ValidationError();
		if (!hasRelationshipCompra(idFornecedor)) {
			validation.setValid(true);
			validation.setMsg("Fornecedor deletado com sucesso!");
		} else {
			validation.setValid(false);
			validation.setMsg("Fornecedor já está registrado em pelo menos uma compra");
		}
		return validation;

	}

	private boolean hasRelationshipCompra(int idFornecedor) throws SQLException {

		return fornecedorDbUtil.hasRelationshipCompra(idFornecedor);

	}

	private void updateFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String jsonUpdateFornecedor = HttpUtil.getBody(request);


		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

		System.out.println("json update produto " + jsonUpdateFornecedor);

		Fornecedor fornecedorDoJson = gson.fromJson(jsonUpdateFornecedor, Fornecedor.class);

		
		ValidationError validation = validateFornecedorInputUpdate(fornecedorDoJson.getNomeFornecedor());

		if (validation.isValid()) {
			Date dataContrato = fornecedorDoJson.getDataContrato();
			String nomeFornecedor = fornecedorDoJson.getNomeFornecedor();
			int idFornecedor = fornecedorDoJson.getIdFornecedor();
			List<Integer> listagemIdProdutos = fornecedorDoJson.getListagemIdProdutos();
			List<Produto> listagemProdutos = new ArrayList<Produto>();
			
			
			addProdutoNaListagemProdutos(listagemIdProdutos, listagemProdutos);
			
			Fornecedor fornecedor = new Fornecedor(nomeFornecedor, dataContrato);
			fornecedor.setIdFornecedor(idFornecedor);
			fornecedor.setListagemProdutos(listagemProdutos);

			fornecedorDbUtil.deleteRelationship(fornecedor.getIdFornecedor());
			fornecedorDbUtil.updateFornecedor(fornecedor);
						
			validation.setMsg("Fornecedor atualizado com sucesso");
			HttpUtil.setStatusSuccess(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());

		} else {
			HttpUtil.setStatusError(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());
		}

		
	}

	private void loadFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String idFornecedorString = request.getParameter("idFornecedor");

		int idFornecedor = Integer.parseInt(idFornecedorString);

		Fornecedor fornecedor = fornecedorDbUtil.getFornecedor(idFornecedor);

		List<Produto> produtos = produtoDbUtil.getProdutos();

		for (Produto produto : produtos) {

			for (Produto produtoFornecedor : fornecedor.getListagemProdutos()) {

				if (produto.getIdProduto() == produtoFornecedor.getIdProduto()) {
					produto.setChecked(true);
				}

			}

		}

		request.setAttribute("PRODUTOS_LIST", produtos);

		request.setAttribute("FORNECEDOR_UPDATE", fornecedor);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-fornecedor-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

	
		String jsonAddFornecedor = HttpUtil.getBody(request);


		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

		System.out.println("json add produto " + jsonAddFornecedor);

		Fornecedor fornecedorDoJson = gson.fromJson(jsonAddFornecedor, Fornecedor.class);

		
		ValidationError validation = validateFornecedorInput(fornecedorDoJson.getNomeFornecedor());

		if (validation.isValid()) {
			Date dataContrato = fornecedorDoJson.getDataContrato();
			String nomeFornecedor = fornecedorDoJson.getNomeFornecedor();

			List<Integer> listagemIdProdutos = fornecedorDoJson.getListagemIdProdutos();
			List<Produto> listagemProdutos = new ArrayList<Produto>();
			
			
			addProdutoNaListagemProdutos(listagemIdProdutos, listagemProdutos);
			
			Fornecedor fornecedor = new Fornecedor(nomeFornecedor, dataContrato);

			fornecedor.setListagemProdutos(listagemProdutos);

			fornecedorDbUtil.addFornecedor(fornecedor);
						
			validation.setMsg("Fornecedor salvo com sucesso");
			HttpUtil.setStatusSuccess(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());

		} else {
			HttpUtil.setStatusError(response);
			HttpUtil.sendJsonToJsp(response, validation);
			System.out.println("Mensagem: " + validation.getMsg());
		}

	}

	private void addProdutoNaListagemProdutos(List<Integer> listagemIdProdutos, List<Produto> listagemProdutos)
			throws Exception {
		for (Integer tempIdProduto : listagemIdProdutos) {
			Produto produto = produtoDbUtil.getProduto(tempIdProduto);
			listagemProdutos.add(produto);
		}
	}

	public ValidationError validateFornecedorInput(String inputName) throws SQLException {

		ValidationError validation = new ValidationError();

		isFornecedorDuplicate(inputName, validation);
		isNameEmpty(inputName, validation);

		return validation;
	}

	public ValidationError validateFornecedorInputUpdate(String inputName) throws SQLException {

		ValidationError validation = new ValidationError();

		isNameEmpty(inputName, validation);

		return validation;
	}
	
	private void isFornecedorDuplicate(String nomeFornecedor, ValidationError validation) throws SQLException {

		boolean nomeDuplicado = fornecedorDbUtil.checkIfDuplicate(nomeFornecedor);
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

	private void listFornecedores(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Fornecedor> fornecedores = fornecedorDbUtil.getFornecedores();

		request.setAttribute("FORNECEDORES_LIST", fornecedores);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-fornecedores.jsp");
		dispatcher.forward(request, response);
	}

	private void listProdutosFornecedor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Produto> produtos = produtoDbUtil.getProdutos();

		request.setAttribute("PRODUTOS_LIST", produtos);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-fornecedor-form.jsp");

		dispatcher.forward(request, response);

	}

}