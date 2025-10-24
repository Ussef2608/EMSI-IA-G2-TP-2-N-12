package ma.emsi.elfaize.tp2elfaize;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Scanner;

/**
 * Test 6 : Utilisation d'un outil (Météo)
 * Objectif : permettre au LLM d'appeler un outil externe pour récupérer la météo
 *             via l'API https://wttr.in/
 */
public class Test6 {

    // Interface de l’assistant IA (comme dans Test5)
    interface AssistantMeteo {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        // 1. Récupération de la clé API Gemini
        String cle = System.getenv("GEMINI_KEY");
        if (cle == null || cle.isEmpty()) {
            System.err.println("Variable d'environnement GEMINI_KEY non trouvée !");
            return;
        }

        // 2. Création du modèle Gemini (avec logging activé)
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .logRequestsAndResponses(true) //  pour observer les appels de l'outil
                .build();

        // 🛠️ 3. Création de l’assistant météo avec outil + mémoire de conversation
        AssistantMeteo assistant = AiServices.builder(AssistantMeteo.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(new MeteoTool()) // l’outil météo
                .build();

        // 4. Mode interactif
        System.out.println(" Assistant météo prêt ! Posez vos questions (ou 'exit' pour quitter).");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nVous : ");
            String question = scanner.nextLine();

            if (question.equalsIgnoreCase("exit")) break;

            try {
                String reponse = assistant.chat(question);
                System.out.println(" Assistant : " + reponse);
            } catch (Exception e) {
                System.out.println(" Erreur : " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println(" Session terminée. Merci !");
    }
}
