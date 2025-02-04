package org.zerock.etl_items.domain;

import lombok.Data;

@Data
public class ETLItemsVo {
    private String mstId;
    private Long mstDataId;
    private String inspInstCd;
    private String docCycl;
    private Long docNo;
    private String prdctNm;
    private String prdctTypeNm;
    private String mnftrYmd;
    private String rptTypeNm;
    private String rptInstNm;
    private String rptPicNm;
    private int inspSeNu;
    private String inspSeNm;
    private String bzentyTypeNm;
    private String bzentyNm;
    private String bzentyTelno;
    private String bzentyAddr;
    private String bzentyDaddr;
    private String icptInspArtclCn;
    private String icptInspSpcfctCn;
    private String icptInspRsltCn;
    private String imgUrl;
    private String createdAt;

}
