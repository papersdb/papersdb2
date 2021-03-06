package edu.ualberta.cs.papersdb.persist.dao;

import java.util.Set;

import edu.ualberta.cs.papersdb.model.Paper;

public interface PaperDAO extends GenericDAO<Paper, Long> {

    Paper getByTitle(String title);

    Set<Paper> getPapersTitleMatching(String match, int start, int max);

    Set<Paper> getForAuthor(long authorId, int start, int max);

    Paper getByDoi(String doi);

}
