package com.cnh.utils;

import com.cnh.utils.vo.ResultVo;

public class ResultVoUtil {

        //当有返回值而且没问题时传入这个方法
        public static ResultVo success(Object object) {
            ResultVo resultVo = new ResultVo();
            resultVo.setData(object);
            resultVo.setCode(0);
            resultVo.setMsg("success");
            return resultVo;
        }

        //当没有返回值而且正确的时候
        public static ResultVo success() {

            return success(null);
        }

        //错误时
        public static ResultVo error(Integer code, String message) {
            ResultVo resultVo = new ResultVo();
            resultVo.setCode(code);
            resultVo.setMsg(message);
            return resultVo;
        }
    }

