package webSpring.repository;

import org.springframework.data.repository.CrudRepository;
import webSpring.entity.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

}