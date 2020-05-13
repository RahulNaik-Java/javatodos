package com.rahulnaik.todo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.rahulnaik.todo.model.ToDo;


public interface ToDoRepository extends MongoRepository<ToDo, String>{

}
