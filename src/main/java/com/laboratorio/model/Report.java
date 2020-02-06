package com.laboratorio.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Report {

    private Integer idReport;
    private String fileName;
    private String title;
    private String description;
    private String dataSource;
    private Date createdAt;
    private Date updatedAt;
}
