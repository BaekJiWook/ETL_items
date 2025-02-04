package org.zerock.etl_items.sample.mappers;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.zerock.etl_items.domain.TbGdsBzentyLVo;
//import org.zerock.etl_items.domain.TbGdsIcptInspLVO;
import org.zerock.etl_items.domain.TbOprGdsLVo;
import org.zerock.etl_items.mappers.postgres.PostgresMapper;
import org.zerock.etl_items.service.PostgresService;

import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class PostgresTest {

    @Autowired
    private PostgresMapper postgresMapper;

    @Test
    public void testGetList() {
        // tb_opr_gds_l 데이터를 테스트
        List<TbOprGdsLVo> list = postgresMapper.getList();
        log.info("getList 결과:");
        list.forEach(item -> log.info(item.toString()));
    }
    @Test
    public void testPagination() {
        // Given
        String searchQuery = ""; // 검색어 (필요에 따라 변경 가능)
        int page = 1; // 테스트할 페이지 번호
        int size = 5; // 페이지당 항목 수
        int offset = (page - 1) * size;

        // When
        List<TbOprGdsLVo> items = postgresMapper.getPaginatedItems(searchQuery, size, offset);
        long totalItems = postgresMapper.getTotalItems(searchQuery);
        

        // 로그 출력
        System.out.println("총 데이터 수: " + totalItems);
        items.forEach(System.out::println);
    }
}


