package com.cnh;

import com.cnh.pojo.Category;
import com.cnh.vo.CategoryVO;
import com.cnh.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {
    public List<Category> queryAllRootLevelCat();

    public List<CategoryVO> getSubCatList(Integer rootCatId);

    //查询最新6条数据
    public List<NewItemsVO> getSixNewItemLazy(Integer rootCatId);
}
