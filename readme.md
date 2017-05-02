## swagger与soa服务的结合

### swagger简介
Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。总体目标是使客户端和文件系统作为服务器以同样的速度来更新。文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。
目前官方针对的是spring mvc生成在线api文档的。公司目前主要服务都是提供SOA服务，并通过pizza转化为web服务。所以需要在spring服务层提供swagger的扫描功能。




