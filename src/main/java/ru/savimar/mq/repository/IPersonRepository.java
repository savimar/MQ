package ru.savimar.mq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.savimar.mq.entity.Person;

@Repository
public interface IPersonRepository extends CrudRepository<Person, Long> {
}
