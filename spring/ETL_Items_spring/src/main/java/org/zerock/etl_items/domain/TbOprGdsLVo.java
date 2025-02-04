package org.zerock.etl_items.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class TbOprGdsLVo {
    private String mstId;           // 마스터아이디
    private String mstDataId;       // 마스터데이터아이디
    private String mstUseYn;        // 마스터사용여부
    private String regId;           // 등록아이디
    private Timestamp regDt;        // 등록일시
    private String modId;           // 변경아이디
    private Timestamp modDt;        // 변경일시
    private String inspInstCd;      // 검사기관코드
    private String docNo;           // 문서번호
    private String docCycl;         // 문서차수
    private String inspInstNm;      // 검사기관명
    private String ntfctnRtrcnYn;   // 통보취소여부
    private String rptTypeCd;       // 보고유형코드
    private String rptTypeNm;       // 보고유형명
    private String prdctNm;         // 제품명
    private String prdctTypeNm;     // 제품유형명
    private String plorNm;          // 원산지명
    private String barcdNo;         // 바코드번호
    private String mnftrYmd;        // 제조일자
    private String rtlTermCn;       // 유통기한내용
    private String pacUnitCn;       // 포장단위내용
    private String safeCertNo;      // 안전인증번호
    private String prdctPrmsnNo;    // 제품허가번호
    private String mnftrNo;         // 제조번호
    private String rtrvlRsnCd;      // 회수사유코드
    private String rtrvlRsnNm;      // 회수사유명
    private String rptInstNm;       // 보고기관명
    private String rptPicNm;        // 보고담당자명
    private String rptPicTelno;     // 보고담당자전화번호
    private String inspSeNm;        // 검사구분명
    private String cltInstNm;       // 수거기관명
    private String cltYmd;          // 수거일자
    private String rmrkCn;          // 비고내용
    private String rptYmd;          // 보고일자
    private Timestamp ntfctnDt;     // 통보일시
    private String cmdBgngDdCn;    // 명령시작일내용
    private String certNm;          // 인증명
    private String certCdCn;        // 인증코드내용
    private String stdGdsClsfCd;    // 표준상품분류코드
    private String imgUrl;         //이미지url
    LocalDateTime createdAt;        // created_at 필드 추가

    // 자식 테이블 데이터를 포함한 필드
    private TbGdsIcptInspLVo icptInspData;
    private TbGdsBzentyLVo bzentyData;

    // 기본 생성자
    public TbOprGdsLVo() {
    }

}
