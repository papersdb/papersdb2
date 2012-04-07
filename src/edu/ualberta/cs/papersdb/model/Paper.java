package edu.ualberta.cs.papersdb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ualberta.cs.papersdb.model.publication.Publication;

@Entity
@Table(name = "PAPER")
public class Paper implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String pubAbstract;
    private String keywords;
    private Date date;
    private String submittedBy;
    private String userTags;
    private String customRanking;
    private String doi;
    private Set<Collaboration> collaborations;
    private boolean isPublic = false;
    private Set<Author> authors;
    private Publication publication;
    private Set<Paper> relatedPapers;
    private Set<String> relatedUrls;
    private Set<String> attachments;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = "{edu.ualberta.cs.papersDb.model.PubEntry.title.NotEmpty}")
    @Column(name = "TITLE", length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "ABSTRACT", columnDefinition = "TEXT")
    public String getAbstract() {
        return pubAbstract;
    }

    public void setAbstract(String pubAbstract) {
        this.pubAbstract = pubAbstract;
    }

    @Column(name = "KEYWORDS", length = 255)
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "PUB_ENTRY_DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "SUBMITTED_BY", length = 255)
    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    @Column(name = "USER_TAGS", length = 255)
    public String getUserTags() {
        return userTags;
    }

    public void setUserTags(String userTags) {
        this.userTags = userTags;
    }

    @Column(name = "CUSTOM_RANKING", length = 255)
    public String getCustomRanking() {
        return customRanking;
    }

    public void setCustomRanking(String customRanking) {
        this.customRanking = customRanking;
    }

    @Column(name = "DOI")
    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    @ElementCollection(targetClass = Collaboration.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "PAPER_COLLABORATION", joinColumns = @JoinColumn(name = "PAPER_ID"))
    @Column(name = "COLLABORATION_ID", nullable = false)
    @Enumerated(EnumType.STRING)
    // @Type(type = "collaboration")
    public Set<Collaboration> getCollaborations() {
        return collaborations;
    }

    public void setCollaborations(Set<Collaboration> collaborations) {
        this.collaborations = collaborations;
    }

    @Column(name = "PUBLIC")
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AUTHOR_PAPER",
        joinColumns = { @JoinColumn(name = "PAPER_ID", nullable = false, updatable = false) },
        inverseJoinColumns = { @JoinColumn(name = "AUTHOR_ID", nullable = false, updatable = false) })
    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "paper", orphanRemoval = true)
    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PAPER_PAPER",
        joinColumns = { @JoinColumn(name = "FROM_PAPER_ID", nullable = false, updatable = false) },
        inverseJoinColumns = { @JoinColumn(name = "TO_PAPER_ID", nullable = false, updatable = false) })
    public Set<Paper> getRelatedPapers() {
        return relatedPapers;
    }

    public void setRelatedPapers(Set<Paper> relatedPapers) {
        this.relatedPapers = relatedPapers;
    }

    @ElementCollection
    @CollectionTable(name = "PAPER_URL", joinColumns = @JoinColumn(name = "PAPER_ID"))
    @Column(name = "URL")
    public Set<String> getRelatedUrls() {
        return relatedUrls;
    }

    public void setRelatedUrls(Set<String> relatedUrls) {
        this.relatedUrls = relatedUrls;
    }

    @ElementCollection
    @CollectionTable(name = "PAPER_ATTACHMENT", joinColumns = @JoinColumn(name = "PAPER_ID"))
    @Column(name = "ATTACHMENT")
    public Set<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<String> attachments) {
        this.attachments = attachments;
    }

}