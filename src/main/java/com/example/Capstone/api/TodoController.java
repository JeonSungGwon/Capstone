package com.example.Capstone.api;

import com.example.Capstone.dto.TodoDTO;
import com.example.Capstone.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
@Api(tags = "Todo List")
public class TodoController {
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @Operation(summary = "모든 Todo List 불러오기")
    public List<TodoDTO> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping
    @Operation(summary = "Todo List 생성")
    public TodoDTO createTodo(@RequestBody TodoDTO todoDTO) {
        return todoService.createTodo(todoDTO);
    }


    @PatchMapping("/{id}")
    @Operation(summary = "Todo List 수정")
    public TodoDTO updateTodo(@PathVariable Long id, @RequestBody TodoDTO todoDTO) {
        return todoService.updateTodo(id, todoDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "모든 Todo List 삭제")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}

