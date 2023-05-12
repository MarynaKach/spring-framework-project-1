package com.javarush.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(schema = "todo", name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
