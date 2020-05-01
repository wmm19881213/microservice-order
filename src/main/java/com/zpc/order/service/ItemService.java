package com.zpc.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zpc.order.entity.Item;
import com.zpc.order.feign.ItemFeignClient;
import com.zpc.order.properties.OrderProperties;

@Service
public class ItemService {
	//Spring 框架Restful方式的http请求做了封装，来简化操作
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ItemFeignClient itemFeignClient;
	
	/*@Value("${myspcloud.item.url}")   
	private String itemUrl;*/
/*	@Autowired
	private OrderProperties orderProperties;*/
	
	public Item queryItemById(Long id){
		String itemUrl = "http://app-item/item/{id}";
		
//		return this.restTemplate.getForObject("http://127.0.0.1:8081/item/"+id, Item.class);
//		return this.restTemplate.getForObject(orderProperties.getItem().getUrl()+id, Item.class);
		// 该方法走eureka注册中心调用(去注册中心根据app-item查找服务，这种方式必须先开启负载均衡@LoadBalanced)
		return this.restTemplate.getForObject(itemUrl, Item.class,id);
	}
	/**
	 * 进行容错处理
	 * fallbackMethod的方法参数个数类型要和原方法一致
	 * @param id
	 * @return
	 */
//	@HystrixCommand(fallbackMethod="queryItemByIdFallbackMethod")
	public Item queryItemById3(Long id){
		String itemUrl = "http://app-item/item/{id}";
		
//		return this.restTemplate.getForObject("http://127.0.0.1:8081/item/"+id, Item.class);
//		return this.restTemplate.getForObject(orderProperties.getItem().getUrl()+id, Item.class);
		// 该方法走eureka注册中心调用(去注册中心根据app-item查找服务，这种方式必须先开启负载均衡@LoadBalanced)
//		return this.restTemplate.getForObject(itemUrl, Item.class,id);
		return this.itemFeignClient.queryItemById(id);//是用Feignin调用rest
	}
	
	/**
	 * 请求失败执行的方法
	 * fallbackMethod的方法参数个数类型要和原方法一致
	 * @param id
	 * @return
	 */
	public Item queryItemByIdFallbackMethod(Long id){
		return new Item(id,"查询商品信息出错！",null,null,null);
	}
	
}
