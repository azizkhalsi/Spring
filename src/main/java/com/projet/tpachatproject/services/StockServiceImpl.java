package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Stock;
import com.projet.tpachatproject.repositories.StockRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StockServiceImpl implements IStockService {


	StockRepository stockRepository;


	@Override
	public List<Stock> retrieveAllStocks() {
		// récuperer la date à l'instant t1
		log.info("In method retrieveAllStocks");
		List<Stock> stocks =  stockRepository.findAll();
		for (Stock stock : stocks) {
			log.info(" Stock : " + stock);
		}
		log.info("out of method retrieveAllStocks");
		// récuperer la date à l'instant t2
		// temps execution = t2 - t1
		return stocks;
	}

	@Override
	public Stock addStock(Stock s) {
		// récuperer la date à l'instant t1
		log.info("In method addStock");
		return stockRepository.save(s);
		
	}

	@Override
	public void deleteStock(Long stockId) {
		log.info("In method deleteStock");
		stockRepository.deleteById(stockId);

	}

	@Override
	public Stock updateStock(Stock s) {
		log.info("In method updateStock");
		return stockRepository.save(s);
	}

	@Override
	public Stock retrieveStock(Long stockId) {
		long start = System.currentTimeMillis();
		log.info("In method retrieveStock");
		Stock stock = stockRepository.findById(stockId).orElse(null);
		log.info("out of method retrieveStock");
		 long elapsedTime = System.currentTimeMillis() - start;
		log.info("Method execution time: " + elapsedTime + " milliseconds.");

		return stock;
	}

	@Override
	public String retrieveStatusStock() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String msgDate = sdf.format(now);
		String newLine = System.getProperty("line.separator");

		List<Stock> stocksEnRouge = stockRepository.retrieveStatusStock();

		// Use StringBuilder for more efficient string concatenation
		StringBuilder finalMessage = new StringBuilder();

		for (int i = 0; i < stocksEnRouge.size(); i++) {
			Stock stock = stocksEnRouge.get(i);
			finalMessage.append(newLine)
					.append(msgDate)
					.append(newLine)
					.append(": le stock ")
					.append(stock.getLibelleStock())
					.append(" a une quantité de ")
					.append(stock.getQte())
					.append(" inférieur à la quantité minimale a ne pas dépasser de ")
					.append(stock.getQteMin())
					.append(newLine);
		}

		String resultMessage = finalMessage.toString(); // Convert StringBuilder to String
		log.info(resultMessage);

		return resultMessage;
	}

}