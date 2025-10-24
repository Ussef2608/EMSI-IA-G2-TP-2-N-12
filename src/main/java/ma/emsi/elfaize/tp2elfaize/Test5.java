package ma.emsi.elfaize.tp2elfaize;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.Scanner;

/**
 * Test 5 : RAG sur un fichier PDF volumineux
 */
public class Test5 {

    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        // Récupération de la clé Gemini
        String cle = System.getenv("GEMINI_KEY");
        if (cle == null || cle.isEmpty()) {
            System.err.println("Variable d'environnement GEMINI_KEY non existante :(");
            return;
        }

        // Création du modèle Gemini
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .logRequestsAndResponses(true)
                .build();

        // Chargement du document PDF (ex. : cours_machine_learning.pdf)
        String nomDocument = "rag.pdf";
        Document document = FileSystemDocumentLoader.loadDocument(nomDocument);

        // Base vectorielle en mémoire
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // Calcul des embeddings et stockage dans la base
        System.out.println("Chargement du document et génération des embeddings...");
        EmbeddingStoreIngestor.ingest(document, embeddingStore);
        System.out.println(" Document prêt. Vous pouvez poser vos questions.");

        // Création de l’assistant IA
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();

        // Mode conversation interactive (plusieurs questions)
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n Posez votre question (ou tapez 'exit' pour quitter) : ");
            String question = scanner.nextLine();
            if (question.equalsIgnoreCase("exit")) break;

            String reponse = assistant.chat(question);
            System.out.println(" Réponse du LLM : " + reponse);
        }

        scanner.close();
        System.out.println(" Session terminée. Merci !");
    }
}