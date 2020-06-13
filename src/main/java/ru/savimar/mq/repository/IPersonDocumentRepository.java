package ru.savimar.mq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.savimar.mq.entity.PersonDocument;

@Repository
public interface IPersonDocumentRepository extends CrudRepository<PersonDocument, Long> {
}
