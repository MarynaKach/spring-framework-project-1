package com.javarush.controller;

import com.javarush.domain.Status;
import lombok.Data;

@Data
public class TaskInfo {
    private String description;
    private Status status;
}
