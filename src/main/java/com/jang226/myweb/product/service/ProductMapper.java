package com.jang226.myweb.product.service;

import com.jang226.myweb.command.CategoryVO;
import com.jang226.myweb.command.ProductUploadVO;
import com.jang226.myweb.command.ProductVO;
import com.jang226.myweb.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ProductMapper {
    
    // 상품 등록
    int productRegist(ProductVO vo);

    // 이미지 파일 등록
    void productFileRegist(ProductUploadVO vo);

    // 상품 목록 조회
//    ArrayList<ProductVO> getList(String writer);
    // writer, page, amount 사용 가능
    ArrayList<ProductVO> getList(@Param("writer") String writer,
                                 @Param("cri") Criteria cri);
    
    // 조회된 데이터 수
    int getTotal(@Param("writer") String writer, @Param("cri") Criteria cri);


    // 상세 조회
    ProductVO getDetail(int prod_id);

    // 상품 정보 수정
    int productUpdate(ProductVO vo);

    // 상품 삭제
    int productDelete(int prod_id);

    //카테고리 처리
    ArrayList<CategoryVO> getCategory();

    // 하위 카테고리 처리
    ArrayList<CategoryVO> getCategoryChild(CategoryVO vo);

    // 모달창 이미지
    ArrayList<ProductUploadVO> getProductImg(int prod_id);

}
