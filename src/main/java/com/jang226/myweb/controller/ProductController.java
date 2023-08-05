package com.jang226.myweb.controller;

import com.jang226.myweb.command.ProductVO;
import com.jang226.myweb.product.service.ProductService;
import com.jang226.myweb.util.Criteria;
import com.jang226.myweb.util.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    // 사용할 서비스 객체
    @Autowired
    @Qualifier("productService")
    private ProductService productService;

    // 상품 목록
    @GetMapping("/productList")
    public String list(Model model, Criteria cri) {

        // 임시
        // 로그인 기능이 없으므로, admin 이라는 계정 기반으로 조회
        String writer = "admin";

        // 1st
        // 목록 조회 해서 받아온 list
//        ArrayList<ProductVO> list = productService.getList(writer);
//        model.addAttribute("list", list);

        // 2nd
        ArrayList<ProductVO> list = productService.getList(writer, cri);

        // 작성자에 해당하는 모든 sql 데이터의 개수
        int total = productService.getTotal(writer, cri);

        PageVO pageVO = new PageVO(cri, total);

        // 목록, pageVO 를 model 에
        model.addAttribute("list", list);
        model.addAttribute("pageVO", pageVO);

        System.out.println(pageVO.toString());

        return "product/productList";
    }

    // 상품 등록
    @GetMapping("/productReg")
    public String reg() {
        return "product/productReg";
    }

    //post 방식
    //등록요청
    @PostMapping("/registForm")
    public String registForm(ProductVO vo, RedirectAttributes ra,
                             @RequestParam("file") List<MultipartFile> list ) {
        // 1. 빈 객체 검사
        list = list.stream().filter(f -> !f.isEmpty()).collect(Collectors.toList());

        // 2. 확장자 검사
        for(MultipartFile file : list) {
            if (!file.getContentType().contains("image")) {
                ra.addFlashAttribute("msg", "jpg, png, jpeg 이미지 파일만 등록이 가능합니다");
                // 지금은 이미지가 아니라면 list 목록으로
                return "redirect:/product/productList"; // 처리할 거면 나중에 추가
            }
        }

        // 3. 파일을 처리하는 작업은 service 위임 => 원래 controller Request 객체를 받고 service 위임 전략
        
        // 등록작업~~~
//        System.out.println(vo.toString());
        // 성공 = 1 , 실패 = 0
        int result = productService.productRegist(vo, list);

        String msg = result == 1 ? "등록되었습니다" : "등록 실패, 관리자에게 문의하세요";

        ra.addFlashAttribute("msg", msg);

        return "redirect:/product/productList";
    }

    // 상품 상세 정보
    @GetMapping("/productDetail")
    public String detail(@RequestParam("prod_id") int prod_id, Model model) {

        // 데이터 조회해서 해당 데이터를 수정/관리 할 수 있게 
        // prod_id 가 필요하다
        ProductVO vo = productService.getDetail(prod_id);
        model.addAttribute("vo", vo);

        return "product/productDetail";
    }

    // 상품 수정
    @PostMapping("/modifyForm")
    public String modifyForm(ProductVO vo, RedirectAttributes ra) {

        //데이터베이스 업데이트 작업을 진행
        // 업데이트된 결과를 반환받아서 list 화면으로 "업데이트 성공" 메시지르 띄우기

        int result = productService.productUpdate(vo);

        String msg = result == 1 ? "업데이트 성공" : "업데이트 실패~!!";

        ra.addFlashAttribute("msg", msg);

        return "redirect:/product/productList";
    }

    // 상품 삭제
    @PostMapping("/deleteForm")
    public String deleteForm(@RequestParam("prod_id") int prod_id, RedirectAttributes ra){

        int result = productService.productDelete(prod_id);

        String msg = result == 1 ? "삭제 성공" : "삭제 실패 !!";

        ra.addFlashAttribute("msg", msg);

        return "redirect:/product/productList";
    }
}
