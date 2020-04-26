package OntologyCreation.Ontology;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

import java.io.*;
import java.util.*;

public class ABOX {

    public static void createAuthor() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.AUTHOR_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String authorID = row_data[0];
            String authorFullName = row_data[1];
            String affiliationID = row_data[2];


            String[] authorFullNameArr = authorFullName.split(" ");
            int arrayLen = authorFullNameArr.length;

            String authorName = authorFullNameArr[0];
            String authorLastName = "";
            if(arrayLen > 1) authorLastName = authorFullNameArr[arrayLen - 1];

            for(int i = 1; i < arrayLen - 1; i++){
                authorName += " " + authorFullNameArr[i];
            }

            String authorUri = Config.RESOURCE_URL + "AU-" + authorID;
            String affiliationUri = Config.RESOURCE_URL + "AF-" + affiliationID;

            Resource currentAuthor = model.createResource(authorUri)
                    .addProperty(FOAF.firstName, authorName)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"affiliated"), model.createResource(affiliationUri));

            if(!authorLastName.equals("")) currentAuthor.addProperty(FOAF.lastName, authorLastName);
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"author.nt");
        model.write(os,"NT");

        appendAuthorWrites();
        appendAuthorReviews();
    }

    public static void appendAuthorWrites() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.BRIDGE_ARTICLE_AUTHOR_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String articleID = row_data[0];
            String authorID = row_data[1];
            String isCorresponding = row_data[2];

            String articleUri = Config.RESOURCE_URL + "AR-" + articleID;
            String authorUri = Config.RESOURCE_URL + "AU-" + authorID;

            Resource currentAuthor = model.createResource(authorUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"write"), model.createResource(articleUri));

            if(isCorresponding.equals("1"))
                currentAuthor.addProperty(model.createProperty(Config.PROPERTY_URL+"correspondingAuthor"), model.createResource(articleUri));
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"author.nt",true);
        model.write(os,"NT");
    }

    public static void appendAuthorReviews() throws IOException{
        Model modelAut = ModelFactory.createDefaultModel();
        Model modelRev = ModelFactory.createDefaultModel();
        int reviewID = 0;

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.BRIDGE_REVIEWS_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String authorID = row_data[0];
            String articleID = row_data[1];
            String revDesc = row_data[2];
            String sugDec = row_data[3];

            String articleUri = Config.RESOURCE_URL + "AR-" + articleID;
            String authorUri = Config.RESOURCE_URL + "AU-" + authorID;
            String reviewUri = Config.RESOURCE_URL + "RE-" + reviewID;

            modelAut.createResource(authorUri)
                    .addProperty(modelAut.createProperty(Config.PROPERTY_URL+"does"), modelAut.createResource(reviewUri));

            modelRev.createResource(reviewUri)
                    .addProperty(modelRev.createProperty(Config.PROPERTY_URL+"description"), revDesc)
                    .addProperty(modelRev.createProperty(Config.PROPERTY_URL+"suggestedDecision"), sugDec)
                    .addProperty(modelRev.createProperty(Config.PROPERTY_URL+"for"), modelAut.createResource(articleUri));

            reviewID++;
        }
        csvReader.close();

        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"author.nt",true);
        modelAut.write(os,"NT");

        OutputStream os2 = new FileOutputStream(Config.OUTPUT_PATH+"review.nt");
        modelRev.write(os2,"NT");
    }

    public static void createAffiliation() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.AFFILIATION_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String affiliationID = row_data[0];
            String affiliationType = row_data[1];
            String affiliationName = row_data[2];

            String affiliationUri = Config.RESOURCE_URL + "AF-" + affiliationID;

            model.createResource(affiliationUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"affiliationType"), affiliationType)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"affiliationName"), affiliationName);
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"affiliation.nt");
        model.write(os,"NT");
    }

    public static void createArticle() throws IOException{
        Model model = ModelFactory.createDefaultModel();
        HashMap<String, String> articleProceeding = new HashMap<>();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.ARTICLE_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String articleID = row_data[0];
            String key = row_data[1];
            String title = row_data[2];
            String volume = row_data[3];
            String year = row_data[4];
            String proceedingType = row_data[5];

            String articleUri = Config.RESOURCE_URL + "AR-" + articleID;

            model.createResource(articleUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"key"), key)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"title"), title)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"volume"), volume)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"year"), year);

