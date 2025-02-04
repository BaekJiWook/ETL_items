package org.zerock.etl_items.domain;

import lombok.Data;

@Data
public class TbGdsIcptInspLVo {

    private String inspInstCd;          // 검사기관코드
    private String docNo;               // 문서번호
    private String docCycl;             // 문서차수
    private String icptInspSn;          // 검사일련번호
    private String icptInspArtclCn;     // 검사 항목 내용
    private String icptInspSpcfctCn;    // 검사 세부 내용
    private String icptInspRsltCn;      // 검사 결과 내용

}
