package demo.configuration.service.persist;

import demo.configuration.service.model.entity.Directory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DirectoryRepository extends ReactiveMongoRepository<Directory, String> {

}
