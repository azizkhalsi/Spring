package com.projet.tpachatproject.controllers;


import com.projet.tpachatproject.entities.Stock;
import com.projet.tpachatproject.services.IStockService;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@Tag(name = "Gestion des stocks")
@RequestMapping("/stock")
@CrossOrigin("*")
@AllArgsConstructor
public class StockRestController {


	IStockService stockService;

	// http://localhost:8089/SpringMVC/stock/retrieve-all-stocks
	@GetMapping("/retrieve-all-stocks")
	@ResponseBody
	public List<Stock> getStocks() {
		return stockService.retrieveAllStocks();
	}

	// http://localhost:8089/SpringMVC/stock/retrieve-stock/8
	@GetMapping("/retrieve-stock/{stock-id}")
	@ResponseBody
	public Stock retrieveStock(@PathVariable("stock-id") Long stockId) {
		return stockService.retrieveStock(stockId);
	}

	// http://localhost:8089/SpringMVC/stock/add-stock
	@PostMapping("/add-stock")
	@ResponseBody
	public Stock addStock(@RequestBody Stock s) {
		return stockService.addStock(s);
	}

	// http://localhost:8089/SpringMVC/stock/remove-stock/{stock-id}
	@DeleteMapping("/remove-stock/{stock-id}")
	@ResponseBody
	public void removeStock(@PathVariable("stock-id") Long stockId) {
		stockService.deleteStock(stockId);
	}

	// http://localhost:8089/SpringMVC/stock/modify-stock
	@PutMapping("/modify-stock")
	@ResponseBody
	public Stock modifyStock(@RequestBody Stock stock) {
		return stockService.updateStock(stock);
	}

	/*
	 * Spring Scheduler : Comparer QteMin tolérée (à ne pa dépasser) avec
	 * Quantité du stock et afficher sur console la liste des produits inférieur
	 * au stock La fct schédulé doit obligatoirement etre sans paramètres et
	 * sans retour (void)
	 */
	// http://localhost:8089/SpringMVC/stock/retrieveStatusStock
	// @Scheduled(fixedRate = 60000)
	// @Scheduled(fixedDelay = 60000)
	//@Scheduled(cron = "*/60 * * * * *")
	//@GetMapping("/retrieveStatusStock")
//	@ResponseBody
//	public void retrieveStatusStock() {
//		stockService.retrieveStatusStock();
//	}

}