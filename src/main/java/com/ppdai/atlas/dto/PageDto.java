package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.List;


@Data
public class PageDto<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private int numberOfElements;
    private int size;
    private int number;
}
