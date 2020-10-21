package com.cnh.utils.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class ResultVo<T> implements Serializable {


    private static final long serialVersionUID = 8215964912862543378L;
    /* code是错误码*/
    private Integer code;
    /* 提示信息*/
    private String msg;
    /* 具体内容*/
    private T data;

}
