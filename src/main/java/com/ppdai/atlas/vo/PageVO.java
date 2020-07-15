package com.ppdai.atlas.vo;

import lombok.Data;

import java.util.List;

/**
 * 分页
 *
 * @author huangyinhuang
 * @date 5/23/2018
 */
@Data
public class PageVO<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private int numberOfElements;
    private int size;
    private int number;

}