//            if(proceedingType.equals("conf")){
//                Random random = new Random();
//                int randomInt = random.nextInt(4);
//                if(randomInt == 0) proceedingType = "work";
//            }
            articleProceeding.put(articleID,proceedingType);
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"article.nt");
        model.write(os,"NT");

        appendArticleProceeding(articleProceeding);
        appendArticleCitations();
        appendArticleKeyword();
    }

    public static void appendArticleProceeding(HashMap<String, String> artProc) throws IOException{
        Model model = ModelFactory.createDefaultModel();
        boolean firstRowFlag = true;
        HashMap<String, String> procType = new HashMap<>();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.BRIDGE_ARTICLE_PROCEEDING_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            if(firstRowFlag){
                firstRowFlag = false;
                continue;
            }
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String articleID = row_data[0];
            String proceedingID = row_data[1];

            String articleUri = Config.RESOURCE_URL + "AR-" + articleID;
            String proceedingUri = Config.RESOURCE_URL;
            String proceedingType = "";
            if(artProc.get(articleID).equals("conf")){
                String procStr = "conf";
                if(procType.containsKey(proceedingID)){
                    procStr = procType.get(proceedingID);
                }else{
                    Random random = new Random();
                    int randomInt = random.nextInt(4);
                    if(randomInt == 0) procStr = "work";
                    procType.put(proceedingID,procStr);
                }

                proceedingUri += "PRO-" + proceedingID;
                if(procStr.equals("conf")) proceedingType = "conference";
                else proceedingType = "workshop";
            }else{
                proceedingUri += "JO-" + proceedingID;
                proceedingType = "journal";
            }

            model.createResource(articleUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"published_in_"+proceedingType), model.createResource(proceedingUri));
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"article.nt",true);
        model.write(os,"NT");
    }

    public static void appendArticleCitations() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.BRIDGE_CITATIONS_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String articleID = row_data[0];
            String citedArticleID = row_data[1];

            String articleUri = Config.RESOURCE_URL + "AR-" + articleID;
            String citedArticleUri = Config.RESOURCE_URL + "AR-" + citedArticleID;

            model.createResource(articleUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"cites"), model.createResource(citedArticleUri));
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"article.nt",true);
        model.write(os,"NT");
    }

    public static void appendArticleKeyword() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.BRIDGE_ARTICLE_KEYWORD_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String articleID = row_data[0];
            String keywordID = row_data[1];

            String articleUri = Config.RESOURCE_URL + "AR-" + articleID;
            String keywordUri = Config.RESOURCE_URL + "KW-" + keywordID;

            model.createResource(articleUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"contains"), model.createResource(keywordUri));
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"article.nt",true);
        model.write(os,"NT");
    }

    public static void createProceeding() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.PROCEEDING_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String proceedingID = row_data[0];
            String title = row_data[1];
            String key = row_data[2];
            String series = row_data[3];
            String[] nameAndEdition = row_data[4].split(",");
            String volume = row_data[5];
            String year = row_data[6];

            String name = nameAndEdition[0];

            String proceedingUri = Config.RESOURCE_URL;

            String procType = key.split("/")[0];
            if(procType.equals("conf")) proceedingUri += "PRO-" + proceedingID;
            else proceedingUri += "JO-" + proceedingID;

            Resource currentProc = model.createResource(proceedingUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"title"), title)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"key"), key)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"name"), name)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"year"), year);

            if(!series.equals("")) currentProc.addProperty(model.createProperty(Config.PROPERTY_URL+"series"), series);

            if(procType.equals("conf")) {
                String edition = "";
                for (int i = 1; i < nameAndEdition.length; i++) {
                    edition += nameAndEdition[i];
                }
                if(!edition.equals("")) currentProc.addProperty(model.createProperty(Config.PROPERTY_URL+"edition"), edition);
            }else
                currentProc.addProperty(model.createProperty(Config.PROPERTY_URL+"volume"), volume);
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"proceeding.nt");
        model.write(os,"NT");
    }

    public static void createKeyword() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.KEYWORD_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String keywordID = row_data[0];
            String keyword = row_data[1];

            String keywordUri = Config.RESOURCE_URL + "KW-" + keywordID;

            model.createResource(keywordUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"keyword"), keyword);
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"keyword.nt");
        model.write(os,"NT");

        appendKeywordTopic();
    }

    public static void appendKeywordTopic() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.BRIDGE_KEYWORD_TOPIC_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String keywordID = row_data[0];
            String topicID = row_data[1];

            String keywordUri = Config.RESOURCE_URL + "KW-" + keywordID;
            String topicUri = Config.RESOURCE_URL + "TO-" + topicID;

            model.createResource(keywordUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"related_to"), model.createResource(topicUri));
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"keyword.nt",true);
        model.write(os,"NT");
    }

    public static void createTopic() throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // read the csv line by line
        BufferedReader csvReader = new BufferedReader(new FileReader(Config.TOPIC_PATH));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] row_data = row.split(Config.CSV_SPLIT_REGEX);
            String topicID = row_data[0];
            String topic = row_data[1];

            String topicUri = Config.RESOURCE_URL + "TO-" + topicID;

            model.createResource(topicUri)
                    .addProperty(model.createProperty(Config.PROPERTY_URL+"topic"), topic);
        }
        csvReader.close();
        OutputStream os = new FileOutputStream(Config.OUTPUT_PATH+"topic.nt");
        model.write(os,"NT");
    }
}
