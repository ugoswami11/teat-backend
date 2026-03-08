package com.teat.teat_backend.mapper;

import com.teat.teat_backend.dto.response.TestCaseDTO;
import com.teat.teat_backend.dto.response.TestStepDTO;
import com.teat.teat_backend.entity.TestCase;
import com.teat.teat_backend.entity.TestStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for TestCase entity to DTO conversions.
 */
@Mapper(componentModel = "spring")
public interface TestCaseMapper {

    @Mapping(target = "createdBy", expression = "java(entity.getCreatedBy() != null ? entity.getCreatedBy().toString() : null)")
    TestCaseDTO toDTO(TestCase entity);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TestCase toEntity(TestCaseDTO dto);

    TestStepDTO toTestStepDTO(TestStep testStep);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TestStep toTestStepEntity(TestStepDTO dto);
}
