package com.javarush.controller;

import com.javarush.domain.Task;
import com.javarush.service.TaskService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
@Data
public class TaskController {
    // controller should provide method which are needed for frontend
    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/")
    public String tasks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        List<Task> tasks = taskService.getAll((page-1)*limit, limit);
        // если страница 1, то 1-1*10 == 0, т.е. ничегое не пропустит, 2-1*10==10 пропустит первые 10
        // кодга получили задачи добавляем их в модель
        model.addAttribute("tasks", tasks);
        model.addAttribute("current_page", page);
        // Находим общее количесвто страниц
        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / limit);
        if (totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers",pageNumbers);
        }
        return "tasks";

    }
    @PostMapping("/{id}")
    public String edit(Model model,
                     @PathVariable Integer id,
                     @RequestBody TaskInfo info) {
        if (id == null || id <=0) {
            throw new RuntimeException("Invalid id");
        }
        //на слой сервиса отправить запрос на редактирование задачи

        Task task = taskService.edit(id, info.getDescription(), info.getStatus());
        return tasks(model, 1, 10);
    }
    @PostMapping("/")
    public String add(Model model,
                     @RequestBody TaskInfo info) {
        Task task = taskService.create(info.getDescription(), info.getStatus());
        return tasks(model, 1, 10);

    }
    @DeleteMapping("/{id}")
    public String delete(Model model,
                         @PathVariable Integer id) {
        if (id == null || id <=0) {
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);
        return tasks(model, 1, 10);
    }
}
