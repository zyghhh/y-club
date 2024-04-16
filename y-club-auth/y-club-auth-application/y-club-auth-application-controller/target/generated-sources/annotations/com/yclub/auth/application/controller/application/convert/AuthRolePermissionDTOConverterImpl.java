package com.yclub.auth.application.controller.application.convert;

import com.yclub.auth.application.controller.application.dto.AuthRolePermissionDTO;
import com.yclub.auth.domain.entity.AuthRolePermissionBO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T14:05:47+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_212 (Oracle Corporation)"
)
public class AuthRolePermissionDTOConverterImpl implements AuthRolePermissionDTOConverter {

    @Override
    public AuthRolePermissionBO convertDTOToBO(AuthRolePermissionDTO authRolePermissionDTO) {
        if ( authRolePermissionDTO == null ) {
            return null;
        }

        AuthRolePermissionBO authRolePermissionBO = new AuthRolePermissionBO();

        authRolePermissionBO.setId( authRolePermissionDTO.getId() );
        authRolePermissionBO.setRoleId( authRolePermissionDTO.getRoleId() );
        authRolePermissionBO.setPermissionId( authRolePermissionDTO.getPermissionId() );

        return authRolePermissionBO;
    }
}
