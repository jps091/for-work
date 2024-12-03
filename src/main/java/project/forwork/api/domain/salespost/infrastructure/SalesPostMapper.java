package project.forwork.api.domain.salespost.infrastructure;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.forwork.api.domain.salespost.infrastructure.model.SalesPostSearchDto;

import java.util.List;

@Mapper
public interface SalesPostMapper {
    List<SalesPostSearchDto> searchByText(
            @Param("textCond") String textCond,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset
    );

    List<SalesPostSearchDto> searchByTextWithLike(
            @Param("textCond") String textCond,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset
    );
}
