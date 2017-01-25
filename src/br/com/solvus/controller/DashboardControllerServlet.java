package br.com.solvus.controller;

import java.io.IOException;
import java.sql.SQLException;
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

import br.com.solvus.dao.DashboardDbUtil;
import br.com.solvus.dao.FornecedorDbUtil;
import br.com.solvus.dao.ItemDeCompraDbUtil;
import br.com.solvus.dao.ProdutoDbUtil;
import br.com.solvus.model.DashboardDTO;
import br.com.solvus.model.FillDropDownsDTO;
import br.com.solvus.model.FiltroDashboard;
import br.com.solvus.model.Fornecedor;
import br.com.solvus.model.GraphFornecedorDTO;
import br.com.solvus.model.GraphProdutoDTO;
import br.com.solvus.model.GraphVolumeMensalProdutoDTO;
import br.com.solvus.model.Produto;
import br.com.solvus.model.TableTopFornecedoresDTO;
import br.com.solvus.util.HttpUtil;

@WebServlet("/DashboardControllerServlet")
public class DashboardControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	private FornecedorDbUtil fornecedorDbUtil;
	private DashboardDbUtil dashboardDbUtil;
	private ProdutoDbUtil produtoDbUtil;

	@Resource(name = "jdbc/TesteProgramador1Web")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		try {
			new ItemDeCompraDbUtil(dataSource);
			fornecedorDbUtil = new FornecedorDbUtil(dataSource);
			produtoDbUtil = new ProdutoDbUtil(dataSource);
			dashboardDbUtil = new DashboardDbUtil(dataSource);

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

				listDashboard(request, response);
				break;
				
			case "LIST_FORNECEDORES":
				listFornecedores(request, response);
				break;
				
			case "LIST_TOP_FORNECEDORES":
				listTopFornecedores(request, response);
				break;
				
			default:
				listDashboard(request, response);
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

			case "FILTRAR":
				filtrarDashboard(request, response);
				break;

			default:

				listDashboard(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}



	

	
		
	

	private void filtrarDashboard(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		
		String jsonFiltrarDashboard = HttpUtil.getBody(request);

		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			
		System.out.println("json filtrar dashboard " + jsonFiltrarDashboard);

		FiltroDashboard filtroDoJson = gson.fromJson(jsonFiltrarDashboard, FiltroDashboard.class);

		Date dataInicial = filtroDoJson.getDataInicial();
		Date dataFinal = filtroDoJson.getDataFinal();
		Integer idProduto = filtroDoJson.getIdProduto();
		Integer [] arrayIdFornecedores = filtroDoJson.getArrayIdFornecedoresSelecionados();
		
		
		List<GraphFornecedorDTO> listLinesGraphFornecedorDTO = dashboardDbUtil.filtrarGraficoPercentualFornecedores(arrayIdFornecedores, idProduto, dataInicial, dataFinal);
		List<GraphProdutoDTO> listLinesGraphProdutoDTO = dashboardDbUtil.filtrarGraficoPercentualProduto(arrayIdFornecedores, idProduto, dataInicial, dataFinal);
		List<GraphVolumeMensalProdutoDTO> listLinesGraphVolumeMensalProdutoDTO = dashboardDbUtil.filtrarGraficoBarraVolumeMensalProduto(arrayIdFornecedores, idProduto, dataInicial, dataFinal);
		List<TableTopFornecedoresDTO> listLinesTopFornecedoresDTO = dashboardDbUtil.filtrarTabelaTopFornecedores(arrayIdFornecedores, idProduto, dataInicial, dataFinal);
		
		DashboardDTO dashboardDTO = new DashboardDTO(listLinesGraphFornecedorDTO, listLinesGraphProdutoDTO, listLinesGraphVolumeMensalProdutoDTO, listLinesTopFornecedoresDTO);
		
		String jsonDashboardDTO = new Gson().toJson(dashboardDTO);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonDashboardDTO);
		
		System.out.println("json dashboard dto "+jsonDashboardDTO);
			
	}

	private void listDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {


		 RequestDispatcher dispatcher =
		 request.getRequestDispatcher("/dashboard.jsp");
		 dispatcher.forward(request, response);
	}

	private void listFornecedores(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Fornecedor> fornecedores = fornecedorDbUtil.getFornecedoresWithCompra();
		List<Produto> produtos = produtoDbUtil.getProdutos();
		
		FillDropDownsDTO fillDropDownsDTO = new FillDropDownsDTO(fornecedores, produtos);
		
		String jsonFillDropDown = new Gson().toJson(fillDropDownsDTO);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonFillDropDown);
		
		
	}
	
	private void listTopFornecedores(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		List<Fornecedor> topFornecedores = fornecedorDbUtil.getTopFornecedoresWithCompra();

	 for (Fornecedor fornecedor : topFornecedores) {
		System.out.println(fornecedor.getNomeFornecedor() +" "+ fornecedor.getTotalEmCompras());
	}
		
		
		String jsonFillTopFornecedores = new Gson().toJson(topFornecedores);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonFillTopFornecedores);
		
		System.out.println(jsonFillTopFornecedores);
		
	}

	

}