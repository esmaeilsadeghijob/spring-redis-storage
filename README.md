# redis-auth-cache
Spring Redis Authentication Store And Caching

* Running redis on Docker

`docker run -p 6379:6379 --name some-redis -d redis`

* Redis with persistence

`docker run -p 6379:6379 --name some-redis -d redis redis-server --appendonly yes`


**RedisConnection and RedisConnectionFactory**

Jedis is used as Redis java client. Lettuce is another alternative that is supported by Spring. 
Connection to redis is provided by RedisConnection, and active RedisConnection objects are created through RedisConnectionFactory.


* Configure Jedis Connector with default settings 

```java
@Configuration
class AppConfig {

  @Bean
  public JedisConnectionFactory redisConnectionFactory() {
    return new JedisConnectionFactory();
  }
}
```

* Configure Jedis Connector with custom config

```java
@Configuration
class AppConfig {

  @Bean
      JedisConnectionFactory jedisConnectionFactory() {
  
          RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
  
          redisStandaloneConfiguration.setHostName("localhost");
          redisStandaloneConfiguration.setPort(6379);
          redisStandaloneConfiguration.setDatabase(0);
          redisStandaloneConfiguration.setPassword(RedisPassword.none());
  
          return new JedisConnectionFactory(redisStandaloneConfiguration);
      }

}
```
**RedisTemplate and working with objects**

RedisTemplate offers a high-level abstraction for Redis interactions. 
It provides interfaces as HashOperations, ListOperations, SetOperations, ValueOperations etc. to manage Redis operations.

* Configure RedisTemplate

```java
@Configuration
class AppConfig {

  @Bean
      public RedisTemplate<String, Object> redisTemplate() {
          RedisTemplate<String, Object> template = new RedisTemplate<>();
          template.setConnectionFactory(jedisConnectionFactory());
          return template;
      }

}
```

* Redis commands via RedisTemplate

After accessing redisTemplate (autowire), it is possible to use its operations that matches redis commands.

`redisTemplate.opsForValue().set("myKey", "myValue");`

One thing to note here is that by default **RedisTemplate** uses java serialization, you may see keys look a little weird if you reach them via **redis-cli**, for example:

`"\xac\xed\x00\x05t\x00\x03myKey"`

When we are retrieving via redisTemplate it will deserialize it in same manner so there wont be problem. But if you like to change this check out changing default serialization:

https://stackoverflow.com/questions/13215024/why-is-data-getting-stored-with-weird-keys-in-redis-when-using-jedis-with-spring?lq=1


**Redis as Spring Session Storage**

Adding the following dependency handles auto-configuration of spring session storage as redis.

```
<dependency>
	<groupId>org.springframework.session</groupId>
	<artifactId>spring-session-data-redis</artifactId>
</dependency>
```

Having this dependency and a working redis client (JedisConnection) that manages connections to server 
will be enough to automatically add session filter to security chain with redis configurations. Example session stored:
```
127.0.0.1:6379> KEYS *
1) "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:user"
2) "spring:session:sessions:6c39c36c-25e2-4412-8ae8-770ea6d39fac"
3) "spring:session:expirations:1561624560000"
4) "spring:session:sessions:expires:6c39c36c-25e2-4412-8ae8-770ea6d39fac"
```

Note here that we created our redis connector as bean with the given configuration above. 
Another way to create redis connection instance can be with auto configuration. we could say spring-data-redis 
to configure and start redis connection for us with adding following line in application.properties file:

`spring.session.store-type=redis`

So just having dependency and this parameter in application.properties is enough to generate redis instance and store session data in redis.

Additional possible configuration for application.properties

```
spring.redis.host=localhost # Redis server host.
spring.redis.password= # Login password of the redis server.
spring.redis.port=6379 # Redis server port.
```
**Redis repositories and using Redis as primary storage**

Redis Repositories let us easily convert java objects to RedisHash and store in Redis server.

