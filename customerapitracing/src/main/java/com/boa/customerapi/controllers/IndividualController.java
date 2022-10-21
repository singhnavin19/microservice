package com.boa.customerapi.controllers;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boa.customerapi.models.Individual;
import com.boa.customerapi.services.IndividualService;
import com.boa.customerapi.vos.ResponseWrapper;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

@RestController
@RequestMapping("/individuals")
public class IndividualController {
    @Autowired 
	private IndividualService individualService;
    private static final Logger LOGGER = LogManager.getLogger(IndividualController.class);
    @Autowired
    private Tracer tracer;
    
    private HttpStatus httpStatus;

    @PostMapping({"/v1.0/"})
    public ResponseEntity<ResponseWrapper> addIndividual(@RequestBody Individual individual){
    	
    	Individual individualObj=this.individualService.addIndividual(individual);
    	if(individualObj!=null) {
    		return ResponseEntity.status(HttpStatus.CREATED)
    				.body(new ResponseWrapper(individualObj));
    	}
    	else
    	{
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    				.body(new ResponseWrapper("Individual Input Not Correct"));
    	}
    }
    @GetMapping({"/v1.0/"})
    public List<Individual> getIndividuals(){
    	
    	List<Individual> individuals=this.individualService.getAllIndividuals();
    	LOGGER.info(LocalDate.now()+"Fetching Data");
    	for(Individual individual : individuals)
    		LOGGER.info(individual);
        LOGGER.debug("Debug level log message");
        LOGGER.error("Error level log message");



        Tracer tracer = GlobalTracer.get();
        Tracer.SpanBuilder spanBuilder = tracer.buildSpan("CustomerSpan")
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER);
         
        Span span1 = spanBuilder.start();
        Tags.COMPONENT.set(span1, "IndividualAController");
        span1.setTag("testtag", "test");
        span1.finish();
        Span span = tracer.buildSpan("accessing customers").start();
        
      
        if (individuals.size()>0) {
            httpStatus = HttpStatus.CREATED;
            span.setTag("http.status_code", 201);
        } else {
            span.setTag("http.status_code", 403);
        }
        span.finish();

		return individuals;

    }
    
    @PutMapping({"/v1.0/{customerId}"})
    public ResponseEntity<ResponseWrapper> updateIndividual(@PathVariable("customerId") long customerId,
    		@RequestParam(name = "email") String email, 
			@RequestParam(name="password") String password){
    	
    	Individual individualObj=this.individualService.updateIndividual(customerId, password, email);
    	if(individualObj!=null) {
    		return ResponseEntity.status(HttpStatus.CREATED)
    				.body(new ResponseWrapper(individualObj));
    	}
    	else
    	{
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    				.body(new ResponseWrapper("Individual Input Not Correct"));
    	}
    }
    
    @GetMapping({"/v1.0/{customerId}"})
    public ResponseEntity<ResponseWrapper> getIndividualById(@PathVariable("customerId") 
          long customerId){
    	
    	Individual individualObj=this.individualService.getIndividualById(customerId);
    	if(individualObj!=null) {
    		return ResponseEntity.status(HttpStatus.ACCEPTED)
    				.body(new ResponseWrapper(individualObj));
    	}
    	else
    	{
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    				.body(new ResponseWrapper("Individual Input Not Correct"));
    	}
    }
    @DeleteMapping({"/v1.0/{customerId}"})
    public ResponseEntity<ResponseWrapper> deleteIndividualById(@PathVariable("customerId") 
          long customerId){
    	
    	
    	if(this.individualService.deleteIndividualById(customerId)) {
    		return ResponseEntity.status(HttpStatus.ACCEPTED)
    				.body(new ResponseWrapper("Individual Customer-->"+customerId+"==>deleted"));
    	}
    	else
    	{
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    				.body(new ResponseWrapper("Individual Input Not Correct"));
    	}
    }
}
