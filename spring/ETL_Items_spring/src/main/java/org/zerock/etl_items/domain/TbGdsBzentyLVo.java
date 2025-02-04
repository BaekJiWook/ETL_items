package org.zerock.etl_items.domain;

import lombok.Data;

@Data
public class TbGdsBzentyLVo {
    private String inspInstCd;  // 검사기관코드
    private String docNo;       // 문서번호
    private String docCycl;     // 문서차수
    private String bzentySn;    // 부적합일련번호
    private String bzentyTypeNm; // 부적합 종류 명
    private String bzentyBrno;  // 부적합 번호
    private String bzentyNm;    // 부적합명
    private String bzentyTelno; // 부적합 전화번호
    private String bzentyZip;   // 부적합 우편번호
    private String bzentyAddr;  // 부적합 주소
    private String bzentyDaddr; // 부적합 상세 주소


}
