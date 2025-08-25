package elearningspringboot.mapper;

import elearningspringboot.dto.request.PermissionRequest;
import elearningspringboot.dto.response.PermissionResponse;
import elearningspringboot.entity.Permission;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity(PermissionRequest request);
    PermissionResponse toDTO(Permission entity);
    List<PermissionResponse> toListDTOs(List<Permission> entities);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PermissionRequest request, @MappingTarget Permission entity);

}
