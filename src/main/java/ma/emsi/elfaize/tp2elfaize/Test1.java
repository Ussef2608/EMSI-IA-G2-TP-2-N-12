package ma.emsi.elfaize.tp2elfaize;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

public class Test1 {
    public static void main(String[] args) {

        // Récupération de la clé API stockée dans les variables d’environnement
        String cle = System.getenv("GEMINI_KEY");

        // Création du modèle Gemini avec le builder
        ChatModel modele = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .build();

        // Envoi d’une question simple
        String question = "Peux-tu me dire en une phrase ce qu’est le RAG avec un petit exemple ?";
        String reponse = modele.chat(question);

        // Affichage de la réponse
        System.out.println("Question : " + question);
        System.out.println("Réponse du modèle : " + reponse);
    }
}
