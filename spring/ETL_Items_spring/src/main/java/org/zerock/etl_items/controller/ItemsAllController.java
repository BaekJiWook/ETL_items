package org.zerock.etl_items.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.etl_items.domain.ETLItemsVo;
import org.zerock.etl_items.domain.TbOprGdsLVo;
import org.zerock.etl_items.service.ETLService;
import org.zerock.etl_items.service.PostgresService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Log4j2
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:8081")
@CrossOrigin(origins = {"http://52.79.139.134:8081", "http://localhost:8081"})
@RequestMapping("/api") // /api 경로가 중복되지 않도록 조정
public class ItemsAllController {
    private final PostgresService postgresService;
    private final ETLService etlService;

    @GetMapping("/itemsAll")
    public Map<String, Object> getAllItemsOrFiltered(
            @RequestParam(required = false, defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("API 호출: /api/itemsAll");
        log.info("검색어: {}, 페이지: {}, 페이지당 항목 수: {}", searchQuery, page, size);

        // 검색어가 없을 경우 전체 항목 반환
        if (searchQuery.trim().isEmpty()) {
            log.info("검색어가 없으므로 전체 데이터를 반환합니다.");
            List<TbOprGdsLVo> allItems = postgresService.getAllItems();

            // 결과를 Map으로 래핑하여 반환
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", allItems.size());
            response.put("items", allItems);

            return response;
        }

        // 검색어가 있을 경우 페이지네이션된 데이터 반환
        log.info("검색어가 있으므로 필터링된 데이터를 반환합니다.");
        return postgresService.getItems(searchQuery, page, size);
    }

    @PostMapping("/etlItems")
    @ResponseBody
    public ResponseEntity<String> initiateETL() {
        try {
            log.info("ETL 프로세스 시작");
            etlService.transferData();
            log.info("ETL 프로세스 완료");
            return ResponseEntity.ok("ETL 프로세스가 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            log.error("ETL 프로세스 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("ETL 프로세스에 실패했습니다.");
        }
    }

    @GetMapping("/mariaItems")
    public Map<String, Object> getAllItemsOrFilteredMaria(
            @RequestParam(required = false, defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("API 호출: /api/mariaItems");
        log.info("검색어: {}, 페이지: {}, 페이지당 항목 수: {}", searchQuery, page, size);

        // 검색어가 없을 경우 전체 항목 반환
        if (searchQuery.trim().isEmpty()) {
            log.info("검색어가 없으므로 전체 데이터를 반환합니다.");
            List<ETLItemsVo> allItems = etlService.getAllMariaDbItems();

            // 결과를 Map으로 래핑하여 반환
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", allItems.size());
            response.put("items", allItems);

            return response;
        }

        // 검색어가 있을 경우 페이지네이션된 데이터 반환
        log.info("검색어가 있으므로 필터링된 데이터를 반환합니다.");
        return etlService.getItems(searchQuery, page, size);
    }

    //위해 아이템
    @GetMapping("/dngItems")
    public Map<String, Object> getDngItemsMaria(
            @RequestParam(required = false, defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("API 호출: /api/dngItems");
        log.info("검색어: {}, 페이지: {}, 페이지당 항목 수: {}", searchQuery, page, size);

        // 검색어가 없을 경우 전체 항목 반환
        if (searchQuery.trim().isEmpty()) {
            log.info("검색어가 없으므로 전체 데이터를 반환합니다.");
            List<ETLItemsVo> dngItems = etlService.getDngMariaDbData();

            // 결과를 Map으로 래핑하여 반환
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", dngItems.size());
            response.put("items", dngItems);

            return response;
        }
        // 검색어가 있을 경우 페이지네이션된 데이터 반환
        log.info("검색어가 있으므로 필터링된 데이터를 반환합니다.");
        return etlService.getDngItems(searchQuery, page, size);
    }

    // 특정 위해상품 데이터 조회
    @GetMapping("/dngItems/{id}")
    public List<ETLItemsVo> getDngItemsDetail(@PathVariable("id") String id) {
        return etlService.getDngItemById(id);
    }

    //일반아이템
    @GetMapping("/normItems")
    public Map<String, Object> getNormItemsMaria (
            @RequestParam(required = false, defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){

        // 검색어가 없을 경우 전체 항목 반환
        if (searchQuery.trim().isEmpty()) {
            log.info("검색어가 없으므로 전체 데이터를 반환합니다.");
            List<ETLItemsVo> normItems = etlService.getNormMariaDbData();

            // 결과를 Map으로 래핑하여 반환
            Map<String, Object> response = new HashMap<>();
            response.put("totalItems", normItems.size());
            response.put("items", normItems);

            return response;
        }
        // 검색어가 있을 경우 페이지네이션된 데이터 반환
        return etlService.getNormItems(searchQuery, page, size);
    }
    // 특정 일반상품 데이터 조회
    @GetMapping("/normItems/{id}")
    public List<ETLItemsVo> getNormItemsDetail(@PathVariable("id") String id) {
        return etlService.getNormItemById(id);
    }

    // 데이터 수정 (일반 상품)
    @PutMapping(value = "/normItems/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateNormItem(
            @PathVariable("id") Long id,
            @RequestPart("etlItemsVo") String etlItemsVoJson,  // 🔹 JSON을 String으로 받기
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // 🔹 JSON을 Java 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ETLItemsVo etlItemsVo = objectMapper.readValue(etlItemsVoJson, ETLItemsVo.class);

            // 🔹 서비스 메서드 호출
            etlService.updateEtlItem(id, etlItemsVo, file);

            return ResponseEntity.ok("수정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패: " + e.getMessage());
        }
    }

    // 데이터 수정 (위해 상품)
    @PutMapping(value = "/dngItems/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateDngItem(
            @PathVariable("id") Long id,
            @RequestPart("etlItemsVo") String etlItemsVoJson,  // 🔹 JSON을 String으로 받기
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // 🔹 JSON을 Java 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ETLItemsVo etlItemsVo = objectMapper.readValue(etlItemsVoJson, ETLItemsVo.class);

            // 🔹 서비스 메서드 호출
            etlService.updateEtlItem(id, etlItemsVo, file);

            return ResponseEntity.ok("수정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패: " + e.getMessage());
        }
    }


    //등록하기
    @PostMapping("/itemPost")
    public ResponseEntity<Map<String, Object>> insertEtlItem(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "etlItem") String etlItemJson
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            // JSON 데이터를 Java 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ETLItemsVo etlItemsVo = objectMapper.readValue(etlItemJson, ETLItemsVo.class);

            // Service를 통해 데이터 및 파일 저장 처리
            ETLItemsVo savedEtlItem = etlService.insertEtlItem(etlItemsVo, file);

            // 응답 생성
            response.put("etlItem", savedEtlItem);
            response.put("message", "업로드 및 데이터 저장 성공!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 실패 응답 반환
            response.put("message", "업로드 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //삭제하기
    @DeleteMapping("/deleteItem/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") Long id) {
        try {
            etlService.deleteEtlItem(id);
            return ResponseEntity.ok("삭제가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("삭제 실패: " + e.getMessage());
        }
    }


}

