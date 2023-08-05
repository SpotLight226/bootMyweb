package com.jang226.myweb.product.service;

import com.jang226.myweb.command.CategoryVO;
import com.jang226.myweb.command.ProductUploadVO;
import com.jang226.myweb.command.ProductVO;
import com.jang226.myweb.util.Criteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {

    // 상품 등록
    int productRegist(ProductVO vo, List<MultipartFile> list);

    // 상품 목록 조회
//    ArrayList<ProductVO> getList(String writer);
    ArrayList<ProductVO> getList(String writer, Criteria cri);
    // 전체 데이터 수
    int getTotal(String writer, Criteria cri);

    // 상품 상세 정보 조회
    ProductVO getDetail(int prod_id);

    // 상품 정보 변경
    int productUpdate(ProductVO vo);

    // 상품 삭제
    int productDelete(int prod_id);
    
    // 카테고리 처리
    ArrayList<CategoryVO> getCategory();

    ArrayList<CategoryVO> getCategoryChild(CategoryVO vo);

    // 수정 이미지 가져오기
    ArrayList<ProductUploadVO> getProductImg(int prod_id);

}
