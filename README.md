# SpringCloud-Learning

## 项目介绍
一个 SpringCloud 的学习项目

## 版本说明

	SpringBoot        	2.0.4.RELEASE

	SpringCloud		Finchley.SR1

## 一、什么是 Spring Cloud

	相信各位能够看到本文的都对Spring Cloud 有了基础的理解，在这里就不再赘述了（ps:因为本人不爱看那种长篇的介绍），如果还不太了解的话，推荐各位跳转：
	
	[简述 Spring Cloud 是什么](https://blog.csdn.net/kkkloveyou/article/details/79210420)

	[Spring Cloud都做了些什么](https://blog.csdn.net/zhangweiwei2020/article/details/78672814)

	

## 二、准备工作

	本文依赖与 SpringBoot（2.0.4.RELEASE）、SpringCloud（Finchley.SR1），为了方便起见，构建一个父项目供后面的各个模块使用。主要 pom 依赖如下：

```xml
<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
        <spring-boot.version>2.0.4.RELEASE</spring-boot.version>
        <javadoc.version>3.0.0</javadoc.version>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <repositories><!-- Maven库 -->
        <repository>
            <id>maven-ali</id>
            <url>http://maven.aliyun.com/nexus/content/groups/public//</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
                <checksumPolicy>fail</checksumPolicy>
            </snapshots>
        </repository>
    </repositories>
```



至此，准备完成，开始构建 Spring Cloud 的各个模块吧！