```java
@RedisHash("driver")
public class Driver {

    @Id
    private long id;
    private String name;
    private String surname;
    
}
```
`@RedisHash` marks entities to be stored in redis hashes. `@Id` annotation (`org.springframework.data.annotation.Id`) is 
used together with @RedisHash to generate **key**.

Note that our repo is basic crud repo
```java
@Repository
public interface DriverRepo extends CrudRepository<Driver,Long> {
}

```
Now if we insert some driver it will be stored in redis

```java
Driver driver = new Driver(2L, "cemal", "turkoglu");
driverRepo.save(driver);
```

query in redis server:

```

127.0.0.1:6379> HGETALL driver:2
1) "_class"
2) "com.redis.authCache.entity.Driver"
3) "id"
4) "2"
5) "name"
6) "cemal"
7) "surname"
8) "turkoglu"

```

For the complex object which has stored another object, mapping is implemented with dot path.
For a vehicle object having driver object as property:

```java
@RedisHash("vehicle")
public class Vehicle {

    @Id
    private long id;

    private String numberPlate;
    private String make;
    private String model;
    private Driver driver;
```

if we insert a vehicle

```java
Driver driver = new Driver(2L, "cemal", "turkoglu");
Vehicle vehicle = new Vehicle(1L, "34 XA 102", "opel", "astra",driver);
vehicleRepo.save(vehicle);
```

we see that driver is inserted with (.)

```
127.0.0.1:6379> HGETALL vehicle:1
 1) "_class"
 2) "com.redis.authCache.entity.Vehicle"
 3) "id"
 4) "1"
 5) "numberPlate"
 6) "34 XA 102"
 7) "make"
 8) "opel"
 9) "model"
10) "astra"
11) "driver.id"
12) "2"
13) "driver.name"
14) "cemal"
15) "driver.surname"
16) "turkoglu"
```

By this way we can use Redis as primary storage, store our data and retrieve with same CrudRepository functions as save,findById etc.

For instance, running `vehicleRepo.findAll()` command retrieves the following json

```json
[
   {
      "id":1,
      "numberPlate":"34 XA 102",
      "make":"opel",
      "model":"astra",
      "driver":{
         "id":2,
         "name":"cemal",
         "surname":"turkoglu"
      }
   }
]

```


**Caching with Redis**

Initialization:

* We need to add `@EnableCaching` annotation.

* spring-boot-starter-data-redis

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

* Redis connection configuration. As mentioned above with redisConnectionFactory and redisTemplate beans can be created and it is all needed. 
Also another way to create is via application.properties, redisAutoConfiguration will handle creating beans.

* Setting redis as cache store. We can set this in our config as follows:

```java
@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisCacheManager cacheManager() {
        return RedisCacheManager.create(jedisConnectionFactory);
    }
}
```

Second way is with auto configuration via adding the following parameter in application.properties:

`spring.cache.type=redis`

Caching logic:

@Cachable annotation indicates that the result of invoking a method (or all methods * in a class) can be cached.

if we simply add to a method:

```java
@Cacheable("data")
public String getSomeData(){
    logger.info("accessing the data, NOT FROM CACHE");
    return "someData";
}

```

and if we call the method multiple times

```java
cacheableResourceExample.getSomeData();
cacheableResourceExample.getSomeData();
cacheableResourceExample.getSomeData();
        
```

if we did not have @EnableCaching annotation it would not be cached and result would be

```java
accessing the data, NOT FROM CACHE
accessing the data, NOT FROM CACHE
accessing the data, NOT FROM CACHE
```

but thanks to caching first time the function will be executed and later on result will be retrieved from cache
we see the function is executed once:

```java
accessing the data, NOT FROM CACHE
```

and if we check redis:

```
127.0.0.1:6379> keys *
1) "data::SimpleKey []"
127.0.0.1:6379> get "data::SimpleKey []"
"\xac\xed\x00\x05t\x00\bsomeData"
```

we see our data is stored.

