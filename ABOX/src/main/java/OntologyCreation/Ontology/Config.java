package OntologyCreation.Ontology;

public class Config {
    public static final String CSV_SPLIT_REGEX = ";(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    public static final String OUTPUT_PATH = "src/main/resources/out/";

    public static final String BASE_URL = "http://www.SDMLab3.org/ontologies/2020/3/research_articles/";
//    public static final String PROPERTY_URL = BASE_URL+"property/";
    public static final String PROPERTY_URL = BASE_URL+"#";
//    public static final String RESOURCE_URL = BASE_URL+"resource/";
    public static final String RESOURCE_URL = BASE_URL;

    public static final String AUTHOR_PATH = "src/main/resources/node_Author_Updated.csv";
    public static final String BRIDGE_ARTICLE_AUTHOR_PATH = "src/main/resources/bridge_Article_Author.csv";
    public static final String BRIDGE_REVIEWS_PATH = "src/main/resources/bridge_Reviews_Updated.csv";
    public static final String AFFILIATION_PATH = "src/main/resources/node_Affiliations.csv";
    public static final String ARTICLE_PATH = "src/main/resources/node_Article.csv";
    public static final String BRIDGE_ARTICLE_PROCEEDING_PATH = "src/main/resources/bridge_Article_Proceedings.csv";
    public static final String BRIDGE_CITATIONS_PATH = "src/main/resources/bridge_Citations.csv";
    public static final String BRIDGE_ARTICLE_KEYWORD_PATH = "src/main/resources/bridge_Article_Keyword.csv";
    public static final String PROCEEDING_PATH = "src/main/resources/node_Proceedings.csv";
    public static final String KEYWORD_PATH = "src/main/resources/node_Keyword.csv";
    public static final String BRIDGE_KEYWORD_TOPIC_PATH = "src/main/resources/bridge_Keyword_Topic.csv";
    public static final String TOPIC_PATH = "src/main/resources/node_Topic.csv";
}
