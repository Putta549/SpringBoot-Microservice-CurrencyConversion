package com.microservices.currencyconversion;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	
	@GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean currencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		//CurrencyConversionBean bean = new CurrencyConversionBean(100L,from,to,quantity,BigDecimal.valueOf(75L),
			//	quantity.multiply(BigDecimal.valueOf(75L)),0);
		
		RestTemplate restTemplate = new RestTemplate();
		String serviceURL = "http://localhost:9000/currency-exchange/from/{from}/to/{to}";
		
		Map<String, String> uriMap =  new HashMap<String, String>();
		uriMap.put("from", from);
		uriMap.put("to", to);
		
		ResponseEntity<CurrencyConversionBean> forEntity = restTemplate.getForEntity(serviceURL, CurrencyConversionBean.class
				,uriMap);
		CurrencyConversionBean bean = forEntity.getBody();
		
		return new CurrencyConversionBean(bean.getId(),from,to,bean.getPrice(),quantity, quantity.multiply(bean.getPrice()), bean.getPort());
		
	}
	@GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean currencyConversion_feign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		//CurrencyConversionBean bean = new CurrencyConversionBean(100L,from,to,quantity,BigDecimal.valueOf(75L),
			//	quantity.multiply(BigDecimal.valueOf(75L)),0);
		
		CurrencyConversionBean bean = proxy.retreiveExchangeValue(from, to);
		
		return new CurrencyConversionBean(bean.getId(),from,to,bean.getPrice(),quantity, quantity.multiply(bean.getPrice()), bean.getPort());
		
	}

}
