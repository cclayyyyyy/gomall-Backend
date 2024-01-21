package com.atchen.gomall.model.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "响应结果实体类")
public class Result<T> {

    //Response Code
    @Schema(description = "业务状态码")
    private Integer code;

    //Return Message
    @Schema(description = "响应消息")
    private String message;

    //Return Data
    @Schema(description = "业务数据")
    private T data;

    // Private Constructor
    private Result() {}

    // Return Data
    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = new Result<>();
        result.setData(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // Constructing a Result object through enumeration
    public static <T> Result build(T body , ResultCodeEnum resultCodeEnum) {
        return build(body , resultCodeEnum.getCode() , resultCodeEnum.getMessage()) ;
    }

}
