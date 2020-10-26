package com.qa.choonz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.qa.choonz.exception.ArtistNotFoundException;
import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.utils.SAPIBeanUtils;

@Service
public class ArtistService {

    private ArtistRepository repo;
    private ModelMapper mapper;

    public ArtistService(ArtistRepository repo, ModelMapper mapper) {
        super();
        this.repo = repo;
        this.mapper = mapper;
    }

    private ArtistDTO mapToDTO(Artist artist) {
        return this.mapper.map(artist, ArtistDTO.class);
    }

    public ArtistDTO create(Artist artist) {
        Artist created = this.repo.save(artist);
        return this.mapToDTO(created);
    }

    public List<ArtistDTO> read() {
        return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ArtistDTO read(long id) {
        Artist found = this.repo.findById(id).orElseThrow(ArtistNotFoundException::new);
        return this.mapToDTO(found);
    }

    public ArtistDTO update(ArtistDTO artist, Long id) {
        Artist toUpdate = this.repo.findById(id).orElseThrow(ArtistNotFoundException::new);
        SAPIBeanUtils.mergeNotNull(artist,toUpdate);
        return this.mapToDTO(this.repo.save(toUpdate));
    }

    public boolean delete(long id) {
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }
}
