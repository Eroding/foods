package com.cnh.mapper;


import com.cnh.my.mapper.MyMapper;
import com.cnh.pojo.Category;
import com.cnh.vo.CategoryVO;
import com.cnh.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {
    List<CategoryVO> getSubCatList(int rootCatId);

    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}