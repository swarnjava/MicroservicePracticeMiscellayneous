package com.howtodoinjava.example.async.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.howtodoinjava.example.async.model.EmployeeAddress;
import com.howtodoinjava.example.async.model.EmployeeAddresses;
import com.howtodoinjava.example.async.model.EmployeeNames;
import com.howtodoinjava.example.async.model.EmployeePhone;
import com.howtodoinjava.example.async.service.AsyncService;

import ch.qos.logback.core.net.SyslogOutputStream;

@RestController
public class AsyncController {

	private static Logger log = LoggerFactory.getLogger(AsyncController.class);

	@Autowired
	private AsyncService service;

	@RequestMapping(value = "/testAsynch", method = RequestMethod.GET)
	public void testAsynch() throws InterruptedException, ExecutionException 
	{
		log.info("testAsynch Start");

		CompletableFuture<EmployeeAddresses> employeeAddress = service.getEmployeeAddress();
		CompletableFuture<EmployeeNames> employeeName = service.getEmployeeName();
		CompletableFuture<EmployeePhone> employeePhone = service.getEmployeePhone();
		
		
		CompletableFuture<List<EmployeeAddress>> employeeAddress2=CompletableFuture
				                                              .supplyAsync(()->{
				                                            	  try
				                                            	  {
				                                            		  Thread.sleep(1000);
				                                            		  return service.getEmployeeAddress();
				                                            	  }
				                                            	  catch(InterruptedException e)
				                                            	  {
				                                            		  e.printStackTrace();
				                                            	  }
																return null;
				                                            	  
				                                            	  })
				                                              .thenApplyAsync(empaddressList->{
				                                            	  try {
																	return service.getSpecificAddressList(empaddressList.get().employeeAddressList);
																} catch (InterruptedException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																} catch (ExecutionException e) {
																	// TODO Auto-generated catch block
																	e.printStackTrace();
																}
				                                            	  
				                                            	  return null;
				                                            	  
				                                              });

		// Wait until they are all done
		CompletableFuture.allOf(employeeAddress, employeeName, employeePhone).join();
		
		log.info("EmployeeAddress--> " + employeeAddress.get());
		log.info("EmployeeName--> " + employeeName.get());
		log.info("EmployeePhone--> " + employeePhone.get());
		
		
		List<EmployeeAddress> empspecificaddressList=employeeAddress2.get();
		
		empspecificaddressList.forEach(e->System.out.print("Specific Address: "+e.getHouseNo()+" / "+e.getStreetNo()+" / "+e.getZipCode()));
		
	}
	
	
	
}
