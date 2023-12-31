package com.example.demo.repository;

import com.example.demo.entity.Community;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommunityRepositoryTests {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private UserRepository userRepository;

    private Community community1;
    private User creator;

    @BeforeEach
    void setup() {
        creator = User.builder()
                .username("author")
                .email("test@test.com")
                .password("test")
                .build();
        userRepository.save(creator);

        community1 = Community.builder()
                .name("Community 1")
                .creator(creator)
                .build();
        communityRepository.save(community1);

        Community community2 = Community.builder()
                .name("Community 2")
                .creator(creator)
                .build();
        communityRepository.save(community2);
    }

    @Test
    void testFindAll() {
        List<Community> communities = communityRepository.findAll();

        assertThat(communities).isNotNull();
        assertThat(communities.size()).isGreaterThan(0);
    }

    @Test
    void testFindById_Success() {
        Community retrievedCommunity = communityRepository.findById(community1.getId()).orElseThrow();

        assertThat(retrievedCommunity).isNotNull();
        assertThat(retrievedCommunity.getName()).isEqualTo("Community 1");
    }

    @Test
    void testFindById_WhenCommunityNotFound_ThrowsResourceNotFoundException() {
        Long nonExistentId = -1L;

        assertThrows(ResourceNotFoundException.class, () -> communityRepository.findById(nonExistentId)
                .orElseThrow(() -> new ResourceNotFoundException("Community not found")));
    }

    @Test
    public void testFindByName() {
        Community community = communityRepository.findByName(community1.getName()).orElseThrow();

        assertThat(community).isNotNull();
        assertThat(community.getId()).isGreaterThan(0);
        assertThat(community.getName()).isEqualTo("Community 1");
    }

    @Test
    void testSaveCommunity() {
        Community newCommunity = Community.builder()
                .name("New Community")
                .creator(creator)
                .build();

        communityRepository.save(newCommunity);

        Community retrievedCommunity = communityRepository.findById(newCommunity.getId()).orElseThrow();

        assertThat(retrievedCommunity).isNotNull();
        assertThat(retrievedCommunity.getId()).isGreaterThan(0);
        assertThat(retrievedCommunity.getName()).isEqualTo("New Community");
        assertThat(communityRepository.count()).isGreaterThan(0);
    }

}
