package ait.forum.test;

import ait.forum.dao.Forum;
import ait.forum.dao.ForumImpl;
import ait.forum.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ForumTest {
    Forum forum;
    Post[] posts;
    Comparator<Post> comparator = (p1, p2) -> Integer.compare(p1.getPostId(), p2.getPostId());

    @BeforeEach
    void setUp() {
        forum = new ForumImpl();
        posts = new Post[6];
        posts[0] = new Post(1, "title1", "author1", "content1");
        posts[1] = new Post(2, "title2", "author2", "content2");
        posts[2] = new Post(3, "title3", "author1", "content3");
        posts[3] = new Post(4, "title4", "author3", "content4");
        posts[4] = new Post(5, "title5", "author1", "content5");
        posts[5] = new Post(6, "title6", "author2", "content6");
        posts[0].setDate(LocalDateTime.now().minusDays(6));
        posts[1].setDate(LocalDateTime.now().minusDays(9));
        posts[2].setDate(LocalDateTime.now().minusDays(5));
        posts[3].setDate(LocalDateTime.now().minusDays(7));
        posts[4].setDate(LocalDateTime.now().minusDays(10));
        posts[5].setDate(LocalDateTime.now().minusDays(8));
        for (int i = 0; i < posts.length - 1; i++) {
            forum.addPost(posts[i]);
        }
    }

    @Test
    void addPost() {
        assertFalse(forum.addPost(null));
        assertTrue(forum.addPost(posts[5]));
        assertEquals(6, forum.size());
        assertFalse(forum.addPost(posts[5]));
        assertEquals(6, forum.size());
    }

    @Test
    void removePost() {
        assertFalse(forum.removePost(9));
        assertTrue(forum.removePost(4));
        assertEquals(4, forum.size());
        assertFalse(forum.removePost(4));
        assertEquals(4, forum.size());
    }

    @Test
    void updatePost() {
        assertFalse(forum.updatePost(6, "content0"));
        assertTrue(forum.updatePost(4, "content0"));
        assertEquals("content0", forum.getPostById(4).getContent());
    }

    @Test
    void getPostById() {
        assertEquals(posts[3], forum.getPostById(4));
        assertNull(forum.getPostById(10));
    }

    @Test
    void testGetPostsByAuthorString() {
        Post[] actual = forum.getPostsByAuthor("author1");
        Arrays.sort(actual, comparator);
        Post[] expected = {posts[0], posts[2], posts[4]};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testGetPostsByAuthorStringLocalDateLocalDate() {
        Post[] actual = forum.getPostsByAuthor("author1", LocalDate.now().minusDays(8), LocalDate.now().minusDays(5));
        Arrays.sort(actual, comparator);
        Post[] expected = {posts[0], posts[2]};
        assertArrayEquals(expected, actual);
        Post[] actual1 = forum.getPostsByAuthor("author1", LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        Arrays.sort(actual, comparator);
        Post[] expected1 = new Post[0];
        assertArrayEquals(expected1, actual1);
    }

    @Test
    void size() {
        assertEquals(5, forum.size());
    }
}