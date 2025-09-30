package com.community.backend.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.community.backend.config.TestQueryDslConfig;
import com.community.backend.domain.post.dto.request.PostPagingRequestDto;
import com.community.backend.domain.post.entity.Post;
import com.community.backend.domain.post.repository.PostJpaRepository;
import com.community.backend.domain.post.repository.PostRepository;
import com.community.backend.domain.post.repository.PostRepositoryImpl;
import com.community.backend.domain.postcategory.entity.PostCategory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("test")
@Import({PostRepositoryImpl.class, TestQueryDslConfig.class})
class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostJpaRepository postJpaRepository;

	@PersistenceContext
	private EntityManager em;

	private PostCategory category;

	@BeforeEach
	void setUp() {
		// 테스트용 카테고리 생성
		category = new PostCategory(null, "name1", "name1");
		em.persist(category);

		// 테스트용 Post 10개 생성
		for (int i = 0; i < 10; i++) {
			Post post = new Post();
			post.setMemberIdx(1L);
			post.setCategory(category);
			post.setTitle("제목 " + i);
			post.setContents("내용 " + i);
			post.setViewCount(0);
			post.setState(1);

			postJpaRepository.save(post);
		}
	}

	@Test
	void getPosts_정상조회() {
		// given
		PostPagingRequestDto request = new PostPagingRequestDto();
		request.setMemberIdx(1L);
		request.setCategoryIdx(category.getIdx());

		PageRequest pageable = PageRequest.of(0, 5);

		// when
		Page<Post> result = postRepository.getPosts(request, pageable);

		// then
		result.forEach(post -> {
			System.out.println(post.getCategory().getName());
		});

		assertThat(result.getContent().size()).isGreaterThan(0);

	}
}
