This is simple SpringBoot project with trottle control

    -Java version: 1.8
    
    -Dependency: bucket4j-core (used to limiting the calls to onMessage method)
    
To run the project

    -clone the project to a local dir
    
    -cd to the dir
    
    -install dependencywith command: (Windows)"mvnw.cmd clean install" / (Mac) "mvnw clean install"
    
    -start application with command: (Windows)"mvnw.cmd spring-boot:run" / (Mac) "mvnw spring-boot:run" 
    

example to submit a marketdata via Postman:

method: post
url: localhost:8080/api/v1/marketData
{
    "bid":100,
    "ask":100,
    "last": 100,
    "symbol": "btc",
    "updateTime": "2022-06-06T16:17:27.904"
}
