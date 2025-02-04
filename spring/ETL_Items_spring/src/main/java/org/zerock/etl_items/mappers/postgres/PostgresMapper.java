package org.zerock.etl_items.mappers.postgres;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zerock.etl_items.domain.TbOprGdsLVo;

import java.util.List;

@Mapper
public interface PostgresMapper {

    List<TbOprGdsLVo> getList();

    List<TbOprGdsLVo> getPaginatedItems(@Param("searchQuery") String searchQuery,
                                        @Param("size") int size,
                                        @Param("offset") int offset);

    long getTotalItems(@Param("searchQuery") String searchQuery);

}

