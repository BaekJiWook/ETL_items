package org.zerock.etl_items.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.etl_items.domain.ETLItemsVo;
import org.zerock.etl_items.domain.TbOprGdsLVo;
import org.zerock.etl_items.mappers.postgres.PostgresMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class PostgresService {

    private final PostgresMapper postgresMapper;

    public List<TbOprGdsLVo> getAllItems() {
        try {
            // 데이터 조회 메서드 호출
            List<TbOprGdsLVo> items = postgresMapper.getList();
            log.info("데이터 조회 성공: {}", items);
            return items;
        } catch (Exception e) {
            log.error("데이터 조회 중 오류 발생: ", e);
            throw new RuntimeException("데이터 조회에 실패했습니다.", e);
        }
    }

//    public void insertPostgresData(ETLItemsVo data) {
//        log.info("데이터 삽입 시작: {}", data);
//
//        try {
//            // PostgreSQL에 데이터 삽입
//            postgresMapper.insertPostgresData(data);
//            log.info("데이터 삽입 성공: {}", data);
//        } catch (Exception e) {
//            log.error("데이터 삽입 중 오류 발생: ", e);
//            throw new RuntimeException("데이터 삽입에 실패했습니다.", e);
//        }
//    }

    // 페이지네이션
    public Map<String, Object> getItems(String searchQuery, int page, int size) {
        int offset = (page - 1) * size;
        List<TbOprGdsLVo> items = postgresMapper.getPaginatedItems(searchQuery, size, offset);
        long totalItems = postgresMapper.getTotalItems(searchQuery);

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalItems", totalItems);
        response.put("itemsPerPage", size);

        return response;
    }


}
