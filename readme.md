## swagger与soa服务的结合

### swagger简介
Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。总体目标是使客户端和文件系统作为服务器以同样的速度来更新。文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。
目前官方针对的是spring mvc生成在线api文档的。公司目前主要服务都是提供SOA服务，并通过pizza转化为web服务。所以需要在spring服务层提供swagger的扫描功能。



### 原理

+ handlerMappings不再由spring mvc提供，通过SwaggerBeanDefinitionRegistryPostProcessor来扫描类和注解信息，并将接口信息与URL关联起来；
+ 通过注解类EnableSwagger3，启动swagger框架；
+ DocumentationSoaPluginsBootstrapper监听spring容器的启动，结合spring的特性进行接口类扫描
+ 文档元数据信息都存放在DocumentationCache中
+ Swagger3ServiceImpl提供接口用于解析DocumentationCache，以便获取对应格式的数据，本次提供了两种格式：swagger原生支持的格式 和公司pizza系统所支持的格式


### 优点
+  可以定制化扫描接口文档
+ 其他优点参考swagger的优点


### 缺点
+  swagger需要学习成本
+  有代码侵入