package com.jang226.myweb;

import com.jang226.myweb.command.ProductVO;
import com.jang226.myweb.product.service.ProductMapper;
import com.jang226.myweb.util.Criteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class PageTest {

    @Autowired
    private ProductMapper productMapper;

    // 더미데이터 생성

    @Test
    public void testCode01() {

        for (int i = 0; i <= 100; i++) {
            ProductVO vo = ProductVO.builder()
                    .prod_enddate("2023-06-24")
                    .prod_writer("admin")
                    .prod_name("test" + i)
                    .prod_price(1000 + i)
                    .prod_count(100 + i)
                    .prod_discount(1 + i)
                    .prod_purchase_yn("Y")
                    .prod_content("abcdefg" + i)
                    .prod_comment("test")
                    .build();

            productMapper.productRegist(vo);
        }
    }

    // 페이지 조회
    @Test
    public void testCode02() {

        Criteria cri = new Criteria(1, 10); // 1번 페이지, 10개 데이터
//        Criteria cri = new Criteria(1, 10); // 2번 페이지, 10개 데이터
//        Criteria cri = new Criteria(2, 100); // 2번 페이지, 100개 데이터

        ArrayList<ProductVO> list = productMapper.getList("admin", cri);

        System.out.println(list.toString());

    }
}
