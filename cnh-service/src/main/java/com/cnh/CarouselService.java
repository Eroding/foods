package com.cnh;

import com.cnh.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    List<Carousel> queryAll(Integer isShow);
}
