package com.rewcode.blog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewcode.blog.entity.Comment;
import com.rewcode.blog.entity.Post;
import com.rewcode.blog.mapper.CommentMapper;
import com.rewcode.blog.payload.CommentDto;
import com.rewcode.blog.repository.CommentRepository;
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

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
public class CommentControllerIT {

    private final String AUTH_KEY = "Authorization";
    private final String AUTH_TYPE = "Basic";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentMapper commentMapper;
  
    private CommentDto sampleCommentDto;
    private Post samplePost;

    private Map<String, String> headerValues = new HashMap<>();

    @BeforeEach
    public void setup(){
        String authValue = Base64.getEncoder()
                .encodeToString(new String("orlando:root").getBytes());
        headerValues.put(AUTH_KEY, new StringBuilder()
                .append(AUTH_TYPE)
                .append(" ")
                .append(authValue).toString());

        sampleCommentDto = new CommentDto();
        sampleCommentDto.setName("Name");
        sampleCommentDto.setBody("Body sample sample");
        sampleCommentDto.setEmail("Email@email.com");

        samplePost = new Post();
        samplePost.setTitle("Title");
        samplePost.setDescription("Description");
        samplePost.setContent("Content");
    }

    @DisplayName("Integration test for save Comment request ")
    @Test
    @Rollback
    public void givenPostIdAndComment_whenCreateComment_thenReturnComment() throws Exception {
        //given - precondition or setup
        Post savedPost = postRepository.save(samplePost);

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/posts/{postId}/comments",savedPost.getId())
                        .content(objectMapper.writeValueAsString(sampleCommentDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTH_KEY, headerValues.get(AUTH_KEY))
        );
        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sampleCommentDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", CoreMatchers.is(sampleCommentDto.getBody())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(sampleCommentDto.getEmail())))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for get all Comment by Post Id request ")
    @Test
    @Rollback
    public void givenPostId_whenGetAllComments_thenReturnCommentList() throws Exception {
        //given - precondition or setup
        Post savedPost = postRepository.save(samplePost);
        Comment anotherComment = new Comment();
        anotherComment.setName("Name2");
        anotherComment.setEmail("Email2");
        anotherComment.setBody("Body2");
        anotherComment.setPost(savedPost);

        commentRepository.saveAll(List.of(commentMapper.convertToComment(sampleCommentDto, savedPost), anotherComment));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{postId}/comments",savedPost.getId())
                .header(AUTH_KEY, headerValues.get(AUTH_KEY)));
        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for get Comment by Post Id request ")
    @Test
    @Rollback
    public void givenPostIdAndCommentId_whenGetCommentById_thenReturnComment() throws Exception {
        //given - precondition or setup
        Post savedPost = postRepository.save(samplePost);
        Comment savedComment = commentRepository.save(commentMapper.convertToComment(sampleCommentDto, savedPost));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/posts/{postId}/comments/{commentId}",savedPost.getId(), savedComment.getId())
                .header(AUTH_KEY, headerValues.get(AUTH_KEY)));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sampleCommentDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", CoreMatchers.is(sampleCommentDto.getBody())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(sampleCommentDto.getEmail())))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for get Comment by Post Id request that does not belong to Post")
    @Test
    @Rollback
    public void givenWrongPostIdAndCommentId_whenGetCommentById_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Post anotherPost = new Post();
        anotherPost.setTitle("Title2");
        anotherPost.setDescription("Description2");
        anotherPost.setContent("Content2");


        Post savedPost = postRepository.save(samplePost);
        Post savedPost2 = postRepository.save(anotherPost);

        Comment savedComment = commentRepository.save(commentMapper.convertToComment(sampleCommentDto, savedPost));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/posts/{postId}/comments/{commentId}",savedPost2.getId(), savedComment.getId())
                .header(AUTH_KEY, headerValues.get(AUTH_KEY)));

        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }


    @DisplayName("Integration test for update Comment request ")
    @Test
    @Rollback
    public void givenPostIdCommentIdAndComment_whenUpdateComment_thenReturnUpdatedComment() throws Exception {
        //given - precondition or setup
        Post savedPost = postRepository.save(samplePost);

        Comment savedComment = commentRepository.save(commentMapper.convertToComment(sampleCommentDto, savedPost));
        CommentDto updatedCommentDto = commentMapper.convertToCommentDto(savedComment);
      
        updatedCommentDto.setName("Name2");
        updatedCommentDto.setBody("Body2 sample sample");
        updatedCommentDto.setEmail("Email2@gmail.com");

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/posts/{postId}/comments/{commentId}",savedPost.getId(), savedComment.getId())
                .content(objectMapper.writeValueAsString(updatedCommentDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTH_KEY, headerValues.get(AUTH_KEY))
        );
        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(updatedCommentDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", CoreMatchers.is(updatedCommentDto.getBody())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedCommentDto.getEmail())))
                .andDo(MockMvcResultHandlers.print());
    }


    @DisplayName("Integration test for delete Comment request ")
    @Test
    @Rollback
    public void givenPostIdAndCommentId_whenDeleteComment_thenReturnSuccessString() throws Exception {
        //given - precondition or setup
        Post savedPost = postRepository.save(samplePost);
        Comment savedComment = commentRepository.save(commentMapper.convertToComment(sampleCommentDto, savedPost));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/posts/{postId}/comments/{commentId}",savedPost.getId(), savedComment.getId())
                .header(AUTH_KEY, headerValues.get(AUTH_KEY)));
        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Integration test for negative delete Comment request ")
    @Test
    @Rollback
    public void givenPostIdAndCommentId_whenDeleteComment_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Long wrongId = -1L;
        Post savedPost = postRepository.save(samplePost);

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/posts/{postId}/comments/{commentId}",savedPost.getId(), wrongId)
                .header(AUTH_KEY, headerValues.get(AUTH_KEY)));
        //then  - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
