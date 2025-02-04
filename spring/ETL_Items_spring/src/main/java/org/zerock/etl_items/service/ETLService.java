package org.zerock.etl_items.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.etl_items.domain.ETLItemsVo;
import org.zerock.etl_items.domain.TbGdsBzentyLVo;
import org.zerock.etl_items.domain.TbGdsIcptInspLVo;
import org.zerock.etl_items.domain.TbOprGdsLVo;
import org.zerock.etl_items.mappers.mariadb.MariaDbMapper;
import org.zerock.etl_items.mappers.postgres.PostgresMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ETLService {

    @Autowired
    private PostgresMapper postgresMapper;

    @Autowired
    private MariaDbMapper mariaDbMapper;

    public ETLItemsVo convertToETLItemsVo(TbOprGdsLVo tbOprGdsLVo) {
        ETLItemsVo etlItemsVo = new ETLItemsVo();

        // TbOprGdsLVo 객체에서 ETLItemsVo 객체로 데이터 복사
        etlItemsVo.setMstId(tbOprGdsLVo.getMstId());
        etlItemsVo.setMstDataId(Long.valueOf(tbOprGdsLVo.getMstDataId()));
        etlItemsVo.setInspInstCd(tbOprGdsLVo.getInspInstCd());
        etlItemsVo.setDocNo(Long.valueOf(tbOprGdsLVo.getDocNo()));
        etlItemsVo.setPrdctNm(tbOprGdsLVo.getPrdctNm());
        etlItemsVo.setPrdctTypeNm(tbOprGdsLVo.getPrdctTypeNm());
        etlItemsVo.setMnftrYmd(tbOprGdsLVo.getMnftrYmd());
        etlItemsVo.setRptTypeNm(tbOprGdsLVo.getRptTypeNm());
//        etlItemsVo.setRptInstNm(tbOprGdsLVo.getRptInstNm());
        etlItemsVo.setInspSeNm(tbOprGdsLVo.getInspSeNm());
        etlItemsVo.setImgUrl(tbOprGdsLVo.getImgUrl() != null ? tbOprGdsLVo.getImgUrl() : null);

        // 자식 테이블 1: TbGdsIcptInspLVo 데이터 복사
        TbGdsIcptInspLVo icptInsp = tbOprGdsLVo.getIcptInspData();
        if (icptInsp != null) {
            etlItemsVo.setIcptInspArtclCn(icptInsp.getIcptInspArtclCn());
            etlItemsVo.setIcptInspSpcfctCn(icptInsp.getIcptInspSpcfctCn());
            etlItemsVo.setIcptInspRsltCn(icptInsp.getIcptInspRsltCn());
        }

        // 자식 테이블 2: TbGdsBzentyLVo 데이터 복사
        TbGdsBzentyLVo bzenty = tbOprGdsLVo.getBzentyData();
        if (bzenty != null) {
            etlItemsVo.setBzentyTypeNm(bzenty.getBzentyTypeNm());
            etlItemsVo.setBzentyNm(bzenty.getBzentyNm());
            etlItemsVo.setBzentyTelno(bzenty.getBzentyTelno());
            etlItemsVo.setBzentyAddr(bzenty.getBzentyAddr());
            etlItemsVo.setBzentyDaddr(bzenty.getBzentyDaddr());
        }

        // created_at 처리 (현재 시간으로 설정)
        etlItemsVo.setCreatedAt(String.valueOf(LocalDateTime.now()));

        return etlItemsVo;
    }


    // MariaDB의 데이터를 삭제하는 메서드
    public void deleteAllData() {
        mariaDbMapper.deleteAllData();  // MariaDbMapper의 deleteAllData 메서드 호출
    }

    // ETL 프로세스 실행
    @Transactional
    public void transferData() {
        // 데이터 삭제 (기존 데이터를 삭제)
        deleteAllData();

        // PostgreSQL에서 데이터를 조회
        List<TbOprGdsLVo> postgresData = postgresMapper.getList();

        // 데이터를 변환하고 MariaDB에 삽입
        for (TbOprGdsLVo tbOprGdsLVo : postgresData) {
            // 변환 메소드 호출
            ETLItemsVo etlItemsVo = convertToETLItemsVo(tbOprGdsLVo);

            // 변환된 데이터를 MariaDB로 삽입
            mariaDbMapper.insertMariaDBData(etlItemsVo);
        }
    }
    //전체데이터 조회
    public List<ETLItemsVo> getAllMariaDbItems() {
        return mariaDbMapper.getMariaDbData();
    }

    //위해상품데이터 조회+상세보기
    public List<ETLItemsVo> getDngMariaDbData() {
        return mariaDbMapper.getDngMariaDbData();
    }
    public List<ETLItemsVo> getDngItemById(String id) {
        // DB 조회 로직
        List<ETLItemsVo> items = mariaDbMapper.findDngItemsById(id);
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("No data found for id: " + id);
        }
        return items;
    }

    //일반데이터 조회+상세보기
    public List<ETLItemsVo> getNormMariaDbData() {
        return mariaDbMapper.getNormMariaDbData();
    }
    public List<ETLItemsVo> getNormItemById(String id) {
        // DB 조회 로직
        List<ETLItemsVo> items = mariaDbMapper.findNormItemsById(id);
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("No data found for id: " + id);
        }
        return items;
    }

    // 페이지네이션
    public Map<String, Object> getItems(String searchQuery, int page, int size) {
        int offset = (page - 1) * size;
        List<ETLItemsVo> items = mariaDbMapper.getPaginatedItems(searchQuery, size, offset);
        long totalItems = mariaDbMapper.getTotalItems(searchQuery);

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalItems", totalItems);
        response.put("itemsPerPage", size);

        return response;
    }
    //위해상품+페이지네이션
    public Map<String, Object> getDngItems(String searchQuery, int page, int size) {
        int offset = (page - 1) * size;
        List<ETLItemsVo> items = mariaDbMapper.getPaginatedDngItems(searchQuery, size, offset);
        long dngItems = mariaDbMapper.getDngItems(searchQuery);

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalItems", dngItems);
        response.put("itemsPerPage", size);

        return response;
    }
    //일반상품+페이지네이션
    public Map<String, Object> getNormItems(String searchQuery, int page, int size) {
        int offset = (page - 1) * size;
        List<ETLItemsVo> items = mariaDbMapper.getPaginatedNormItems(searchQuery, size, offset);
        long normItems = mariaDbMapper.getNormItems(searchQuery);

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalItems", normItems);
        response.put("itemsPerPage", size);

        return response;
    }

    //데이터 삽입하기(등록)
    public ETLItemsVo insertEtlItem(ETLItemsVo etlItemsVo, MultipartFile file) throws IOException {
        // 1. 데이터베이스 ID 생성
        Long maxMstDataId = mariaDbMapper.getMaxMstDataId();
        etlItemsVo.setMstDataId(maxMstDataId + 1);

        Long maxDocNo = mariaDbMapper.getMaxDocNo();
        etlItemsVo.setDocNo(maxDocNo + 1);

        // 2. 파일 저장 처리
        if (file != null && !file.isEmpty()) {
            // 고유한 파일 이름 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadDir = "C:/tmp/uploads/";
            Path filePath = Paths.get(uploadDir, fileName);

            // 디렉토리 생성 및 파일 저장
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // 이미지 URL 설정
            String imgUrl = "/uploads/" + fileName;
            etlItemsVo.setImgUrl(imgUrl);
        }

        // 3. 데이터 삽입
        mariaDbMapper.insertEtlItem(etlItemsVo);

        // 4. 삽입된 객체 반환
        return etlItemsVo;
    }

    public void updateEtlItem(Long id, ETLItemsVo etlItemsVo, MultipartFile file) throws IOException {
        if (id == null || etlItemsVo == null || etlItemsVo.getMstDataId() == null) {
            throw new IllegalArgumentException("잘못된 요청 데이터입니다.");
        }

        // 기존 데이터 가져오기
        ETLItemsVo existingItem = (ETLItemsVo) mariaDbMapper.findById(id);
        if (existingItem == null) {
            throw new IllegalArgumentException("해당 ID의 데이터가 존재하지 않습니다.");
        }

        // 1. 파일 업데이트 처리
        if (file != null && !file.isEmpty()) {
            // 기존 파일 삭제 (올바른 파일 경로만 사용)
            if (existingItem.getImgUrl() != null && !existingItem.getImgUrl().startsWith("blob:")) {
                Path oldFilePath = Paths.get("C:/tmp/uploads/", existingItem.getImgUrl().replace("/uploads/", ""));
                Files.deleteIfExists(oldFilePath);
            }

            // 새 파일 저장
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadDir = "C:/tmp/uploads/";
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // 새로운 이미지 URL 설정
            etlItemsVo.setImgUrl("/uploads/" + fileName);
        } else {
            // 파일이 업로드되지 않았다면 기존 URL 유지 (blob: URL 제외)
            if (existingItem.getImgUrl() != null && existingItem.getImgUrl().startsWith("blob:")) {
                etlItemsVo.setImgUrl(null);  // 기존 blob: URL은 무시
            } else {
                etlItemsVo.setImgUrl(existingItem.getImgUrl());
            }
        }

        // 2. 데이터 업데이트 실행
        mariaDbMapper.updateEtlItem(id, etlItemsVo);
    }

    //데이터삭제
    public void deleteEtlItem(Long id) throws Exception {
        // 기존 데이터 조회
        ETLItemsVo existingItem = mariaDbMapper.findById(id);
        if (existingItem == null) {
            throw new IllegalArgumentException("해당 ID의 데이터가 존재하지 않습니다.");
        }

        // 기존 이미지 파일 삭제 (파일이 존재할 경우)
        if (existingItem.getImgUrl() != null && !existingItem.getImgUrl().startsWith("blob:")) {
            Path filePath = Paths.get("C:/tmp/uploads/", existingItem.getImgUrl().replace("/uploads/", ""));
            Files.deleteIfExists(filePath);
        }

        // 데이터베이스에서 삭제
        mariaDbMapper.deleteById(id);
    }


}