package org.zerock.etl_items.sample.mappers;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.etl_items.domain.ETLItemsVo;
import org.zerock.etl_items.domain.TbOprGdsLVo;
import org.zerock.etl_items.mappers.mariadb.MariaDbMapper;
import org.zerock.etl_items.mappers.postgres.PostgresMapper;
import org.zerock.etl_items.service.ETLService;

import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class ETLTest {

    @Autowired
    private PostgresMapper postgresMapper;

    @Autowired
    private MariaDbMapper mariaDbMapper;

    @Autowired
    private ETLService etlService;

//    @Test
//    @Transactional
//    public void testETLProcess() {
//
//        // MariaDB 데이터를 삭제
//        mariaDbMapper.deleteAllData();
//
//        // PostgreSQL에서 데이터를 조회
//        List<TbOprGdsLVo> postgresData = postgresMapper.getList();
//
//        // 데이터를 변환하고 MariaDB에 삽입
//        for (TbOprGdsLVo tbOprGdsLVo : postgresData) {
//            // 변환 메소드 호출
//            ETLItemsVo etlItemsVo = etlService.convertToETLItemsVo(tbOprGdsLVo);
//
//            // 변환된 데이터를 MariaDB로 삽입
//            mariaDbMapper.insertMariaDBData(etlItemsVo);
//        }
//
//        // MariaDB에서 데이터 확인
//        List<ETLItemsVo> mariaDbData = mariaDbMapper.getMariaDbData(); // MariaDB에서 데이터 조회
//        log.info("MariaDB 데이터: {}", mariaDbData); // 데이터를 로깅하여 확인
//
//        // 데이터 비교 예시 (PostgreSQL과 MariaDB 데이터가 일치하는지 확인)
//        assertNotNull(mariaDbData, "MariaDB에서 데이터를 조회할 수 없습니다.");
//        assertEquals(postgresData.size(), mariaDbData.size(), "데이터 개수가 일치하지 않습니다.");
//    }
//    @Test
//    public void testPagination() {
//        // Given
//        String searchQuery = ""; // 검색어 (필요에 따라 변경 가능)
//        int page = 1; // 테스트할 페이지 번호
//        int size = 5; // 페이지당 항목 수
//        int offset = (page - 1) * size;
//
//        // When
//        List<ETLItemsVo> items = mariaDbMapper.getPaginatedItems(searchQuery, size, offset);
//        long totalItems = mariaDbMapper.getTotalItems(searchQuery);
//
//
//        // 로그 출력
//        System.out.println("총 데이터 수: " + totalItems);
//        items.forEach(System.out::println);
//    }
//    @Test
//    public void testGetDngItems() {
//        // MariaDB에서 데이터 확인
//        mariaDbMapper.getDngMariaDbData();
//    }
//    @Test
//    public void testGetNormItems() {
//        // MariaDB에서 데이터 확인
//        mariaDbMapper.getNormMariaDbData();
//    }
//    @Test
//    public void testInsertEtlItem() {
//        // Given: ETLItemsVo 생성
//        ETLItemsVo etlItemsVo = new ETLItemsVo();
//        etlItemsVo.setPrdctNm("Test Product");
//        etlItemsVo.setPrdctTypeNm("Test Type");
//        etlItemsVo.setInspSeNu(1); // mst_id는 'dng_gds'로 설정되어야 함
//        etlItemsVo.setBzentyNm("Test Company");
//        etlItemsVo.setBzentyAddr("123 Test Street");
//        etlItemsVo.setBzentyDaddr("Apt 456");
//        etlItemsVo.setRptTypeNm("Safe");
//        etlItemsVo.setRptInstNm("Test Institution");
//        etlItemsVo.setRptPicNm("John Doe");
//
//        // When: 데이터 삽입
//        ETLItemsVo insertedDocNo = etlService.insertEtlItem(etlItemsVo);
//
//        // Then: doc_no 반환값 검증
//        assertNotNull(insertedDocNo);
//        System.out.println("Inserted doc_no: " + insertedDocNo);
//    }
//@Test
//public void testInsertEtlItemWithFile() throws IOException {
//    // Given: ETLItemsVo 생성 및 파일 생성
//    ETLItemsVo etlItemsVo = new ETLItemsVo();
//    etlItemsVo.setPrdctNm("Test Product");
//    etlItemsVo.setPrdctTypeNm("Test Type");
//    etlItemsVo.setInspSeNu(1);
//    etlItemsVo.setBzentyNm("Test Company");
//    etlItemsVo.setBzentyAddr("123 Test Street");
//    etlItemsVo.setBzentyDaddr("Suite 456");
//    etlItemsVo.setRptTypeNm("Safe");
//    etlItemsVo.setRptInstNm("Test Institution");
//    etlItemsVo.setRptPicNm("John Doe");
//
//    // 테스트용 파일 생성
//    MockMultipartFile mockFile = new MockMultipartFile(
//            "file",
//            "test-image.jpg",
//            MediaType.IMAGE_JPEG_VALUE,
//            "Test file content".getBytes()
//    );
//
//    // When: 데이터 삽입 호출
//    ETLItemsVo insertedEtlItem = etlService.insertEtlItem(etlItemsVo, mockFile);
//
//    // Then: 결과 검증
//    assertNotNull(insertedEtlItem);
//    assertNotNull(insertedEtlItem.getImgUrl()); // imgUrl이 설정되었는지 확인
//    System.out.println("Inserted imgUrl: " + insertedEtlItem.getImgUrl());
//}

}
