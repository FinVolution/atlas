package com.ppdai.atlas.dto.query;

import lombok.Data;

@Data
public class SpecTypeQuery {
    private String name;
    private Float cpu;
    private Float memory;
    private Float disk;
}
