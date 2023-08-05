package com.jang226.myweb.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUploadVO {

    private int upload_no; // PK
    private String filename; // 실제 파일명
    private String filepath; // 폴더명
    private String uuid; // UUID 명 ( 난수값 )
    private LocalDateTime regdate; // 등록일
    private Integer prod_id; // FK
    private String prod_writer; // FK

}
