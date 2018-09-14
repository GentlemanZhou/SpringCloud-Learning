# Spring Cloud（四）配置中心Config

在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库以及数据库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。

在本文中，我们将分别构建基于Git、基于Mysql 数据库存储的分布式配置中心，并对客户端进行改造，并让其能够从配置中心获取配置信息并绑定到代码中的整个过程。



## 基于GIT存储

### 理解配置中心搜索路径

```
配置信息的URL与配置文件的映射关系如下：

- /{application}/{profile}[/{label}]
- /{application}-{profile}.yml
- /{label}/{application}-{profile}.yml
- /{application}-{profile}.properties
- /{label}/{application}-{profile}.properties
```

### 准备配置仓库

在这里，我在 Gitee 上创建了一个公开的 repo,  [config-repo-demo](https://gitee.com/mrzhouy/config-repo-demo) 里面创建 `mz-eureka-client-one` 的文件夹，存放  `mz-eureka-client-one` 项目的配置文件。地址：https://gitee.com/mrzhouy/config-repo-demo

### 构建 Server 端

创建一个 SpringBoot 的基础项目 `mz-config-server-git` ,同时引入 config 的依赖 spring-cloud-config-server

POM 文件如下：

```xml

```

创建项目配置文件 `bootstarp.yml` , 配置如下：

```properties

```

spring clour config git 属性解释：

```properties
uri: 仓库地址
username:访问账号（私库才需要输入）
password:账号密码（私库才需要输入）
searchPaths:git仓库子目录
```

当然，在某些时候我们希望对不同的子项目访问不同的git仓目录,因此我们可以在使用如下配置:

```properties
searchPaths: '{application}'

注：config 客户端在没有 spring.cloud.config.name属性的时候，服务端{application} 获取的是客户端 
spring.application.name的值，否则，获取的是 spring.cloud.config.name的值。 
1）、当没有spring.cloud.config.name时，客户端获取的是spring.application.name 所对应的git库中的文件，并且只能获取一个文件， 
2）、当一个项目中有需求要获取多个文件时，就需要用到spring.cloud.config.name这个属性，以逗号分割
```

#### 开启服务

启动类上添加 @EnableConfigServer 注解开启 config server 服务。代码如下：

```java
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ConfigServerApplication.class);
    }
}
```

### 升级客户端（client）

客户端这里，我们直接在之前的 `mz-eureka-client-one` 项目中进行升级。

添加config依赖：

```x&#39;m&#39;l
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

修改配置文件  `bootstarp.yml` 添加 config 配置：

```properties
spring:
    cloud:
        config:
            discovery:
                enabled: true
                service-id: mz-config-server-git
                #uri: http://localhost:8888/
            label: master
            profile: test
    
#注：如果config server 没有注入到注册中心，我们在这里可以通过 uri 来指定配置中心地址。但是生产、测试、开发环境中，我的配置中心是多个，因此我是将配置中心注册到了注册中心，通过 service-id 来访问注册中心的。
```

改造 HelloController:

```java
@RestController
public class HelloController {
    @Value("${server.port}")
    private String serverPort;
    @Value("${info.message}")
    private String infoMessage;
    @RequestMapping("hi")
    public String sayHi() {
        return "Hi Spring Cloud, running in port :" + serverPort + "     info.message is : " + infoMessage;
    }
}
```

至此，配置中心已经构建完成，按顺序启动 注册中心、配置中心 、客户端。  调用 http://localhost:2001/hi ，我们可以看到通过配置中心拿到的  info.message 这个值。



基于 MySql 存储

前面我们已经完成了基于 GIT 存储的配置中心，其实升级为mysql存储很简单。只需要修改 POM 依赖（引入数据库依赖），以及 bootstarp.yml 属性文件即可。在这里，我们创建新的项目  `mz-config-server-mysql` ,POM 文件如下： 

```xml
<parent>
        <artifactId>SpringCloud-Learning</artifactId>
        <groupId>com.mz</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>mz-config-server-mysql</artifactId>
    <description>Spring Cloud 配置中心——MySql</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <!-- 引入数据库依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.21</version>
        </dependency>
    </dependencies>
```

修改   `bootstarp.yml`  ：

指定 spring.profiles.active = jdbc,然后增加 jdbc配置

```properties
eureka:
    client:
        serviceUrl:
            defaultZone: http://localhost:1001/eureka/
server:
    port: 1003
spring:
    application:
        name: mz-config-server-mysql
    profiles:
        active: jdbc
    cloud:
        config:
            label: master
            server:
                jdbc:
                    sql: SELECT `KEY`, `VALUE` from PROPERTIES where APPLICATION=? and PROFILE=? and LABEL=?
    datasource:
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/db_microserver_config
        username: root
        password: TW123

```

创建数据库的sql 文件在项目中：ConfigMySql.sql

至此，修改完成，按顺序启动 注册中心、配置中心 、客户端(记得修改配置中心的serverId哦)。  调用 http://localhost:2001/hi ，我们可以看到通过配置中心拿到的  info.message 这个值。完结撒花.......