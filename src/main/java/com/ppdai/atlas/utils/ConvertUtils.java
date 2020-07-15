package com.ppdai.atlas.utils;

import com.ppdai.atlas.controller.response.MessageType;
import com.ppdai.atlas.exception.BaseException;
import com.ppdai.atlas.vo.PageVO;
import org.dozer.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * ConvertUtils
 *
 * @author yinzuolong
 * @date 2017/7/17
 */
public class ConvertUtils {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static <S, T> T convert(S s, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (Exception e) {
            throw BaseException.newException(MessageType.ERROR, "convert error");
        }
    }

    public static <S, T> T convert(S s, Class<T> tClass, String... ignoreProperties) {
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(s, t, ignoreProperties);
            return t;
        } catch (Exception e) {
            throw BaseException.newException(MessageType.ERROR, "convert error");
        }
    }

    public static <S, T> T convert(S s, T t) {
        try {
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (Exception e) {
            throw BaseException.newException(MessageType.ERROR, "convert error");
        }
    }

    public static <S, T> T convert(S s, T t, String... ignoreProperties) {
        try {
            BeanUtils.copyProperties(s, t, ignoreProperties);
            return t;
        } catch (Exception e) {
            throw BaseException.newException(MessageType.ERROR, "convert error");
        }
    }

    public static <S, T> List<T> convert(Iterable<S> iterable, Class<T> tClass, Mapper dozerMapper) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(s -> dozerMapper.map(s, tClass)).collect(Collectors.toList());
    }

    public static <S, T> List<T> convert(Iterable<S> iterable, Class<T> tClass) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(s -> ConvertUtils.convert(s, tClass)).collect(Collectors.toList());
    }

    public static <S, T> List<T> convert(Iterable<S> iterable, Function<? super S, ? extends T> mapper) {
        return StreamSupport.stream(iterable.spliterator(), false).map(mapper).collect(Collectors.toList());
    }

    public static <S, T> PageVO<T> convertPage(Page<S> page, Function<? super S, ? extends T> mapper) {
        List<S> contents = page.getContent();
        List<T> tContents = convert(contents, mapper);
        PageVO<T> pageDto = new PageVO<>();
        pageDto.setContent(tContents);
        pageDto.setFirst(page.isFirst());
        pageDto.setLast(page.isLast());
        pageDto.setNumber(page.getNumber());
        pageDto.setNumberOfElements(page.getNumberOfElements());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setSize(page.getSize());
        return pageDto;
    }

    public static <S, T> PageVO<T> convertPage(Page<S> page, Class<T> tClass) {
        return convertPage(page, s -> convert(s, tClass));
    }
}
