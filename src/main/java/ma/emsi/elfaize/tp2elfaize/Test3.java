package ma.emsi.elfaize.tp2elfaize;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;

public class Test3 {

    public static void main(String[] args) {

        String cle = System.getenv("GEMINI_KEY");

        //Création du modèle d’embeddings Gemini
        EmbeddingModel model = GoogleAiEmbeddingModel.builder()
                .apiKey(cle)
                .modelName("gemini-embedding-001")
                .outputDimensionality(300)
                .timeout(Duration.ofSeconds(5))
                .build();

        //Deux phrases à comparer
        String phrase1 = "L’intelligence artificielle transforme le monde.";
        String phrase2 = "L’IA change notre manière de vivre et de travailler.";

        //Génération des embeddings
        Response<Embedding> embedding1 = model.embed(phrase1);
        Response<Embedding> embedding2 = model.embed(phrase2);

        //Calcul de la similarité cosinus
        double similarite = CosineSimilarity.between(
                embedding1.content(),
                embedding2.content()
        );

        System.out.println("Phrase 1 : " + phrase1);
        System.out.println("Phrase 2 : " + phrase2);
        System.out.printf("Similarité cosinus : %.4f%n", similarite);

        //Exemple supplémentaire
        String phrase3 = "J’aime le café chaud le matin.";
        String phrase4 = "Le soleil brille aujourd’hui.";

        double autreSimilarite = CosineSimilarity.between(
                model.embed(phrase3).content(),
                model.embed(phrase4).content()
        );

        System.out.println("Phrase 3 : " + phrase3);
        System.out.println("Phrase 4 : " + phrase4);
        System.out.printf("Similarité cosinus : %.4f%n", autreSimilarite);    }
}
