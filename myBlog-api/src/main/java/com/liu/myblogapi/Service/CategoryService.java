
package com.liu.myblogapi.Service;

import com.liu.myblogapi.vo.CategoryVo;
import com.liu.myblogapi.vo.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long id);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
