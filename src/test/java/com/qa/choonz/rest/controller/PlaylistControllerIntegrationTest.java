package com.qa.choonz.rest.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.choonz.rest.dto.PlaylistDTO;
import com.qa.choonz.persistence.domain.Playlist;
import com.qa.choonz.persistence.repository.PlaylistRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class PlaylistControllerIntegrationTest {
	
	 	@Autowired
	    private MockMvc mock;

	    @Autowired
	    private PlaylistRepository repository;

	    @Autowired
	    private ModelMapper modelMapper;

	    @Autowired
	    private ObjectMapper objectMapper;

	    private Playlist testPlaylist; 
	    private Playlist testPlaylistWithId;
	    private PlaylistDTO PlaylistDTO;

	    private Long id;

		private String testName = "Party";
	    private String testDesc = "Dance CLassics";
	    private String testArtwork = "None";

	    private PlaylistDTO mapToDTO(Playlist playlist) {
	        return this.modelMapper.map(playlist, PlaylistDTO.class);
	    }

	    @BeforeEach
	    void init() {
	        this.repository.deleteAll();

	        this.testPlaylist = new Playlist(testName,testDesc,testArtwork);
	        this.testPlaylistWithId = this.repository.save(this.testPlaylist);
	        this.PlaylistDTO = this.mapToDTO(testPlaylistWithId);
	        this.id = this.testPlaylistWithId.getId();
	        this.testName = this.testPlaylistWithId.getName();
	    }

	    @Test
	    void testCreate() throws Exception {
	        this.mock
	                .perform(request(HttpMethod.POST, "/playlists/create").contentType(MediaType.APPLICATION_JSON)
	                        .content(this.objectMapper.writeValueAsString(testPlaylist))
	                        .accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isCreated())
	                .andExpect(content().json(this.objectMapper.writeValueAsString(PlaylistDTO)));
	    }

	    @Test
	    void testReadOne() throws Exception {
	        this.mock.perform(request(HttpMethod.GET, "/playlists/read/" + this.id).accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(content().json(this.objectMapper.writeValueAsString(this.PlaylistDTO)));
	    }

	    @Test
	    void testReadAll() throws Exception {
	        List<PlaylistDTO> playlist = new ArrayList<>();
	        playlist.add(this.PlaylistDTO);

	        String content = this.mock
	                .perform(request(HttpMethod.GET, "/playlists/read").accept(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

	        assertEquals(this.objectMapper.writeValueAsString(playlist), content);
	    }

	    @Test
	    void testUpdate() throws Exception {
	    	PlaylistDTO newPl = new PlaylistDTO(testName, testDesc, testArtwork);
	    	Playlist updatedPl = new Playlist(newPl.getName(),newPl.getDescription(),newPl.getArtwork());
	        updatedPl.setId(this.id);

	        String result = this.mock
	                .perform(request(HttpMethod.PUT, "/playlists/update/" + this.id).accept(MediaType.APPLICATION_JSON)
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(this.objectMapper.writeValueAsString(newPl)))
	                .andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

	        assertEquals(this.objectMapper.writeValueAsString(this.mapToDTO(updatedPl)), result);
	    }

	    @Test
	    void testDelete() throws Exception {
	        this.mock.perform(request(HttpMethod.DELETE, "/playlists/delete/" + this.id)).andExpect(status().isNoContent());
	    }

}
