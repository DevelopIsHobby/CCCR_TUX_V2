package com.chattest.service;

import com.chattest.dto.FeedDTO;
import com.chattest.dto.PublicDTO;
import com.chattest.entity.Feed;
import com.chattest.entity.Public;
import com.chattest.repository.PublicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {
    private final PublicRepository publicRepository;

    @Override
    public Public register(PublicDTO publicDTO) {
        log.info("service register....");
        log.info("DTO: " + publicDTO.toString());

        Public entity = dtoToEntityPublic(publicDTO);

        log.info("entity" + entity);
        publicRepository.save(entity);

        return entity;
    }

    @Override
    public List<PublicDTO> getListPublic() {
        List<Public> publicList = publicRepository.findAll();
        Function<Public, PublicDTO> fn = (entity -> entityToDtoPublic(entity));
        List<PublicDTO> publicDTOList = publicList.stream().map(fn).collect(Collectors.toList());
        return publicDTOList;
    }

    @Override
    public List<PublicDTO> getListPublicByAdmin() {
        List<Public> publicEntity = publicRepository.findByName();
        Function<Public, PublicDTO> fn = (entity -> entityToDtoPublic(entity));
        List<PublicDTO> publicDTOList = publicEntity.stream().map(fn).collect(Collectors.toList());
        return publicDTOList;
    }

    @Override
    public void removePublic(Long pno) {
        publicRepository.deleteById(pno);
    }

    @Override
    public Public modify(PublicDTO publicDTO) {
        Optional<Public> result = publicRepository.findById(publicDTO.getPno());
        Public publicEntity = null;

        if(result.isPresent()) {
            publicEntity = result.get();

            publicEntity.changeTitle(publicDTO.getTitle());
            publicEntity.changeContent(publicDTO.getContent());

            publicRepository.save(publicEntity);
        }

        return publicEntity;
    }


}

