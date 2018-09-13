# Spring Cloud（三）服务间调用Feign

在Spring Cloud Netflix栈中，各个微服务都是以HTTP接口的形式暴露自身服务的，因此在调用远程服务时就必须使用HTTP客户端。我们可以使用JDK原生的`URLConnection`、Apache的`Http Client`、Netty的异步HTTP Client, Spring的`RestTemplate`。但是，用起来最方便、最优雅的还是要属Feign了。

## Feiegn

Feign是一个声明式的REST客户端，它的目的就是让REST调用更加简单。

Feign提供了HTTP请求的模板，通过编写简单的接口和插入注解，就可以定义好HTTP请求的参数、格式、地址等信息。

而Feign则会完全代理HTTP请求，我们只需要像调用方法一样调用它就可以完成服务请求及相关处理。

SpringCloud对Feign进行了封装，使其支持SpringMVC标准注解和HttpMessageConverters。

Feign可以与Eureka和Ribbon组合使用以支持负载均衡。

### 准备工作：

	启动教程一服务注册与发现中的 mz-eureka-server 和 mz-eureka-client-one 

​	修改  mz-eureka-client-one 端口再次启动一个服务提供方。

	此时服务 application.name=mz-eureka-client-one 这个服务就有两个服务单元同时运行，可以在EurekaServer 中查看



### 服务引入 Feign

在这里，我们就不创建新项目了，直接在之前 `mz-eureka-consumer-ribbon` 项目上进行改造：

添加 Feign 依赖：

```xml
	<dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```

在应用主类中，通过`@EnableFeignClients`注解开启扫描Spring Cloud Feign客户端的功能。

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RibbonApplication {
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }
}
```

创建 `FeignClientDemo`  interface，添加 @FeignClient 标注要调用 的服务 service-id

```java
@Component
@FeignClient(value = "mz-eureka-client-one")
public interface FeignClientDemo {
    @GetMapping("/hi")
    String sayHi();
}
```

创建`HiController`来消费  `mz-eureka-client-one` 的hi服务。通过直接Feign来调用服务。

```java
@RestController
public class HiController {
    @Autowired
    FeignClientDemo feignClientDemo;

    @GetMapping("/hello")
    public String hello() {
        return feignClientDemo.sayHi();
    }
}
```

多次访问：<http://localhost:1002/hello> 能够看出每次调用的是不同端口的  `mz-eureka-client-one` 

到这里，我们已经通过Feign + Ribbon在客户端已经实现了对服务调用的均衡负载。

### Feign 之参数传递

```java
 	@RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET)
    AdvertGroupVO findByGroupId(@PathVariable("groupId") Integer adGroupId);

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.PUT)
    void update(@PathVariable("groupId") Integer groupId, @RequestParam("groupName") String groupName);

	@PostMapping("/group")
    String sayHi(@RequestBody String group);
```

