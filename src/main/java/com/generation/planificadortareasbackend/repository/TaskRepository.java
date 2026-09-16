package com.generation.planificadortareasbackend.repository;

import com.generation.planificadortareasbackend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
