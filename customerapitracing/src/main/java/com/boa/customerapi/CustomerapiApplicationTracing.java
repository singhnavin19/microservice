package com.boa.customerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;

@SpringBootApplication
public class CustomerapiApplicationTracing {

	public static void main(String[] args) {
		SpringApplication.run(CustomerapiApplicationTracing.class, args);
	}
	 @Bean
	    public static JaegerTracer getTracer() {
	        Configuration.SamplerConfiguration samplerConfig = 
	        		Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
	        Configuration.ReporterConfiguration reporterConfig = 
	        		Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
	Configuration config = 
			new Configuration("customerservice")
			.withSampler(samplerConfig).withReporter(reporterConfig);


	        return config.getTracer();

	    }

}
