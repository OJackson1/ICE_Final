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

import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.repository.PlaylistRepository;
import com.qa.choonz.rest.dto.PlaylistDTO;

@SpringBootTest
public class PlaylistServiceIntegrationTest {
		
		@InjectMocks
	    private PlaylistService service;

	    @Mock
	    private PlaylistRepository repo;

	    @Mock
	    private ModelMapper modelMapper;

	    private List<Playlist> playlist;

	    private Playlist testPlaylist;

	    private Playlist testPlaylistWithId;

	    private PlaylistDTO playlistDTO;

	    final long id = 1L;
	    
	    private PlaylistDTO mapToDTO(Playlist genre) {
	        return this.modelMapper.map(genre, PlaylistDTO.class);
	    }

	    @BeforeEach
	    public void init() {
	        this.playlist = new ArrayList<>();
	        this.playlist.add(testPlaylist);
	        this.testPlaylist = new Playlist("Summer");
	        this.testPlaylistWithId = new Playlist(testPlaylist.getName(),testPlaylist.getDescription(),testPlaylist.getArtwork());
	        this.testPlaylistWithId.setId(id);
	        this.playlistDTO = new ModelMapper().map(testPlaylistWithId, PlaylistDTO.class);
	    }

	    @Test
	    public void createPlaylistTest() {
	        when(this.repo.save(testPlaylist)).thenReturn(testPlaylistWithId);
	        when(this.modelMapper.map(testPlaylistWithId, PlaylistDTO.class)).thenReturn(playlistDTO);

	        assertEquals(this.playlistDTO, this.service.create(testPlaylist));

	        verify(this.repo, times(1)).save(this.testPlaylist);
	    }

	 
	    @Test
	    void ReadByIdTest() {
	        assertThat(this.playlistDTO)
	               .isEqualTo(this.service.read(this.id));
	        assertThat(this.service.read(this.testPlaylistWithId.getId()))
	        .isEqualTo(this.mapToDTO(this.testPlaylistWithId));
	    }

	    @Test
	    void ReadAllPlaylistTest() {
	        assertThat(this.service.read())
	                .isEqualTo(Stream.of(this.mapToDTO(testPlaylistWithId))
	                        .collect(Collectors.toList()));
	    }
	    
	    @Test
	    void testUpdate() {
	    	PlaylistDTO newPl = new PlaylistDTO(id, "Rock");
	    	PlaylistDTO updatedPl = new PlaylistDTO(this.id, newPl.getName());

	        assertThat(updatedPl)
	            .isEqualTo(this.service.update(newPl, this.id));
	    }
	    
	    @Test
	    void DeleteTest() {
	        assertThat(this.service.delete(this.id)).isTrue();
	    }

}
