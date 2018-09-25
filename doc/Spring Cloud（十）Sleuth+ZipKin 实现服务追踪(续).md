# Spring Cloud（十）Sleuth+ZipKin 实现服务追踪（续）

注：本文Spring Cloud 版本 Finchley.SR1









本节是对上一篇的延续，没有查看上一篇的请先查看 Spring Cloud（九）Sleuth+ZipKin 实现服务追踪

相信细心的网友也发现了，以上的配置方式使用的是 http 发送链路数据，并且保存在缓存中，所以也只能当个 demo,自己玩玩，不适合应用在生产环境中。所以我们的测试环境、生产环境项目需要在之前的基础上升级。通过上一步的构建我们应该清楚，在生产中使用，我们需要考虑的是链路请求数据的传输（如何发送到 zipkin server ）以及 zipkin server 将数据的保存在哪里。解决了这两个问题我们就可以放心的在生产环境中使用该功能。

**链路数据的传输及保存方式：** 其实我们查看 zipkin-server 的依赖就可以知道， zipkin 对于数据传输，支持使用消息队列 kafka 以及  rabbitmq，数据存储支持 cassandra、elasticsearch、mysql 

接下来我们对之前的项目进行改造，使用kafka来传输链路数据，ElasticSearch 来存储数据，当然我们也可以选择 mysql,但是在使用一段时间后，随着数据积累，mysql 访问速度会大大降低，因此推荐使用 ElasticSearch。

## 升级 zipkin-server

#### 1、升级依赖

各位可以点进 zipkin-server 的pom里，看看官方都提供了什么依赖 在这里，我们需要引入如下依赖：

```xml
<!-- 使用 kafka 传输链路数据-->
<dependency>
     <groupId>io.zipkin.java</groupId>
     <artifactId>zipkin-autoconfigure-collector-kafka</artifactId>
     <version>${zipkin.version}</version>
</dependency>

<!-- 使用 ElasticSearch 存储数据-->
<dependency>
  <groupId>io.zipkin.java</groupId>
  <artifactId>zipkin-autoconfigure-storage-elasticsearch</artifactId>
  <version>${zipkin.version}</version>
</dependency>
```

#### 2、升级配置文件

添加如下配置：

```properties
zipkin:
    collector:
        kafka:
            bootstrap-servers: ip:port
            groupId: zipkin
            topic: zipkin
    storage:
        StorageComponent: elasticsearch
        elasticsearch:
            cluster: elasticsearch
            hosts: 192.168.0.222:9200
            index: zipkin
            index-replicas: 1
            index-shards: 5
        type: elasticsearch

```

## 升级 client

#### 1、POM 依赖

添加 kafka 依赖

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

#### 2、修改配置文件

配置链路数据传输方式为 kafka， 以及 kafka 的配置

```properties
spring.zipkin.sender.type=kafka
spring.sleuth.sampler.probability=1.0
spring.kafka.bootstrap-servers=ip:port
spring.zipkin.kafka.topic=zipkin
spring.zipkin.kafka.groupId=zipkin

#删除之前配置的 spring.zipkin.base-url=http://localhost:9411
```

## 测试

1、分别启动注册中心、Zuul 网关、ZipkinServer、以及上面的 client one  、client two

2、分别请求上面的三个接口产生链路数据

http://localhost:1004/mz-zipkin-client-one/rest

http://localhost:1004/mz-zipkin-client-one/feign

http://localhost:1004/mz-zipkin-client-one/fail

3、打开  <http://localhost:9411>  查看



完结撒花！！！！