package com.rewcode.blog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewcode.blog.entity.Post;
import com.rewcode.blog.mapper.PostMapper;
import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.repository.PostRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class PostControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostMapper postMapper;

    private PostDto postDtoExample;

    @BeforeEach
    public void setup(){
        postDtoExample = new PostDto();
        postDtoExample.setTitle("Title Demo");
        postDtoExample.setContent("Content Demo");
        postDtoExample.setDescription("Description Demo");
    }

    @DisplayName("Integration test for save Post request")
    @Test
    @Rollback
    public void givenPost_whenSavePost_thenReturnPost() throws Exception {
        //given - precondition or setup

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDtoExample)));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(postDtoExample.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(postDtoExample.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(postDtoExample.getContent())))
                .andDo(MockMvcResultHandlers.print());
    }


    @DisplayName("Integration test for get all Posts with default query params request")
    @Test
    @Rollback
    public void givenPostsListWithDefaults_whenGetAllPosts_thenReturnPostsResponse() throws Exception {
        //given - precondition or setup
        PostDto postDtoExample2 = new PostDto();
        postDtoExample2.setTitle("Title demo 2");
        postDtoExample2.setDescription("Description demo 2");
        postDtoExample2.setContent("Content demo 2");

        postRepository.saveAll(List.of(postMapper.convertToPost(postDtoExample), postMapper.convertToPost(postDtoExample2)));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", CoreMatchers.is(1)))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for get all Posts with default query params request")
    @Test
    @Rollback
    public void givenPostsListPaginationAndSize_whenGetAllPosts_thenReturnPostsResponse() throws Exception {
        //given - precondition or setup
        PostDto postDtoExample2 = new PostDto();
        postDtoExample2.setTitle("Title demo 2");
        postDtoExample2.setDescription("Description demo 2");
        postDtoExample2.setContent("Content demo 2");

        postRepository.saveAll(List.of(postMapper.convertToPost(postDtoExample), postMapper.convertToPost(postDtoExample2)));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts?pageSize=1&pageNo=0"));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.last", CoreMatchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", CoreMatchers.is(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for get all Posts with default query params request")
    @Test
    @Rollback
    public void givenPostsListPaginationSizeSortBy_whenGetAllPosts_thenReturnPostsResponse() throws Exception {
        //given - precondition or setup
        PostDto postDtoExample2 = new PostDto();
        postDtoExample2.setTitle("Demo Title 2");
        postDtoExample2.setDescription("Description demo 2");
        postDtoExample2.setContent("Content demo 2");

        postRepository.saveAll(List.of(postMapper.convertToPost(postDtoExample), postMapper.convertToPost(postDtoExample2)));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts?pageSize=1&pageNo=0&sortBy=title&sortDirection=asc"));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title", CoreMatchers.is(postDtoExample2.getTitle())))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for get all Posts with default query params request")
    @Test
    @Rollback
    public void givenPostsListPaginationSizeSortBySortDir_whenGetAllPosts_thenReturnPostsResponse() throws Exception {
        //given - precondition or setup
        PostDto postDtoExample2 = new PostDto();
        postDtoExample2.setTitle("Demo Title 2");
        postDtoExample2.setDescription("Description demo 2");
        postDtoExample2.setContent("Content demo 2");

        postRepository.saveAll(List.of(postMapper.convertToPost(postDtoExample), postMapper.convertToPost(postDtoExample2)));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts?pageSize=1&pageNo=0"));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title", CoreMatchers.is(postDtoExample.getTitle())))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for get Post by id request")
    @Test
    @Rollback
    public void givenPostId_whenGetPostById_thenReturnPost() throws Exception {
        //given - precondition or setup

        Post savedPost = postRepository.save(postMapper.convertToPost(postDtoExample));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", savedPost.getId()));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(postDtoExample.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(postDtoExample.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(postDtoExample.getContent())))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration negative test for get Post by id request")
    @Test
    @Rollback
    public void givenWrongPostId_whenGetPostById_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Long incorrectId = -1L;

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}", incorrectId));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for update Post request")
    @Test
    @Rollback
    public void givenPostIdAndPost_whenUpdatePost_thenReturnUpdatedPost() throws Exception {
        //given - precondition or setup
        Post savedPost = postRepository.save(postMapper.convertToPost(postDtoExample));

        PostDto updatedPost = postMapper.converToPostDto(savedPost);
        updatedPost.setTitle("updated title");
        updatedPost.setDescription("updated description");
        updatedPost.setContent("updated content");

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPost)));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(updatedPost.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(updatedPost.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", CoreMatchers.is(updatedPost.getContent())))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration negative test for update Post request")
    @Test
    @Rollback
    public void givenPostIdAndPost_whenUpdatePost_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Long incorrectId = -1L;

        Post savedPost = postRepository.save(postMapper.convertToPost(postDtoExample));

        PostDto updatedPost = postMapper.converToPostDto(savedPost);
        updatedPost.setTitle("updated title");
        updatedPost.setDescription("updated description");
        updatedPost.setContent("updated content");

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/{id}", incorrectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPost)));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for delete Post by id request")
    @Test
    @Rollback
    public void givenPostId_whenDeletePostById_thenReturnSuccessfullyString() throws Exception {
        //given - precondition or setup
        Post savedPost = postRepository.save(postMapper.convertToPost(postDtoExample));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", savedPost.getId()));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration negative test for delete Post by id request")
    @Test
    @Rollback
    public void givenPostId_whenDeletePostById_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Long incorrectId = -1L;
        postRepository.save(postMapper.convertToPost(postDtoExample));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", incorrectId));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }



}
