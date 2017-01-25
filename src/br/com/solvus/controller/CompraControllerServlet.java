package br.com.solvus.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import br.com.solvus.dao.CompraDbUtil;
import br.com.solvus.dao.FornecedorDbUtil;
import br.com.solvus.dao.ItemDeCompraDbUtil;
import br.com.solvus.dao.ProdutoDbUtil;
import br.com.solvus.model.Compra;
import br.com.solvus.model.DadosTabelaAddCompra;
import br.com.solvus.model.Fornecedor;
import br.com.solvus.model.ItemDeCompra;
import br.com.solvus.model.Itens;
import br.com.solvus.model.Produto;
import br.com.solvus.util.ConvertDate;
import br.com.solvus.util.HttpUtil;
import br.com.solvus.util.ValidationError;

@WebServlet("/CompraControllerServlet")
public class CompraControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CompraDbUtil compraDbUtil;
	private FornecedorDbUtil fornecedorDbUtil;
	private ProdutoDbUtil produtoDbUtil;

	@Resource(name = "jdbc/TesteProgramador1Web")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		try {
			compraDbUtil = new CompraDbUtil(dataSource);
			new ItemDeCompraDbUtil(dataSource);
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

				listCompras(request, response);
				break;

			case "FILTRAR":
				filterCompra(request, response);
				break;

			case "IR_PARA_ADICIONAR_COMPRA":
				listFornecedores(request, response);

				break;

			case "FILTRAR_FORNECEDOR":
				listProdutoFornecedorSelecionado(request, response);
				break;

			case "DELETE":
				deleteCompra(request, response);
				break;

			default:
				listCompras(request, response);
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

				addCompra(request, response);

				break;

			default:

				listCompras(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}

	private void deleteCompra(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String idCompraString = request.getParameter("idCompra");
		int idCompra = Integer.parseInt(idCompraString);

		compraDbUtil.deleteRelationship(idCompra);
		compraDbUtil.deleteCompra(idCompra);

		listCompras(request, response);
	}

	private void filterCompra(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String idFornecedorString = request.getParameter("idFornecedor");
		String dataInicialString = request.getParameter("dataInicial");
		String dataFinalString = request.getParameter("dataFinal");

		int idFornecedor = Integer.parseInt(idFornecedorString);
		List<Compra> compras = new ArrayList<Compra>();

		if (dataInicialString.isEmpty() || dataFinalString.isEmpty()) {

			compras = compraDbUtil.filterListaCompra(idFornecedor);
		} else {

			Date dataInicial = ConvertDate.convertStringToDate(dataInicialString);
			
			java.sql.Date dataInicialSql = ConvertDate.convertDateToSqlDate(dataInicial);

			Date dataFinal = ConvertDate.convertStringToDate(dataFinalString);

			java.sql.Date dataFinalSql = ConvertDate.convertDateToSqlDate(dataFinal);

			compras = compraDbUtil.filterListaCompra(idFornecedor, dataInicialSql, dataFinalSql);
		}
		request.setAttribute("COMPRAS_LIST", compras);

		List<Fornecedor> fornecedores = fornecedorDbUtil.getFornecedores();
		request.setAttribute("FORNECEDORES_LIST", fornecedores);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-compras.jsp");

		dispatcher.forward(request, response);
	}

	

	private void addCompra(HttpServletRequest request, HttpServletResponse response) throws Exception {

		DadosTabelaAddCompra dados = receiveJsonFromJsp(request);

		List<ItemDeCompra> listaItemDeCompra = new ArrayList<ItemDeCompra>();
		Itens itens[] = dados.getItens();
		double valorTotalCompra = 0;
		for (Itens item : itens) {

			int idProduto = item.getIdProduto();
			int quantidade = item.getQuantidade();
			double valorUnitario = item.getValorUnitario();

			Produto produto = produtoDbUtil.getProduto(idProduto);
			ItemDeCompra itemDeCompra = new ItemDeCompra(produto, quantidade, valorUnitario);
			listaItemDeCompra.add(itemDeCompra);

			valorTotalCompra += valorUnitario * quantidade;
		}

		String dataCompraString = dados.getDataCompra();
		Date dataCompra = ConvertDate.convertStringToDate(dataCompraString);

		Fornecedor fornecedor = fornecedorDbUtil.getFornecedor(dados.getIdFornecedor());
		Compra compra = new Compra(fornecedor, dataCompra);

		ValidationError validation = validateCompra(compra);

		if (validation.isValid()) {

			compra.setValorTotal(valorTotalCompra);
			compra.setListaDeItemDeCompra(listaItemDeCompra);

			compraDbUtil.addCompra(compra);
			compraDbUtil.addItemDeCompra(compra);
			compraDbUtil.saveRelationship(compra);

			validation.setMsg("Compra salva com sucesso");

			HttpUtil.setStatusSuccess(response);
	
		} else {

			HttpUtil.setStatusError(response);
			

		}
		HttpUtil.sendJsonToJsp(response, validation);
	}

	private DadosTabelaAddCompra receiveJsonFromJsp(HttpServletRequest request) throws IOException {
		String json = HttpUtil.getBody(request);

		Gson gson = new Gson();

		System.out.println("json " + json);

		DadosTabelaAddCompra dados = gson.fromJson(json, DadosTabelaAddCompra.class);
		
		return dados;
	}

	

	private ValidationError validateCompra(Compra compra) throws Exception {
		ValidationError validation = new ValidationError();

		validation.setValid(compraDbUtil.isDataCompraValid(compra));
		validation.setMsg("Data invalida. A data de compra e menor que a data de contrato do fornecedor.");

		return validation;

	}

	private void listCompras(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Compra> compras = compraDbUtil.getCompras();

		request.setAttribute("COMPRAS_LIST", compras);

		List<Fornecedor> fornecedores = fornecedorDbUtil.getFornecedoresWithCompra();
		request.setAttribute("FORNECEDORES_LIST", fornecedores);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-compras.jsp");

		dispatcher.forward(request, response);
	}

	private void listFornecedores(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<Fornecedor> fornecedores = fornecedorDbUtil.getFornecedores();

		request.setAttribute("FORNECEDORES_LIST", fornecedores);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-compra-form.jsp");

		dispatcher.forward(request, response);

	}

	private void listProdutoFornecedorSelecionado(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String idFornecedorString = request.getParameter("idFornecedor");

		int idFornecedor = Integer.parseInt(idFornecedorString);

		Fornecedor fornecedor = fornecedorDbUtil.getFornecedor(idFornecedor);

		request.setAttribute("FORNECEDOR_SELECIONADO", fornecedor);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-compra-form.jsp");

		dispatcher.forward(request, response);

	}


	

}