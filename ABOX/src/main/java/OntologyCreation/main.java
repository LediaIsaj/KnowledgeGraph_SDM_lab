package OntologyCreation;

import OntologyCreation.Ontology.ABOX;

public class main
{
    public static void main(String[] args) throws Exception {
        ABOX.createAuthor();
        ABOX.createAffiliation();
        ABOX.createArticle();
        ABOX.createProceeding();
        ABOX.createKeyword();
        ABOX.createTopic();
    }
}
