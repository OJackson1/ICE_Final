package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Artist;
import com.qa.choonz.persistence.repository.ArtistRepository;
import com.qa.choonz.rest.dto.ArtistDTO;

@SpringBootTest
public class ArtistServiceIntegrationTest {

    @InjectMocks
    private ArtistService service;

    @Mock
    private ArtistRepository repo;

    @Mock
    private ModelMapper modelMapper;

    private List<Artist> artist;

    private Artist testArtist;

    private Artist testArtistWithId;

    private ArtistDTO ArtistDTO;

    final long id = 1L;
    
    private ArtistDTO mapToDTO(Artist artist) {
        return this.modelMapper.map(artist, ArtistDTO.class);
    }

    @BeforeEach
    public void init() {
        this.artist = new ArrayList<>();
        this.artist.add(testArtist);
        this.testArtist = new Artist("White Lies");
        this.testArtistWithId = new Artist(id, testArtist.getName());
        this.testArtistWithId.setId(id);
        this.ArtistDTO = new ModelMapper().map(testArtistWithId, ArtistDTO.class);
    }

    @Test
    public void createArtistTest() {
        when(this.repo.save(testArtist)).thenReturn(testArtistWithId);
        when(this.modelMapper.map(testArtistWithId, ArtistDTO.class)).thenReturn(ArtistDTO);

        assertEquals(this.ArtistDTO, this.service.create(testArtist));

        verify(this.repo, times(1)).save(this.testArtist);
    }


    @Test
    void ReadByIdTest() {
        assertThat(this.ArtistDTO)
               .isEqualTo(this.service.read(this.id));
        assertThat(this.service.read(this.testArtistWithId.getId()))
        .isEqualTo(this.mapToDTO(this.testArtistWithId));
    }

    @Test
    void ReadAllArtistTest() {
        assertThat(this.service.read())
                .isEqualTo(Stream.of(this.mapToDTO(testArtistWithId))
                        .collect(Collectors.toList()));
    }
  
    @Test
    void testUpdate() {
    	ArtistDTO newArtist = new ArtistDTO(id, "Foals");
    	ArtistDTO updatedArtist = new ArtistDTO(this.id, newArtist.getName());

        assertThat(updatedArtist)
            .isEqualTo(this.service.update(newArtist, this.id));;
    }
    
    @Test
    void DeleteTest() {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
