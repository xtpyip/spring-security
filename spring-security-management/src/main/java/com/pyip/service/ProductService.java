package com.pyip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyip.domain.Product;

import java.util.List;

public interface ProductService extends IService<Product> {

    List<Product> findAll();

}
