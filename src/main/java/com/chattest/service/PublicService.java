package com.chattest.service;

import com.chattest.dto.PublicDTO;
import com.chattest.entity.Public;

import java.util.List;

public interface PublicService {
    Public register(PublicDTO publicDTO);

    List<PublicDTO> getListPublic();

    List<PublicDTO> getListPublicByAdmin();

    void removePublic(Long pno);

    Public modify(PublicDTO publicDTO);

    default Public dtoToEntityPublic(PublicDTO publicDTO) {
        Public publicEntity = Public.builder()
                .pno(publicDTO.getPno())
                .title(publicDTO.getTitle())
                .content(publicDTO.getContent())
                .writer(publicDTO.getWriter())
                .build();
        return publicEntity;
    }

    default PublicDTO entityToDtoPublic(Public publicEntity) {
        PublicDTO publicDTO = PublicDTO.builder()
                .pno(publicEntity.getPno())
                .title(publicEntity.getTitle())
                .content(publicEntity.getContent())
                .writer(publicEntity.getWriter())
                .regDate(publicEntity.getRegDate())
                .modDate(publicEntity.getModDate())
                .build();

        return publicDTO;
    }
}
