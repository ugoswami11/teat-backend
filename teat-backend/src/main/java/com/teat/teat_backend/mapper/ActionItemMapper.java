package com.teat.teat_backend.mapper;

import com.teat.teat_backend.dto.response.ActionItemDTO;
import com.teat.teat_backend.dto.response.ActionItemResolutionSummaryDTO;
import com.teat.teat_backend.entity.ActionItem;
import com.teat.teat_backend.entity.ActionItemResolution;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for ActionItem entity to DTO conversions.
 */
@Mapper(componentModel = "spring")
public interface ActionItemMapper {

    ActionItemDTO toDTO(ActionItem entity);

    ActionItemResolutionSummaryDTO toResolutionSummaryDTO(ActionItemResolution entity);
}
