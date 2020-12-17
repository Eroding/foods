package com.cnh.controller;

import com.cnh.CarouselService;
import com.cnh.CategoryService;
import com.cnh.bo.UsersBo;
import com.cnh.com.imooc.enums.YesOrNo;
import com.cnh.com.imooc.utils.IMOOCJSONResult;
import com.cnh.pojo.Carousel;
import com.cnh.pojo.Category;
import com.cnh.pojo.Users;
import com.cnh.vo.CategoryVO;
import com.cnh.vo.NewItemsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(value = "index", tags = {"index"})
//这个方法用于不展示这个controller @ApiIgnore
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "轮播列表", notes = "轮播列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() throws Exception {
        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.type);
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "第一列", notes = "第一列", httpMethod = "GET")
    @GetMapping("/cats")
    public IMOOCJSONResult cats() throws Exception {
        List<Category> list = categoryService.queryAllRootLevelCat();
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "第一列", notes = "第一列", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(@PathVariable Integer rootCatId) throws Exception {
        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "最新六条", notes = "第一列", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItems(@PathVariable Integer rootCatId) throws Exception {
        List<NewItemsVO> list = categoryService.getSixNewItemLazy(rootCatId);
        return IMOOCJSONResult.ok(list);
    }
}
