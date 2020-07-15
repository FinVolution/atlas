package com.ppdai.atlas.dto;

import java.util.List;

public class DasPage<T> {
    private long totalElements;
    private List<T> elements;

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }
}
