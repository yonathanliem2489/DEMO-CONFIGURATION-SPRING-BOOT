package demo.configuration.service.persist.config;


import com.mongodb.ConnectionString;
import com.mongodb.WriteConcern;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import java.net.InetSocketAddress;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@TestConfiguration
public class MongoServerConfiguration {

    @Bean
    ReactiveMongoTemplate reactiveMongoTemplate(
        ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory) {
        ReactiveMongoTemplate reactiveMongoTemplate =
            new ReactiveMongoTemplate(reactiveMongoDatabaseFactory);
        reactiveMongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return reactiveMongoTemplate;
    }

    @Bean
    ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(MongoServer mongoServer) {
        InetSocketAddress serverAddress = mongoServer.getLocalAddress();
        return new SimpleReactiveMongoDatabaseFactory(new ConnectionString(
            "mongodb://" + serverAddress.getHostName() + ":" + serverAddress.getPort() + "/test-mongo"));
    }

    @Bean
    MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory);
        mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return mongoTemplate;
    }

    @Bean
    MongoDatabaseFactory mongoDatabaseFactory(MongoServer mongoServer) {
        InetSocketAddress serverAddress = mongoServer.getLocalAddress();
        return new SimpleMongoClientDatabaseFactory(new ConnectionString(
            "mongodb://" + serverAddress.getHostName() + ":" + serverAddress.getPort() + "/test-mongo"));
    }

    @Bean(destroyMethod = "shutdown")
    public MongoServer mongoServer() {
        MongoServer mongoServer = new MongoServer(new MemoryBackend());
        mongoServer.bind();
        return mongoServer;
    }
}
