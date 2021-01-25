# DEMO-CONFIGURATION-SPRING-BOOT
This demo service is a way to separate configurations using @Configuration and Enable Auto Configuration in spring boot. How to setup unit test behavior based on type of configuration

Since spring-boot 1.2.0 @SpringBootApplication defined @ComponentScan, @EnableAutoConfiguration and @Configuration with their default attribute

## Setup configuration using @Configuration
 Indicates that a class declares one or more {@link Bean @Bean} methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime
 
 Example : 

```
@Configuration
public class AppConfig {

   @Bean
   public MyBean myBean() {
   // instantiate, configure and return bean ...
   }
 }
```


## Setup configuration using @EnableAutoConfiguration
  The @EnableAutoConfiguration annotation enables Spring Boot to auto-configure the application context. Therefore, it automatically creates and registers beans based on both the included jar files in the classpath and the beans defined by us.
  
  Example: 
  
  
```
@Configuration
public class AppConfig {

   @Bean
   public MyBean myBean() {
   // instantiate, configure and return bean ...
   }
 }
```

Create file spring.factories in folder resources/META-INF

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  AppConfig
```

## UNIT TEST
  Simple unit test configuration
  
  ### Unit Test Using @TestConfiguration
  
  1. First, Create Configuration
```
@Configuration
@EnableConfigurationProperties(HandlerProperties.class)
public class HandlerConfiguration {

  @Bean
  @ConditionalOnBean(value = UserCache.class)
  UserHandler userHandler(UserCache userCache, HandlerProperties properties) {
    return new DefaultUserHandler(userCache, properties);
  }

}
```
  
  2. Setup Simple Unit Test
```
@SpringBootConfiguration
@SpringBootTest(classes = TestingConfiguration.class, properties = {

})
@Import(SupportConfiguration.class)
public class UserHandlerTests {

  @Autowired
  private UserHandler userHandler;

  @MockBean
  private UserCache userCache;

  @Test
  void whenCreateUser_thenShouldSuccess() {

    Mockito.when(userCache.put(any()))
        .thenReturn(Mono.empty());

    StepVerifier.create(userHandler.create(UserRequest.builder().build()))
        .expectSubscription().thenAwait()
        .expectNextMatches(userResponse -> {

          return true;
        })
        .verifyComplete();
  }

  @TestConfiguration
  @EnableConfigurationProperties(HandlerProperties.class)
  static class SupportConfiguration {
    @Bean
    UserHandler userHandler(UserCache userCache, HandlerProperties properties) {
      return new DefaultUserHandler(userCache, properties);
    }
  }
}
```
  
  ### Unit Test Call To Class of Configuration
  
  1. First, Create Configuration
```
public class HandlerAutoConfiguration {

  @Bean
  AutoConfigurationHandlerA autoConfigurationService(ObjectProvider<UserCache> userCache) {
    return new DefaultAutoConfigurationHandlerA(userCache
        .getIfUnique(() -> new UserCache() {
          @Override
          public Mono<Void> put(User user) {
            return illegalAccess();
          }

          @Override
          public Mono<Boolean> exist(String name) {
            return illegalAccess();
          }

          @Override
          public Mono<User> retrieve(String name) {
            return illegalAccess();
          }
        }));
  }

  @Bean
  AutoConfigurationHandlerB autoConfigurationServiceB(
      ObjectProvider<DirectoryCache> directoryCache) {
    return new DefaultAutoConfigurationHandlerB(
        directoryCache.getIfUnique(() -> directory -> illegalAccess()));
  }

  private <T> Mono<T> illegalAccess() {
    return Mono.error(new IllegalAccessException());
  }
}
```

  2. Setup Simple Unit Test
```
@SpringBootTest(classes = TestingConfiguration.class, properties = {})
@ImportAutoConfiguration({
    HandlerAutoConfiguration.class
})
public class AutoConfigurationHandlerATests {

  @Autowired
  private AutoConfigurationHandlerA serviceA;

  @MockBean
  private UserCache userCache;

  @Test
  void whenHandle_thenShouldSuccess() {
    Mockito.when(userCache.put(User.builder().build()))
      .thenReturn(Mono.empty());

    StepVerifier.create(serviceA.handle())
        .expectSubscription().thenAwait()
        .verifyComplete();
  }

}
```
  
Important : This Demo service also implement Fake Mongo (Integration Test), Redis Test and Web Client Test
