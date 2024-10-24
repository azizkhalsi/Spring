package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StockServiceImplTest {
	@Autowired
	IStockService stockService;
	
	//@Test
	public void testAddStock() {
	//	List<Stock> stocks = stockService.retrieveAllStocks();
	//	int expected=stocks.size();

		Stock s = new Stock("stock test",10,100);
		Stock savedStock= stockService.addStock(s);
		
	//	assertEquals(expected+1, stockService.retrieveAllStocks().size());
		assertNotNull(savedStock.getLibelleStock());
		stockService.deleteStock(savedStock.getIdStock());
		
	} 
	
	//@Test
	public void testAddStockOptimized() {
		//arrange
		Stock s = new Stock("stock test",10,100);

		//act
		Stock savedStock= stockService.addStock(s);

		//assert
		assertNotNull(savedStock.getIdStock());
		assertSame(10, savedStock.getQte());
		assertTrue(savedStock.getQteMin()>0);
		stockService.deleteStock(savedStock.getIdStock());
		
	} 
	
	//@Test
	public void testDeleteStock() {
		Stock s = new Stock("stock test",30,60);
		Stock savedStock= stockService.addStock(s);
		stockService.deleteStock(savedStock.getIdStock());
		assertNull(stockService.retrieveStock(savedStock.getIdStock()));
	}

}
