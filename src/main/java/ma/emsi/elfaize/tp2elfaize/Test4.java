package ma.emsi.elfaize.tp2elfaize;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

/**
 Réponse LLM de test1
 Question : Comment s'appelle le chat de Pierre ?
 Réponse du modèle : Il n'y a pas de chat de Pierre célèbre ou universellement connu dont je puisse vous donner le nom. "Pierre" est un prénom très courant !

 Pourriez-vous me donner plus de contexte ? S'agit-il d'un personnage de livre, de film, d'une histoire en particulier ?
 **/
/**
 * Le RAG facile !
 */

public class Test4 {

    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        // Clé API Gemini
        String llmKey = System.getenv("GEMINI_KEY");

        // Modèle de chat Gemini
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(llmKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .build();

        // Modèle d’embedding Gemini
        EmbeddingModel embeddingModel = GoogleAiEmbeddingModel.builder()
                .apiKey(llmKey)
                .outputDimensionality(300)
                .modelName("gemini-embedding-001")
                .build();

        // Chargement du document
        String nomDocument = "infos.txt"; // fichier à la racine du projet
        Document document = FileSystemDocumentLoader.loadDocument(nomDocument);

        // Base vectorielle en mémoire
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // Création de l’ingestor et ingestion du document
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);

        // Création du content retriever avec embeddingModel OBLIGATOIRE
        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3) // optionnel : nombre d’éléments similaires à récupérer
                .build();

        // Création de l’assistant IA avec mémoire et RAG
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(retriever)
                .build();

        // Question test
//        String question = "Comment s'appelle le chat de Pierre ?";
          String question = "Quelle est la capitale de France ?";
        // Réponse du LLM :
        // Question : Comment s'appelle le chat de Pierre ?
        //Réponse : Le chat de Pierre s'appelle Felix.

        //Réponse à la question : Quelle est la capitale de France aprèes modification de la dernière ligne de infos.txt
        //Réponse : La capitale de France est Marseille

        // Exécution
        String reponse = assistant.chat(question);
        System.out.println("Question : " + question);
        System.out.println("Réponse : " + reponse);
    }
}
