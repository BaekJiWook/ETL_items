package org.zerock.etl_items.mappers.mariadb;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zerock.etl_items.domain.ETLItemsVo;
import org.zerock.etl_items.domain.TbOprGdsLVo;

import java.util.List;

@Mapper
public interface MariaDbMapper {

    // MariaDB에 데이터 삽입 (XML에서 정의된 쿼리 호출)
    void insertMariaDBData(ETLItemsVo etlItemsVo);

    // 데이터 전체삭제(ETL할거니까 리프레시)
    void deleteAllData();

    // MariaDB에서 데이터를 조회하는 메소드
    List<ETLItemsVo> getMariaDbData();
    List<ETLItemsVo> getPaginatedItems(@Param("searchQuery") String searchQuery,
                                       @Param("size") int size,
                                       @Param("offset") int offset);
    long getTotalItems(@Param("searchQuery") String searchQuery);

    //위해상품 조회
    List<ETLItemsVo> getDngMariaDbData();
    List<ETLItemsVo> getPaginatedDngItems(@Param("searchQuery") String searchQuery,
                                          @Param("size") int size,
                                          @Param("offset") int offset);
    long getDngItems(@Param("searchQuery") String searchQuery);
    List<ETLItemsVo> findDngItemsById(@Param("id") String id);

    //일반상품 조회
    List<ETLItemsVo> getNormMariaDbData();
    List<ETLItemsVo> getPaginatedNormItems(@Param("searchQuery") String searchQuery,
                                           @Param("size") int size,
                                           @Param("offset") int offset);
    long getNormItems(@Param("searchQuery") String searchQuery);
    List<ETLItemsVo> findNormItemsById(@Param("id") String id);

    //등록하기 insert
    Long getMaxDocNo();     // 최대 doc_no 값 반환
    Long getMaxMstDataId(); // 최대 mst_data_id 값 반환
    void insertEtlItem(ETLItemsVo etlItemsVo);

    // 데이터 수정 메서드 (수정할 데이터를 ETLItemsVo 객체로 전달)
    void updateEtlItem(@Param("mstDataId") Long mstDataId, @Param("etlItem") ETLItemsVo etlItemsVo);
    ETLItemsVo findById(@Param("id") Long id);

    //데이터 삭제하기
    void deleteById(@Param("id") Long id);

}