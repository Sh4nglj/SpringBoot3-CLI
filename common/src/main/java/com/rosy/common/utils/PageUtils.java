package com.rosy.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.enums.ErrorCode;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 转换分页对象
     *
     * @param sourcePage 源分页对象
     * @param converter  转换函数
     * @param <T>        源类型
     * @param <R>        目标类型
     * @return 转换后的分页对象
     */
    public static <T, R> Page<R> convert(Page<T> sourcePage, Function<T, R> converter) {
        if (sourcePage == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页对象不能为空");
        }
        if (converter == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "转换函数不能为空");
        }
        Page<R> targetPage = new Page<>(sourcePage.getCurrent(), sourcePage.getSize(), sourcePage.getTotal());
        List<T> records = sourcePage.getRecords();
        records = records != null ? records : Collections.emptyList();
        List<R> convertedRecords = records.stream().map(converter).collect(Collectors.toList());
        targetPage.setRecords(convertedRecords);
        return targetPage;
    }

    /**
     * 转换分页对象（使用BeanUtil自动复制属性）
     *
     * @param sourcePage  源分页对象
     * @param targetClass 目标类
     * @param <T>         源类型
     * @param <R>         目标类型
     * @return 转换后的分页对象
     */
    public static <T, R> Page<R> convert(Page<T> sourcePage, Class<R> targetClass) {
        if (targetClass == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "目标类不能为空");
        }
        return convert(sourcePage, source -> source != null ? BeanUtil.copyProperties(source, targetClass) : null);
    }
}
