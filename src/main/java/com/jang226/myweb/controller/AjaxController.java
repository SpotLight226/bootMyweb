package com.jang226.myweb.controller;

import com.jang226.myweb.command.CategoryVO;
import com.jang226.myweb.command.ProductUploadVO;
import com.jang226.myweb.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class AjaxController {

    @Autowired
    private ProductService productService;

    @Value("${project.upload.path}")
    private String uploadPath; // 업로드할 경로

    @GetMapping("/getCategory")
    public ResponseEntity<ArrayList<CategoryVO>> getCategory() {

//        ArrayList<CategoryVO> list = productService.getCategory();

        return new ResponseEntity<>(productService.getCategory(), HttpStatus.OK);
    }

    @GetMapping("/getCategoryChild/{a}/{b}/{c}")
    public ResponseEntity<ArrayList<CategoryVO>> getCategoryChild(@PathVariable("a") String group_id,
                                                                  @PathVariable("b") int category_lv,
                                                                  @PathVariable("c") int category_detail_lv) {

        CategoryVO vo = CategoryVO.builder()
                                  .group_id(group_id)
                                  .category_lv(category_lv)
                                  .category_detail_lv(category_detail_lv)
                                  .build();

        return new ResponseEntity<>(productService.getCategoryChild(vo), HttpStatus.OK);
    }
    
    /////////////////////////////////////////////////////
    // 이미지 데이터 화면에 응답하기
    /*
    1. 화면에서 넘어오는 getAjaxImg 요청을 받는 REST API 생성
    1 -1. 서비스, 매퍼 함수를 생성
    2. getAjaxImg()로 업로드 테이블에서 조회한 결과를 화면으로 전달
     */
    @PostMapping("/getAjaxImg")
    public ResponseEntity<ArrayList<ProductUploadVO>> getAjaxImg(@RequestBody Map<String, Integer> map) {
        
        // prod_id 를 이용해 가져온 이미지 리스트
        ArrayList<ProductUploadVO> list = productService.getProductImg(map.get("prod_id"));

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    // 이미지 src값 응답하기
    @GetMapping("/display")
    public ResponseEntity<byte[]> display(@RequestParam("filename") String filename,
                                          @RequestParam("filepath") String filepath,
                                          @RequestParam("uuid") String uuid) {

        String path = uploadPath + "/" + filepath + "/" + uuid + "_" + filename;
        File file = new File(path);

        byte[] arr = null;

        try {
            arr = FileCopyUtils.copyToByteArray(file); // 파일 경로 기반으로 데이터를 구함
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(arr, HttpStatus.OK);

    }
}
