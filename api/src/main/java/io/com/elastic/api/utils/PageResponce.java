package io.com.elastic.api.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PageResponce<T> {
    private Integer totalPages; // 총 페이지
    private Integer totalElements; // 총 속성갯수
    private Integer currentPage; // 현재 페이지
    private T data; // response 데이터

    public static <T> PageResponce<T> of(int totalPages, int totalElements, int currentPage, T content){
        return new PageResponce(totalPages, totalElements, currentPage, content);
    }

}
