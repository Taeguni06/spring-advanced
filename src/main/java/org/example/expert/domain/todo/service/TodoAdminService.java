package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoAdminService {
    private final TodoRepository todoRepository;

    @Transactional
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}
