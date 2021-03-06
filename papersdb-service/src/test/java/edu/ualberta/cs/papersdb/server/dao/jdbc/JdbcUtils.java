package edu.ualberta.cs.papersdb.server.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.model.AuthorRanked;
import edu.ualberta.cs.papersdb.model.Collaboration;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Publisher;
import edu.ualberta.cs.papersdb.model.Ranking;
import edu.ualberta.cs.papersdb.model.publication.JournalPub;

public class JdbcUtils {

    private JdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    private SimpleJdbcInsert insertAuthor;

    private SimpleJdbcInsert insertAuthorRanked;

    private SimpleJdbcInsert insertPublication;

    private SimpleJdbcInsert insertPublisher;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAuthor = new SimpleJdbcInsert(dataSource).withTableName(
            "author").usingGeneratedKeyColumns("id");
        this.insertAuthorRanked = new SimpleJdbcInsert(dataSource)
            .withTableName("author_ranked").usingGeneratedKeyColumns("id");

        this.insertPublisher = new SimpleJdbcInsert(dataSource).withTableName(
            "publisher").usingGeneratedKeyColumns("id");

        this.insertPublication = new SimpleJdbcInsert(dataSource)
            .withTableName("publication").usingGeneratedKeyColumns("id");
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /*
     * Can also use: jdbcTemplate.queryForObject(sql, new Object[] { title },
     * new BeanPropertyRowMapper<Paper>(Paper.class));
     */
    public Paper getPaper(String title) {
        String sql = "SELECT * FROM paper WHERE title = ?";
        RowMapper<Paper> mapper = new RowMapper<Paper>() {
            @Override
            public Paper mapRow(ResultSet rs, int rowNum) throws SQLException {
                Paper paper = new Paper();
                paper.setId(rs.getLong("id"));
                paper.setTitle(rs.getString("title"));
                if (rs.getString("ranking_id") != null) {
                    paper
                        .setRanking(Ranking.valueOf(rs.getString("ranking_id")));
                }
                return paper;
            }
        };

        Paper paper = getJdbcTemplate().queryForObject(sql, mapper, title);
        Set<Collaboration> collaborations = new HashSet<Collaboration>(0);

        sql =
            "SELECT collaboration_id FROM paper_collaboration WHERE paper_id = ?";

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql,
            paper.getId());
        for (Map<String, Object> row : rows) {
            collaborations.add(Collaboration.valueOf((String) row
                .get("collaboration_id")));
        }

        paper.getCollaborations().addAll(collaborations);

        return paper;
    }

    public Author getAuthor(String names) {
        String sql = "select * from author where family_names = ?";
        RowMapper<Author> mapper = new RowMapper<Author>() {
            @Override
            public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
                Author author = new Author();
                author.setId(rs.getLong("id"));
                author.setEmail(rs.getString("email"));
                author.setFamilyNames(rs.getString("family_names"));
                author.setGivenNames(rs.getString("given_names"));
                author.setTitle(rs.getString("title"));
                return author;
            }

        };

        return getJdbcTemplate().queryForObject(sql, mapper, names);
    }

    public void addAuthor(Author author) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(
            author);
        Number newId = insertAuthor.executeAndReturnKey(parameters);
        author.setId(newId.longValue());
    }

    public void addAuthorRanked(AuthorRanked authorRanked) {
        Map<String, Object> parameters = new HashMap<String, Object>(0);
        parameters.put("rank", authorRanked.getRank());
        parameters.put("author_id", authorRanked.getAuthor().getId());
        Number newId = insertAuthorRanked.executeAndReturnKey(parameters);
        authorRanked.setId(newId.longValue());
    }

    public void addPublisher(Publisher publisher) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(
            publisher);
        Number newId = insertPublisher.executeAndReturnKey(parameters);
        publisher.setId(newId.longValue());
    }

    public void addPublication(final JournalPub publication) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("publication").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<String, Object>(0);

        parameters.put("discriminator", JournalPub.class.getSimpleName());
        parameters.put("name", publication.getName());
        parameters.put("date", new java.sql.Date(publication.getPublishedDate()
            .getTime()));
        parameters.put("paper_id", publication.getPaper().getId());
        parameters.put("publisher_id", publication.getPublisher().getId());

        Number newId = jdbcInsert.executeAndReturnKey(parameters);
        publication.setId(newId.longValue());
    }

}
