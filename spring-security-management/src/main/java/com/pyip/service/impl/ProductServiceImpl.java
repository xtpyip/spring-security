package com.pyip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyip.domain.Product;
import com.pyip.mapper.ProductMapper;
import com.pyip.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public List<Product> findAll() {
        return null;
    }
}
