package com.zpc.order.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages="com.zpc.order.feign") //增加Feign注解
@EnableHystrix
@ComponentScan(basePackages={"com.zpc.order.feign","com.zpc.order.fallback","com.zpc.order.controller","com.zpc.order.service","com.zpc.order.properties"})
public class OrderApp {

	public static void main(String[] args) {
		SpringApplication.run(OrderApp.class, args);

	}
	/**
	 * 向Spring容器中定义RestTemplate对象
	 * @return
	 */
	@Bean
	@LoadBalanced //（加入ribbon）负载均衡
	public RestTemplate restTemplate(){
//		return new RestTemplate();
		return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
	}
}
