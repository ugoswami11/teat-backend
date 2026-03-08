package com.teat.teat_backend.mapper;

import com.teat.teat_backend.dto.response.TestRunDTO;
import com.teat.teat_backend.dto.response.TestCaseSummaryDTO;
import com.teat.teat_backend.entity.TestRun;
import com.teat.teat_backend.entity.TestCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for TestRun entity to DTO conversions.
 */
@Mapper(componentModel = "spring")
public interface TestRunMapper {

    @Mapping(target = "createdBy", expression = "java(entity.getCreatedBy() != null ? entity.getCreatedBy().toString() : null)")
    TestRunDTO toDTO(TestRun entity);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TestRun toEntity(TestRunDTO dto);

    TestCaseSummaryDTO toTestCaseSummaryDTO(TestCase testCase);
}
