package com.jang226.myweb.util;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data // getter, stter, toString
public class PageVO {
    
    private  int start; // 시작페이지네이션
    private int end; // 끝페이지 네이션
    private boolean prev; // 이전버튼 활성화 여부
    private boolean next; // 다음버튼 활성화 여부
    private int total; // 전체게시글 개수
    private int realEnd; // 실제보여지는 끝번호
    
    //
    private int page; // cri에 있는 현재 조회하는 페이지
    private int amount; // cri에 있는 데이터 개수
    private Criteria cri; // 페이지 기준

    private int pnCount = 10; // 페이지네이션 개수

    // 페이지네이션 클래스는 cri와 total을 매개변수로 받는다
    private List<Integer> pageList;

    public PageVO(Criteria cri, int total){
        this.cri = cri;
        this.page = cri.getPage();
        this.amount = cri.getAmount();
        this.total = total;

        // end 페이지 계산
        // page 가 1을 가르킬 때, end = 10
        // page 가 11을 가르킬 때, end = 20
        // 공식 : this.end = (int) (Math.ceil( 현재 조회하는 페이지 / 페이지네이션 개수 )) * 페이지네이션 개수;
        this.end = (int) (Math.ceil(this.page / (double)this.pnCount)) * this.pnCount;

        // start 페이지 계산
        // this.start = this.end - 페이지네이션 개수 + 1;
        this.start = this.end - this.pnCount + 1;

        //실제 끝 번호 계산 : 페이지의 개수
        //총 게시글 수가 53개 라면 마지막 번호는 6
        //총 게시글 수가 232개 -> 마지막 번호는 24
        // this.realEnd = (int)(Math.ceil( 전체 게시글 수 / 데이터 개수 ));
        this.realEnd = (int)(Math.ceil(this.total / (double)this.amount));

        // end 페이지 재결정
        // 데이터가 25개 -> end = 10, realEnd = 3 => realEnd 를 따라간다 ( 실제 끝번호 )
        // 데이터가 153개 (현재 11번 페이지 조회시 )-> end = 20, realEnd = 16 => 실제 끝번호를 따라간다
        // 데이터가 153개 ( 현재 3번 페이지 조회시) -> end = 10, realEnd = 16 => end 따라감 ( 페이지네이션 따라감 )
        // end 와 realEnd 중 작은 값을 따라간다
        if(this.end > this.realEnd){
            this.end = this.realEnd;
        }

        // prev 활성화 여부
        // start 페이지 결정은 1, 11, 21, 31, 41, .....
        this.prev = this.start > 1;

        // next 활성화 여부
        // end = 10, realEnd = 16 이라 할 때, 다음 버튼이 있다는 의미이다
        this.next = this.realEnd > this.end;

        // 타임리프  리스트에 페이지네이션을 담음
        this.pageList = IntStream.rangeClosed(this.start, this.end).boxed().collect(Collectors.toList());
    }
    
}
