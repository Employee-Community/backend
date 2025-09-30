package com.community.backend.unit;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.community.backend.config.TestQueryDslConfig;
import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.member.enums.MemberRole;
import com.community.backend.domain.post.entity.Post;
import com.community.backend.domain.post.entity.PostComment;
import com.community.backend.domain.post.repository.PostCommentJpaRepository;
import com.community.backend.domain.post.repository.PostJpaRepository;
import com.community.backend.domain.post.repository.PostRepository;
import com.community.backend.domain.post.repository.PostRepositoryImpl;
import com.community.backend.domain.postcategory.entity.PostCategory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@ActiveProfiles("test")
@Import({PostRepositoryImpl.class, TestQueryDslConfig.class})
public class PostCommentRepositoryTest {

	@Autowired
	private PostCommentJpaRepository postCommentJpaRepository;

	@PersistenceContext
	private EntityManager em;

	private Post post;

	@BeforeEach
	void setUp() {

		// category 저장
		PostCategory category = new PostCategory(null, "name1", "name1");
		em.persist(category);

		// 게시글 저장
		post = new Post();
		post.setMemberIdx(1L);
		post.setCategory(category);
		post.setTitle("제목 ");
		post.setContents("내용 ");
		post.setViewCount(0);
		post.setState(1);
		em.persist(post);

		// 댓글 여러 개 저장
		for (int i = 0; i < 5; i++) {

			Member member = Member.createDefault("member"+i, "member"+i, "member"+i,
				"member@email.com"+i, "0101234567" + i, "member"+i, MemberRole.FREE);
			em.persist(member);

			PostComment comment = new PostComment(null, member, post, "content" + i, 1);
			postCommentJpaRepository.save(comment);
		}

		em.flush();
		em.clear();
	}

	@Test
	void findByPostIdx_ShouldCauseNPlusOne() {
		PageRequest pageable = PageRequest.of(0, 5);

		Page<PostComment> comments = postCommentJpaRepository.findByPost_Idx(post.getIdx(), pageable);

		comments.forEach(c -> {
			System.out.println("comment: " + c.getContent() + ", member: " + c.getMember().getName());
		});

		assertThat(comments.getTotalElements()).isEqualTo(5);
	}
}
