package com.rahulnaik.todo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahulnaik.todo.exceptions.ResourceNotFoundException;
import com.rahulnaik.todo.model.ToDo;
import com.rahulnaik.todo.repository.ToDoRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class ToDoController {

	@Autowired
	ToDoRepository todoRepository;
	
	@GetMapping("/todos")
    public List<ToDo> getAllTodos() {
        return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
	
	@PostMapping("/todos")
	public ToDo createTodo(@Valid @RequestBody ToDo todo) {
		
		todo.setCompleted(false);
		return todoRepository.save(todo);
	}
	
	@GetMapping(value="/todos/{id}")
    public ResponseEntity<ToDo> getTodoById(@PathVariable("id") String id) {
        return todoRepository.findById(id)
                .map(todo -> ResponseEntity.ok().body(todo))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value="/todos/{id}")
    public ResponseEntity<ToDo> updateTodo(@PathVariable("id") String id, @Valid @RequestBody ToDo todo) throws ResourceNotFoundException {
        
    	ToDo todoUpdate = todoRepository.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + id));
    	todoUpdate.setId(todo.getId());
    	todoUpdate.setTitle(todo.getTitle());
    	todoUpdate.setCompleted(todo.getCompleted());
    	todoUpdate.setCreatedAt(todo.getCreatedAt());
    	
    	todoRepository.save(todoUpdate);
    	 
    	return ResponseEntity.ok().body(todoUpdate);
    	
    }

    @DeleteMapping(value="/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
