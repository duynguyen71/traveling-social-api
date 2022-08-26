package com.tc.tcapi.repository;

import com.tc.tcapi.model.Post;
import com.tc.tcapi.model.PostComment;
import com.tc.tcapi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Optional<PostComment> findByIdAndUser(Long id, User user);

    List<PostComment> findByPost(Post post);

    List<PostComment> findByPostAndUserAndStatusAndParentIsNull(Post post, User user, Integer status);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM post_comment pc \n" +
                    "JOIN post p \n" +
                    "ON pc.post_id = p.id \n" +
                    "WHERE post_id = :postId \n" +
                    "AND pc.parent_comment_id IS NULL \n" +
                    "AND pc.status = :status \n"
    )
    List<PostComment> getPostCommentsNative(@Param("postId") Long postId, @Param("status") Integer status, Pageable pageable);

    List<PostComment> getByParentAndStatus( PostComment parent,Integer status);

    Integer countByPostAndStatusAndParentIsNull(Post post, Integer status);

    Integer countByPostAndStatus(Post post, Integer status);

    Integer countByParentAndStatus(PostComment parent,Integer status);

    Optional<PostComment> findByIdAndStatus(Long id, Integer status);
}
