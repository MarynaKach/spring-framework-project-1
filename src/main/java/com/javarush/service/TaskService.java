package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TaskService {
    private TaskDAO taskDAO;
    @Autowired
    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAll(int offset, int limit) {
        return taskDAO.getAll(offset, limit);
    }
    public int getAllCount() {
        // нужно для пейджинга
        return taskDAO.getAllCount();
    }

    @Transactional
    public Task edit(int id, String description, Status status) {
        Task task = taskDAO.getById(id);
        if(task == null){
            throw new RuntimeException("Not found");
        }
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
        return task;
    }
    public Task create(String description, Status status) {
        Task task = new Task();
        task.setStatus(status);
        task.setDescription(description);
        taskDAO.saveOrUpdate(task);
        return task;
    }
    @Transactional
    public void delete(int id) {
        Task task = taskDAO.getById(id);
        if(task == null){
            throw new RuntimeException("Not found");
        }
        taskDAO.delete(task);
    }
}
