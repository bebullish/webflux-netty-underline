# webflux-netty-underline

## 仓库说明
可以使基于 Reactive-Netty 的 Spring WebFlux 应用，支持域名带下划线 HTTP 的请求。

## 代码改动
UnderlineReactorServerHttpRequest.class 的 resolveBaseUrl 方法。

## 如何引用

### 添加依赖
```groovy
repositories {
    maven {
        url 'https://marlon-maven.pkg.coding.net/repository/artifact/public/'
    }
}

dependencies {
    implementation 'cn.bebullish:webflux-netty-underline:1.0.0'
}
```

### 使配置生效
```java
@SpringBootApplication
@EnableUnderlineNettyContainer
```