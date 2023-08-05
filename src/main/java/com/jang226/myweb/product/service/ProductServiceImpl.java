package com.jang226.myweb.product.service;

import com.jang226.myweb.command.CategoryVO;
import com.jang226.myweb.command.ProductUploadVO;
import com.jang226.myweb.command.ProductVO;
import com.jang226.myweb.util.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("productService")
public class ProductServiceImpl implements ProductService{
    
    // 사용할 Mapper 객체
    @Autowired
    private ProductMapper productMapper;

    /// 이미지 업로드를 위한 경로와, 폴더 생성
    
    // 경로
    @Value("${project.upload.path}")
    private String uploadpath;

    // 폴더 생성함수
    public String makeFolder() {

        String path = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 생성시의 경로에 폴더를 생성해줌
//      File file =  new File(경로);
//        file.mkdirs();
        File file = new File(uploadpath + "/" + path);
        if (!file.exists()) { // 존재하면 true, 존재하지 않으면 false
            file.mkdirs();
        }
        return path; // 날짜 폴더명 반환
    }


    // 상품 등록
    
    //하나의 메서드에서 여러 CRUD 작업이 일어나는 경우에 한 부분이 에러가 발생하면 그 에러를 처리하고, 롤백처리를 대신한다
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int productRegist(ProductVO vo, List<MultipartFile> list) {
        // int 를 반환하므로 바로 리턴
        // 서비스 영역에서 할 일이 있다면 위에서 처리하고 반환할 수 도 있다

        // product 테이블 처리 ( vo insert )
        int result = productMapper.productRegist(vo);

        // 파일 업로드 처리
        for(MultipartFile file : list){

            // 파일 이름
            String originName = file.getOriginalFilename();

            //브라우저 별로 파일의 경로가 다를 수 있기 때문에 \\ 기준으로 파일 명만 잘라서 다시 저장
            // 뒤에서 부터 탐색
            String filename = originName.substring(originName.lastIndexOf("\\") + 1);

            //동일한 파일을 재업로드 시, 기존 파일을 덮어 씌운다 => 난수 이름으로 파일명을 변경해서 업로드
            String uuid = UUID.randomUUID().toString(); // 랜덤한 난수 생성

            // 날짜 별로 폴더 생성
            String filepath = makeFolder();

            //세이브할 경로
            // 저장할 경로에 파일 명 까지 더한 값
            String savepath = uploadpath + "/" + filepath + "/" + uuid + "_" + filename;

            // 데이터베이스에 추후에 저장
            System.out.println("실제 파일명 = " + filename);
            System.out.println("난수값 = " + uuid);
            System.out.println("날짜폴더경로 = " + filepath);
            System.out.println("세이브할 경로 = " + savepath);
            System.out.println("=====================================================================================");

            try {
                File saveFile = new File(savepath);
                file.transferTo(saveFile); // 파일 업로드 진행
            } catch (Exception e) {
                System.out.println("파일업로드 중 error 발생");
                e.printStackTrace();
                return 0; // 실패 했다 -> 0 반환
            }
            
            //productUpload 테이블에 파일의 경로 insert
            // prod_id 는 insert 전에 selectKey 를 통해 받는다
            productMapper.productFileRegist(ProductUploadVO.builder()
                                                           .filename(filename)
                                                           .filepath(filepath)
                                                           .uuid(uuid)
                                                           .prod_writer(vo.getProd_writer())
                                                           .build());

        } // end for

        return result;
    }

    // 상품의 수정 이미지 가져오기


    @Override
    public ArrayList<ProductUploadVO> getProductImg(int prod_id) {
        return productMapper.getProductImg(prod_id);
    }

    // 작성자의 리스트 조회
    @Override
    public ArrayList<ProductVO> getList(String writer, Criteria cri) {
        return productMapper.getList(writer, cri);
    }

    @Override
    public int getTotal(String writer, Criteria cri) {
        return productMapper.getTotal(writer, cri);
    }

    // 상세 정보 조회
    @Override
    public ProductVO getDetail(int prod_id) {

        return productMapper.getDetail(prod_id);
    }

    // 상품 정보 수정

    @Override
    public int productUpdate(ProductVO vo) {

        return productMapper.productUpdate(vo);
    }

    // 상품 삭제
    @Override
    public int productDelete(int prod_id) {

        return productMapper.productDelete(prod_id);
    }

    // 카테고리 처리
    @Override
    public ArrayList<CategoryVO> getCategory() {

        return productMapper.getCategory();
    }

    @Override
    public ArrayList<CategoryVO> getCategoryChild(CategoryVO vo) {
        return productMapper.getCategoryChild(vo);
    }
}
